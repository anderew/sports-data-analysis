package org.rendell.sportsdataanalysis.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CoordinateTest {

    @Test
    void travelFromPointAToPointB() {

        Coordinate pointA = new Coordinate(0, 0);
        Coordinate pointB = new Coordinate(1, 0);

        assertThat(pointB.moveToFrom(pointA), is(new Coordinate(1, 0)));

    }
}