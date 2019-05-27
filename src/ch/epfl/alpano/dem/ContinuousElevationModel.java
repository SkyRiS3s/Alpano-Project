package ch.epfl.alpano.dem;

import java.util.Objects;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Math2;

/**
 * Class ContinuousElevationModel: reprsents a continous DEM obtained by
 * interpolation of a discrete DEM
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public final class ContinuousElevationModel {

    /*
     * DEM from which the continuous elevation model is constructed
     */
    private final DiscreteElevationModel dem;

    /*
     * double variable which represents the distance which separates two samples
     */
    private final static double DISTANCE = Distance
            .toMeters(1.0 / DiscreteElevationModel.SAMPLES_PER_RADIAN);

    /**
     * Constructor of the class
     * 
     * @param dem:
     *            DEM from which the Continuous Model Elevation is constructed
     */
    public ContinuousElevationModel(DiscreteElevationModel dem) {
        Objects.requireNonNull(dem);
        this.dem = dem;
    }

    public void close() throws Exception {
        dem.close();
    }

    public Interval2D extent() {
        return dem.extent();
    }

    public double elevationSample(int x, int y) {
        return dem.extent().contains(x, y) ? dem.elevationSample(x, y) : 0;
    }

    /**
     * Method which determines the elevation at a given point in Earth using
     * bilinear n
     * 
     * @param p:
     *            GeoPoint for which the elevation is computed
     * @return The elevation at the given GeoPoint obtained by bilinear
     *         interpolation
     */
    public double elevationAt(GeoPoint p) {
        double x = DiscreteElevationModel.sampleIndex(p.longitude());
        double y = DiscreteElevationModel.sampleIndex(p.latitude());
        int xf = (int) Math.floor(x);
        int yf = (int) Math.floor(y);
        double p11 = elevationSample(xf, yf);
        double p12 = elevationSample(xf, yf + 1);
        double p21 = elevationSample(xf + 1, yf);
        double p22 = elevationSample(xf + 1, yf + 1);
        return Math2.bilerp(p11, p21, p12, p22, x - xf, y - yf);
    }

    /**
     * Private method which computes the slope of a DEM
     * 
     * @param x:
     *            int variable which corresponds to the longitude of the point
     *            (floored)
     * @param y:
     *            int variable which corresponds to the latitude of the point
     *            (floored)
     * @return the slope at a given point
     */
    private double sampleSlope(int x, int y) {
        double e = elevationSample(x, y);
        double dZaSQUARED = Math2.sq(elevationSample(x, y + 1) - e);
        double dZbSQUARED = Math2.sq(elevationSample(x + 1, y) - e);
        return Math.acos(DISTANCE
                / Math.sqrt((Math2.sq(DISTANCE) + dZaSQUARED + dZbSQUARED)));
    }

    /**
     * Method which returns the slope of a continuous elevation model
     * 
     * @param p:
     *            GeopPoint for which we compute the slope using bilinear
     *            interpolation
     * @return slope of a continuous elevation model.
     */
    public double slopeAt(GeoPoint p) {
        double x = DiscreteElevationModel.sampleIndex(p.longitude());
        double y = DiscreteElevationModel.sampleIndex(p.latitude());

        int xf = (int) Math.floor(x);
        int yf = (int) Math.floor(y);

        double[] slopes = new double[4];

        slopes[0] = sampleSlope(xf, yf);
        slopes[1] = sampleSlope(xf, yf + 1);
        slopes[2] = sampleSlope(xf + 1, yf);
        slopes[3] = sampleSlope(xf + 1, yf + 1);

        return Math2.bilerp(slopes[0], slopes[2], slopes[1], slopes[3], x - xf,
                y - yf);
    }
}
