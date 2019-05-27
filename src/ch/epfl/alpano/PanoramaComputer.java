package ch.epfl.alpano;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama.Builder;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

/**
 * Class PanoramaComputer: represents a panorama computer
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 */
public final class PanoramaComputer {

    /*
     * Field:
     */
    private final ContinuousElevationModel dem;

    /**
     * Constructs a PanoramaComputer from the given continuous DEM
     * 
     * @param dem
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem = Objects.requireNonNull(dem);
    }

    /**
     * Method which creates a Panorama from the given parameters
     * 
     * @param parameters:
     *            data which is utilized to create a parameter
     * @return the Panorama constructed from the given parameters
     */
    public Panorama computePanorama(PanoramaParameters parameters) {
        Objects.requireNonNull(parameters);
        int width = parameters.width();
        int height = parameters.height();

        Builder aBuilder = new Builder(parameters);

        for (int x = 0; x < width; x++) {

            ElevationProfile aProfile = new ElevationProfile(dem,
                    parameters.observerPosition(), parameters.azimuthForX(x),
                    parameters.maxDistance());

            double initRay = 0;

            for (int y = height - 1; y >= 0; y--) {
                double altitudeForY = parameters.altitudeForY(y);
                DoubleUnaryOperator f = rayToGroundDistance(aProfile,
                        parameters.observerElevation(),
                        Math.tan(altitudeForY));

                double x1 = Math2.firstIntervalContainingRoot(f, initRay,
                        parameters.maxDistance(), 64d);

                if (x1 != Double.POSITIVE_INFINITY) {
                    initRay = Math2.improveRoot(f, x1, x1 + 64d, 4d);

                    GeoPoint aPoint = aProfile.positionAt(initRay);

                    aBuilder.setDistanceAt(x, y, (float) (initRay
                            / Math.cos(altitudeForY)));
                    aBuilder.setLongitudeAt(x, y, (float) aPoint.longitude());
                    aBuilder.setLatitudeAt(x, y, (float) aPoint.latitude());
                    aBuilder.setElevationAt(x, y,
                            (float) dem.elevationAt(aPoint));
                    aBuilder.setSlopeAt(x, y, (float) dem.slopeAt(aPoint));
                } else {
                    break;
                }
            }
        }

        return aBuilder.build();
    }

    /**
     * Method which returns the function required to compute the distance from
     * the ray to the ground
     * 
     * @param profile:
     *            altimetric profile
     * @param ray0:
     *            double variable indicating the inital altitude of the ray
     * @param raySlope:
     *            double variable indicating the slope of the ray
     * @return: the function which will be utilized to compute the distance from
     *          where the ray was thrown to the ground.
     */
    public static DoubleUnaryOperator rayToGroundDistance(
            ElevationProfile profile, double ray0, double raySlope) {

        double r0 = ray0;
        double tanAlpha = raySlope;
        double k = 0.13;
        double q = (1 - k) / (2 * Distance.EARTH_RADIUS);

        return x -> {
            return r0 + x * tanAlpha - profile.elevationAt(x)
                    + q * Math2.sq(x);
        };
    }
}
