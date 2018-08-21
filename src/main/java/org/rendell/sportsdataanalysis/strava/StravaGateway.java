package org.rendell.sportsdataanalysis.strava;

import org.rendell.sportsdataanalysis.strava.dto.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StravaGateway {

    private final String ACTIVITY_URL = "https://www.strava.com/api/v3/activities/1785730333?include_all_efforts=true";

    private final RestTemplate restTemplate;

    private final String authorisation;

    public StravaGateway(@Autowired RestTemplate restTemplate,
                         @Value("${strava.authorisation}") String authorisation) {
        this.restTemplate = restTemplate;
        this.authorisation = authorisation;
    }

    public Activity loadActivity(String activityId) {

        ResponseEntity<Activity> response = restTemplate.exchange(ACTIVITY_URL, HttpMethod.GET, new HttpEntity<Activity>(createHeaders()), Activity.class);

        return response.getBody();
    }

    private HttpHeaders createHeaders(){
        return new HttpHeaders() {{
            set( "Authorization", authorisation );
        }};
    }
}
