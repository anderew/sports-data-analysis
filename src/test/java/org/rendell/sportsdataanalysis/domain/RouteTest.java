package org.rendell.sportsdataanalysis.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
class RouteTest {

    @Test
    void whenPassedAPolylineShouldProduceAnOrderedListOfAbsoluteCoordinates() {

        // This is a simple polyline from the head of Loch Avon to Etchachan
        String inputPolyline = "itn{IxqfUnEk@GgUle@{K";

        Route actual = Route.fromPolyline(inputPolyline);

        assertThat(actual.getAsAbsoluteCoords(),
                contains(new Coordinate(-3.64333,57.09653),
                        new Coordinate(-3.64311,57.09549),
                        new Coordinate(-3.63955,57.09553),
                        new Coordinate(-3.63749,57.08938)
                ));
    }

    @Test
    void shouldBeAbleToEncodeAndDecodePolyline() {

        Route input = new Route(Arrays.asList(new Coordinate(0,0), new Coordinate(0,54)));

        assertThat(input, is(Route.fromPolyline(input.getPolyline())));
    }

    @Test
    void simplePolylineDecodeFromGoogleExample() {

        String inputPolyline = "wgkyHjjSkMj@";

        Route actual = Route.fromPolyline(inputPolyline);

        assertThat(actual.getAsAbsoluteCoords(),
                contains(new Coordinate(-0.10422,51.5086),
                        new Coordinate(-0.10444,51.5109)
                ));
    }

    @Test
    void simplePolylineEncodeFromGoogleExample() {

        Route route = new Route(Arrays.asList(new Coordinate(-120.2,38.5),
                        new Coordinate(-120.95,40.7),
                        new Coordinate(-126.453,43.252)));

        assertThat(route.getPolyline(), is( "_p~iF~ps|U_ulLnnqC_mqNvxq`@"));
    }
}