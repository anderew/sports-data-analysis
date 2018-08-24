package org.rendell.sportsdataanalysis;

import org.rendell.sportsdataanalysis.domain.Coordinate;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.strava.StravaGateway;
import org.rendell.sportsdataanalysis.streamprocessors.RouteTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MappingAnalyser {

    private final StravaGateway stravaGateway;
    private final RouteTransformer routeTransformer;

    @Autowired
    public MappingAnalyser(StravaGateway stravaGateway, RouteTransformer routeTransformer) {
        this.stravaGateway = stravaGateway;
        this.routeTransformer = routeTransformer;
    }

    public Route createSingleRoute(List<String> activities) {
        // TODO: Collect into route
        List<Coordinate> coordinates = activities.stream()
                .map(activityId -> stravaGateway.loadActivity(activityId))
                .map(activity -> routeTransformer.fromActivity(activity))
                .map(r -> r.getAsAbsoluteCoords())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new Route(coordinates);
    }
}
