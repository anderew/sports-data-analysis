package org.rendell.sportsdataanalysis;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.rendell.sportsdataanalysis.domain.Coordinate;
import org.rendell.sportsdataanalysis.domain.Route;
import org.rendell.sportsdataanalysis.streamprocessors.RouteTransformer;
import org.rendell.sportsdataanalysis.strava.StravaGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableEncryptableProperties
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
