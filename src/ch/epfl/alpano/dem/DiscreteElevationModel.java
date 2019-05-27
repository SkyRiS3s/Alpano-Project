package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Preconditions;

/**
 * Interface DiscreteElevationModel: represents a discrete DEM
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 * 
 */
public interface DiscreteElevationModel extends AutoCloseable {
    /*
     * Integer which contains the number of samples per degree of a DEM
     */
    public static final int SAMPLES_PER_DEGREE = 3600;

    /*
     * double variable which contains the number of samples per radians of a DEM
     */
    public static final double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE * 360
            / Math2.PI2;

    /**
     * Method which returns the corresponding index of a given angle
     * 
     * @param angle
     *            double variable which contains the value of the angle
     * @return the index of the given angle (in radians)
     */
    public static double sampleIndex(double angle) {
        return angle * SAMPLES_PER_RADIAN;
    }

    /**
     * Method which returns the extent of the DEM
     * 
     * @return the extent of the DEM
     */
    public Interval2D extent();

    /**
     * Method which returns the elevation sample at the given index
     * 
     * @param x
     * @param y
     * @return the elevation sample in meters
     */
    public double elevationSample(int x, int y);

    default DiscreteElevationModel union(DiscreteElevationModel that) {
        Preconditions
                .checkArgument(this.extent().isUnionableWith(that.extent()));
        return new CompositeDiscreteElevationModel(this, that);
    }
}