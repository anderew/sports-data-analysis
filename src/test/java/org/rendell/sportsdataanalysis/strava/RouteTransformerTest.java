package org.rendell.sportsdataanalysis.strava;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.rendell.sportsdataanalysis.domain.Coordinate;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.dto.Activity;
import org.rendell.sportsdataanalysis.streamprocessors.RouteTransformer;

import java.io.InputStream;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RouteTransformerTest {

    @Test
    public void shouldTransformAnActivityFromStravaIntoASimpleRoute() throws Exception {
        ObjectMapper om = new ObjectMapper();

        InputStream is = this.getClass().getResourceAsStream("/json/activity.json");
        Activity activity = om.readValue(is, Activity.class);

        RouteTransformer routeTransformer = new RouteTransformer();

        Route actualRoute = routeTransformer.fromActivity(activity);

        assertThat(actualRoute.getAsAbsoluteCoords().size(), is(1279));
    }

}