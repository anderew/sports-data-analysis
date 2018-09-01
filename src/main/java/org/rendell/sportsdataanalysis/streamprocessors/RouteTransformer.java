package org.rendell.sportsdataanalysis.streamprocessors;

import lombok.NonNull;
import org.rendell.sportsdataanalysis.domain.Coordinate;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.dto.Activity;
import org.rendell.sportsdataanalysis.dto.Map;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

@Component
public class RouteTransformer {

    public Route fromActivity(@NonNull Activity activity) {

        Route route = Route.fromPolyline(extractPolylineFromMap(activity.getMap()));

        return route;
    }

    private String extractPolylineFromMap(Map map) {
        return map.getPolyline();
    }

    public List<Coordinate> toAbsoluteCoords(Route r) {
        return null;
    }
}
