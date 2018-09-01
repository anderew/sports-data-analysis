package org.rendell.sportsdataanalysis.google;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.rendell.sportsdataanalysis.domain.Coordinate;
import org.rendell.sportsdataanalysis.domain.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@RestClientTest(value = GoogleMapsApiGateway.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class GoogleMapsApiGatewayTest {

    @Autowired
    private GoogleMapsApiGateway googleMapsApiGateway;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void whenPassedARouteShouldCreateNewRouteWithElevationUsingAsManySamplesAsThereAreCoordinatesUpto100() throws Exception {

        String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/elevation-api.json"));

        // ??_ibE_ibE is the polyline for the trival route below
        server.expect(once(),
                requestTo("https://maps.googleapis.com/maps/api/elevation/json?path=enc:??_ibE_ibE&samples=2&key=fake_key"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        Route route = new Route(Arrays.asList(new Coordinate(0,0,0), new Coordinate(1, 1, 1)));

        Route actual = googleMapsApiGateway.elaborateWithElevation(route);

        assertThat(actual.getAsAbsoluteCoords(), contains(
                new Coordinate(-3.64333,57.09653,729),
                new Coordinate(-3.63901,57.09392,804),
                new Coordinate(-3.63749,57.08938,917)));

    }


    // TODO: Empty list rejected
    // TODO: More than one hundred coordinates
}