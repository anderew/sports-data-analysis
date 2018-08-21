package org.rendell.sportsdataanalysis;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.rendell.sportsdataanalysis.strava.StravaGateway;
import org.rendell.sportsdataanalysis.strava.dto.Activity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEncryptableProperties
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public CommandLineRunner run(StravaGateway stravaGateway) throws Exception {
        return args -> {
            String activityId = "1785730333";
            Activity activity = stravaGateway.loadActivity(activityId);
            log.info(activity.toString());
        };
    }
}
