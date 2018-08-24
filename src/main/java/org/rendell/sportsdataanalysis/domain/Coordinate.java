package org.rendell.sportsdataanalysis.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Coordinate {

    private final double longitude;
    private final double latitude;

    public Coordinate moveToFrom(Coordinate anotherCoordinate) {
        return new Coordinate(longitude - anotherCoordinate.getLongitude() , latitude - anotherCoordinate.getLatitude());
    }
}
