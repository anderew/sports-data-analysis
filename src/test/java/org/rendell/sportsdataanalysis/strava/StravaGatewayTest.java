package org.rendell.sportsdataanalysis.strava;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rendell.sportsdataanalysis.Application;
import org.rendell.sportsdataanalysis.strava.dto.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(SpringExtension.class)
@RestClientTest(value = StravaGateway.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class StravaGatewayTest {

    @Autowired
    private StravaGateway stravaGateway;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void loadActivity() throws Exception {

        String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/activity.json"));

        server.expect(once(),
                requestTo("https://www.strava.com/api/v3/activities/my_activity_id"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        Activity actual = stravaGateway.loadActivity("my_activity_id");

        assertThat(actual.getDistance(), is(79699.0));
    }
}