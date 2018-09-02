package org.rendell.sportsdataanalysis.google;

import lombok.extern.slf4j.Slf4j;
import org.rendell.sportsdataanalysis.domain.Coordinate;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.dto.Elevation_api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GoogleMapsApiGateway {

    public static final int MAX_SAMPLES_PER_ROUTE = 100;
    private static final String ELEVATION_URL = "https://maps.googleapis.com/maps/api/elevation/json?path=enc:{polyline}&samples={samples}&key={key}";
    private static final int MAX_COORDS_PER_API_CALL = 500;
    private final RestTemplate restTemplate;

    private final String applicationKey;

    public GoogleMapsApiGateway(@Autowired RestTemplate restTemplate,
                                @Value("${google.key}") String applicationKey) {
        this.restTemplate = restTemplate;
        this.applicationKey = applicationKey;
    }


    public Route elaborateWithElevation(Route route) {

        // Split route into series of sub-routes, each with a maximum number of coordinates
        // Call elaborateSingleRouteWithElevation for each sub route
        // Combine the elaborated routes into the entire route again
        // Will potentially reduce resolution if max coords > samples but this might not be bad thing
        List<Route> subroutes = split(route, MAX_COORDS_PER_API_CALL);

        List<Coordinate> coords = subroutes.stream()
                .flatMap(r -> elaborateSingleRouteWithElevation(r.getAsAbsoluteCoords()).stream())
                .collect(Collectors.toList());

        log.debug("Input route had {} coordinates, output has {} coordinates",
                route.getAsAbsoluteCoords().size(),
                coords.size());

        return new Route(coords);
    }

    private List<Route> split(Route route, int maxCoordsPerRoute) {
        List<Route> subroutes = new ArrayList<>();

        for(int from = 0 ; from < route.getAsAbsoluteCoords().size() ; from+=maxCoordsPerRoute) {
            int to = from + maxCoordsPerRoute > route.getAsAbsoluteCoords().size() ?
                    route.getAsAbsoluteCoords().size() :
                    from + maxCoordsPerRoute;

            subroutes.add(new Route(route.getAsAbsoluteCoords().subList(from, to)));
        }
        return subroutes;
    }

    /**
     * Less calls to API(?) but will reduce resolution of route
     */
    private List<Coordinate> elaborateSingleRouteWithElevation(List<Coordinate> coords) {

        int samples = coords.size() > MAX_SAMPLES_PER_ROUTE ?
                MAX_SAMPLES_PER_ROUTE
                : coords.size();

        //Route reducedResolutionRoute = reduceResolution(route);

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("polyline", new Route(coords).getPolyline());
        uriVariables.put("samples", Integer.toString(samples));
        uriVariables.put("key", applicationKey);

        log.debug("Input polyline {}", uriVariables.get("polyline"));

        Elevation_api response = null;
        try {
            response = restTemplate.getForObject(ELEVATION_URL, Elevation_api.class, uriVariables);
        } catch (HttpClientErrorException e) {
            log.warn("Error from API: {} {} {}", e.getResponseBodyAsString(), e.getStatusText(), e.getMessage());
            throw e;
        }

        List<Coordinate> coordsWithElevation = response.getResults().stream()
                .map(r -> new Coordinate(r.getLocation().getLng(), r.getLocation().getLat(), r.getElevation()))
                .collect(Collectors.toList());

        log.debug("Input route has {} coordinates, after calling Google Maps API has {} coordinates",
                coords.size(),
                coordsWithElevation.size());



        return coordsWithElevation;
    }

    /**
     * Suspect because there is a limit on the size of the HTTP GET payload very long routes
     * cause a client side error. Need to do this more scientifically -
     * maybe by making mulitple polyline calls?
     * @param route
     * @return
     */
    private Route reduceResolution(Route route) {

        if(route.getAsAbsoluteCoords().size() < MAX_SAMPLES_PER_ROUTE) {
            return route;
        }

        // TODO this properly!
        int i = 0;
        List<Coordinate> reduced = new ArrayList<>();
        for(Coordinate c : route.getAsAbsoluteCoords()) {
            if(i++ % 10 == 0)
                reduced.add(c);
        }

        return new Route(reduced);
    }


}
