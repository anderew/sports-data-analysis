package org.rendell.sportsdataanalysis.strava;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.strava.dto.Activity;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class RouteTransformerTest {

    @Test
    public void shouldTransformAnActivityFromStravaIntoASimpleRoute() throws Exception {


        ObjectMapper om = new ObjectMapper();

        InputStream is = this.getClass().getResourceAsStream("/json/activity.json");
        Activity activity = om.readValue(is, Activity.class);

        RouteTransformer routeTransformer = new RouteTransformer();

        Route actualRoute = routeTransformer.fromActivity(activity);

        Route expectedRoute = new Route("FAKE_POLYLINE_FOR_TEST");
        assertThat(actualRoute.getPolyline(), is(expectedRoute.getPolyline()));
    }

}