package org.rendell.sportsdataanalysis;

import lombok.extern.slf4j.Slf4j;
import org.rendell.sportsdataanalysis.domain.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CommandLineRunner {

    @Bean
    public org.springframework.boot.CommandLineRunner run(MappingAnalyser mappingAnalyser) throws Exception {
        return args -> {
            List<String> activities = Arrays.asList("1785730333");

            Route route = mappingAnalyser.createSingleRoute(activities);

            log.info("{}", route);
        };
    }

}
