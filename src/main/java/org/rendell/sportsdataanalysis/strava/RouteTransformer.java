package org.rendell.sportsdataanalysis.strava;

import lombok.NonNull;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.strava.dto.Activity;
import org.rendell.sportsdataanalysis.strava.dto.Map;
import org.springframework.stereotype.Component;

@Component
public class RouteTransformer {
    public Route fromActivity(@NonNull Activity activity) {

        Route route = new Route(extractPolylineFromMap(activity.getMap()));

        return route;
    }

    private String extractPolylineFromMap(Map map) {
        return map.getPolyline();
    }
}
