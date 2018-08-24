package org.rendell.sportsdataanalysis.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Slf4j
public class Route {

    private final List<Coordinate> asAbsoluteCoords;

    public static Route fromPolyline(String polyline) {

        polyline.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

        polyline.chars()
                .boxed()
                .map(i -> i - 63)
                .map(i -> ~(i | 0x20) )
                .forEach(e -> log.debug("{}", Integer.toBinaryString(e)));



        // TODO
        return null;
    }

    public String getPolyline() {

        // Cant do this as a stream because elements are processed differently based on side effects
        List<Coordinate> relativeCoordinates = new ArrayList<>();
        Coordinate previousCoordinate = null;
        for(Coordinate originalCoordinate : asAbsoluteCoords) {
            Coordinate relativeToPrevious = previousCoordinate == null ? originalCoordinate : originalCoordinate.moveToFrom(previousCoordinate);
            previousCoordinate = originalCoordinate;
            relativeCoordinates.add(relativeToPrevious);
        }

        log.debug("{}", relativeCoordinates);

        Coordinate lastCoordinate = null;
        relativeCoordinates.stream()
                .map(c -> new Coordinate(c.getLongitude() * 1E5, c.getLatitude() * 1E5))
                .map(c -> new Coordinate(round(c.getLongitude()), round(c.getLatitude())))
                .forEach(e -> log.debug("{} {}",  new String(Base64.encodeBase64(Double.toString(e.getLatitude()).getBytes())), new String(Base64.encodeBase64(Double.toString(e.getLongitude()).getBytes()))));

        // TODO
        return null;
    }

    private Double round(Double d) {
        return new Double(Math.round(d));
    }


}
