package ch.epfl.alpano;

import java.util.Locale;


/**
 * Class GeoPoint: Represents a point at the surface of the Earth whose position
 * is given in system of spherical coordinates.
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 */
public final class GeoPoint {

    /*
     * Longitude of our point on Earth
     */
    private final double longitude;

    /*
     * Latitude of our point on Earth
     */
    private final double latitude;

    /**
     * Constructs a GeoPoint with the given longitude and latitude
     * 
     * @param longitude
     * @param latitude
     */
    public GeoPoint(double longitude, double latitude) {
        Preconditions
                .checkArgument(longitude >= -Math.PI && longitude <= Math.PI);
        Preconditions.checkArgument(
                latitude >= (-Math.PI / 2d) && latitude <= Math.PI / 2d);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Method which allows anyone to get a point's longitude
     * 
     * @return a copy of the real longitude of the object
     */
    public double longitude() {
        return this.longitude;
    }

    /**
     * Method which allows anyone to get a point's latitude
     * 
     * @return a copy of the real latitude of the object
     */
    public double latitude() {
        return this.latitude;
    }

    /**
     * Method which computes the distance between two points on the surface of
     * the Earth
     * 
     * @param that
     *            is a point on Earth
     * @return the distance in meters between two points
     */
    public double distanceTo(GeoPoint that) {
        double lambda1 = this.longitude;
        double phi1 = this.latitude;

        double lambda2 = that.longitude;
        double phi2 = that.latitude;

        double alpha = 2 * Math
                .asin(Math.sqrt(Math2.haversin(phi1 - phi2) + Math.cos(phi1)
                        * Math.cos(phi2) * Math2.haversin(lambda1 - lambda2)));
        return Distance.toMeters(alpha);
    }

    /**
     * Method which finds the azimuth angle between two distinct points on the
     * Earth
     * 
     * @param that:
     *            point on the surface of the Earth
     * @return the azimuth angle between two given points
     */
    public double azimuthTo(GeoPoint that) {
        double lambda1 = this.longitude;
        double phi1 = this.latitude;

        double lambda2 = that.longitude;
        double phi2 = that.latitude;

        double betaMath = Math.atan2(
                Math.sin(lambda1 - lambda2) * Math.cos(phi2),
                Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1)
                        * Math.cos(phi2) * Math.cos(lambda1 - lambda2));
        return Azimuth.canonicalize(-betaMath);
    }

    /**
     * Method that returns a string made of the coordinates of the point (in
     * degrees)
     */
    @Override
    public String toString() {
        double longDeg = Math.toDegrees(this.longitude);
        double latDeg = Math.toDegrees(this.latitude);
        Locale l = null;
        String s = String.format(l, "(%.4f,%.4f)", longDeg, latDeg);
        return s;
    }
}
