package org.rendell.sportsdataanalysis;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.rendell.sportsdataanalysis.domain.Coordinate;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.strava.StravaGateway;
import org.rendell.sportsdataanalysis.strava.dto.Activity;
import org.rendell.sportsdataanalysis.streamprocessors.RouteTransformer;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class MappingAnalyserTest implements WithAssertions {

    @InjectMocks
    private MappingAnalyser mappingAnalyser;

    @Mock
    private StravaGateway stravaGateway;

    @Mock
    private RouteTransformer routeTransformer;

    @Mock
    private Activity firstActivity;

    @Mock
    private Activity secondActivity;

    @Test
    void createSingleRoute() {

        when(stravaGateway.loadActivity("1")).thenReturn(firstActivity);
        when(stravaGateway.loadActivity("2")).thenReturn(secondActivity);

        Route firstRoute = new Route(asList(new Coordinate(1,1), new Coordinate(2,2)));
        when(routeTransformer.fromActivity(firstActivity)).thenReturn(firstRoute);
        Route secondRoute = new Route(asList(new Coordinate(3,3), new Coordinate(4,4)));
        when(routeTransformer.fromActivity(secondActivity)).thenReturn(secondRoute);

        Route actual = mappingAnalyser.createSingleRoute(Arrays.asList("1", "2"));

        Route expected = new Route(asList(new Coordinate(1,1),
                new Coordinate(2,2),
                new Coordinate(3,3),
                new Coordinate(4,4)));

        assertThat(actual).isEqualTo(expected);
    }
}