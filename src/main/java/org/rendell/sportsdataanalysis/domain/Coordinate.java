package org.rendell.sportsdataanalysis.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Coordinate {

    private final double longitude;
    private final double latitude;
    private final double altitudeInMetres;

    public Coordinate(double longitude, double latitude, double altitudeInMetres) {
        this.longitude = round(longitude);
        this.latitude = round(latitude);
        this.altitudeInMetres = Math.round(altitudeInMetres);
    }

    /**
     * Returns this Coordinate expressed as a delta from anotherCoordinate
     * @param anotherCoordinate
     * @return the delta Coordinate (i.e. not absolute)
     */
    public Coordinate moveToFrom(Coordinate anotherCoordinate) {
        return new Coordinate(
                longitude - anotherCoordinate.getLongitude() ,
                latitude - anotherCoordinate.getLatitude(),
                altitudeInMetres - anotherCoordinate.getAltitudeInMetres());
    }

    private static double round(double d) {
        return Math.round(d * 1E5) / 1E5;
    }
}
