package org.rendell.sportsdataanalysis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.rendell.sportsdataanalysis.domain.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CommandLineRunner {

    @Bean
    public org.springframework.boot.CommandLineRunner run(MappingAnalyser mappingAnalyser) throws Exception {
        return args -> {
            List<String> activities = Arrays.asList("1685166835",
                    "1685167952",
                    "1687169902",
                    "1687170900",
                    "1689539962",
                    "1691061421");

            Route route = mappingAnalyser.createRouteFrom(activities);

            log.info("{}", route);

            final CSVPrinter printer = new CSVPrinter(System.out, CSVFormat.DEFAULT.withHeader("Altitude"));

            route.getAsAbsoluteCoords().stream()
                    .forEach(c -> {
                        try {
                            printer.printRecord(c.getAltitudeInMetres());
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    });

        };
    }

}
