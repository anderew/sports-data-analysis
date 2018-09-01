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
    private final RestTemplate restTemplate;

    private final String applicationKey;

    public GoogleMapsApiGateway(@Autowired RestTemplate restTemplate,
                                @Value("${google.key}") String applicationKey) {
        this.restTemplate = restTemplate;
        this.applicationKey = applicationKey;
    }

    /**
     * More expensive (in terms of calls to Google API) than passing a polyline
     * @param coordinates
     * @return
     */
    public List<Coordinate> elaborateWithElevation(List<Coordinate> coordinates) {
        // TODO
        return null;
    }

    /**
     * Less calls to API(?) but will reduce resolution of route
     */
    public Route elaborateWithElevation(Route route) {

        int samples = route.getAsAbsoluteCoords().size() > MAX_SAMPLES_PER_ROUTE ?
                MAX_SAMPLES_PER_ROUTE
                : route.getAsAbsoluteCoords().size();

        Route reducedResolutionRoute = reduceResolution(route);

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("polyline", reducedResolutionRoute.getPolyline());
        uriVariables.put("samples", Integer.toString(samples));
        uriVariables.put("key", applicationKey);

        log.debug("Input polyline {}", route.getPolyline());

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

        Route routeWithElevation = new Route(coordsWithElevation);

        log.debug("Input route has {} coordinates, after calling Google Maps API has {} coordinates",
                route.getAsAbsoluteCoords().size(),
                routeWithElevation.getAsAbsoluteCoords().size());

        log.debug("Source polyline: {}", route.getPolyline());
        log.debug("Output polyline: {}", routeWithElevation.getPolyline());

        return routeWithElevation;
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
