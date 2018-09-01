package org.rendell.sportsdataanalysis.domain;

import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

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

        EncodedPolyline encodedPolyline = new EncodedPolyline(polyline);

        List<LatLng> points = encodedPolyline.decodePath();

        List<Coordinate> cords = points.stream()
                .map(p -> new Coordinate(p.lng, p.lat, 0d))
                .collect(Collectors.toList());

        return new Route(cords);
    }

    public String getPolyline() {

        List<LatLng> points = asAbsoluteCoords.stream()
                .map(c -> new LatLng(c.getLatitude(), c.getLongitude()))
                .collect(Collectors.toList());

        EncodedPolyline encodedPolyline = new EncodedPolyline(points);

        return encodedPolyline.getEncodedPath();
    }



}
