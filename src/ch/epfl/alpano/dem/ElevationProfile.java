package ch.epfl.alpano.dem;

import static java.lang.Math.sin;
import java.util.Objects;
import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Preconditions;
import static java.lang.Math.cos;
import static java.lang.Math.asin;

/**
 * Class ElevationProfile: resprsents an altimetric profile
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 * 
 */
public final class ElevationProfile {

    /*
     * Continuous elevation model on which the elevation profile is based on
     */
    private final ContinuousElevationModel elevationModel;

    /*
     * GeoPoint which represents the starting point of the elevation profile
     */
    private final GeoPoint origin;

    /*
     * double variable representing the direction of the circle (in the form of
     * an azimuth angle)
     */
    private final double azimuth;

    /*
     * double variable which represents the length of the circle
     */
    private final double length;

    /*
     * Integer which tells how many points who are separated by 4096 meters
     * there are For example, if the length is 100'000 meters, then there are 26
     * different points Basically, it tells how many elements there are in
     * lambda[] and phi[] (see below)
     */
    private final int idMax;

    /*
     * Table of double variables containing all the different longitudes of the
     * different points contained in x[]
     */
    private double[] lambda;

    /*
     * Table of double variables containing all the different latitudes of the
     * different points contained in x[]
     */
    private double[] phi;

    /**
     * Constructor of the class
     * 
     * @param elevationModel
     * @param origin
     * @param azimuth
     * @param length
     */
    public ElevationProfile(ContinuousElevationModel elevationModel,
            GeoPoint origin, double azimuth, double length) {

        this.elevationModel = Objects.requireNonNull(elevationModel);
        this.origin = Objects.requireNonNull(origin);

        Preconditions.checkArgument(Azimuth.isCanonical(azimuth));
        this.azimuth = azimuth;
        Preconditions.checkArgument(length > 0);
        this.length = length;

        this.idMax = nbOfPositions();
        this.phi = phiTable();
        this.lambda = lambdaTable();

    }

    /**
     * Method which computes the number of positions with respect to the length
     * i.e it determines the length of lambda[] and phi[] For example, if the
     * length is 100 km, the number of positions is 26 (this means that we will
     * have 26 different longitudes and latitudes respectively)
     * 
     * @return integer variable representing the length of phi[] and lambda[]
     */
    private int nbOfPositions() {
        int idMax = (int) Math.ceil((Math.scalb(this.length, -12) + 1));
        return idMax;
    }

    /**
     * Method which creates a table with all the latitudes of all the different
     * positions
     * 
     * @return table of double variables containing all the latitudes
     */
    private double[] phiTable() {
        double phi[] = new double[this.idMax];
        double alpha = Azimuth.toMath(this.azimuth);

        for (int i = 0; i < this.idMax; ++i) {
            double x = Distance.toRadians(Math.scalb(i, 12));
            phi[i] = asin(sin(this.origin.latitude()) * cos(x)
                    + cos(this.origin.latitude()) * sin(x) * cos(alpha));

        }
        return phi;
    }

    /**
     * Method which creates a table with all the longitudes of all the different
     * positions
     * 
     * @return table of double variables containing all the longitudes
     */
    private double[] lambdaTable() {
        double lambda[] = new double[this.idMax];
        double alpha = Azimuth.toMath(this.azimuth);

        for (int i = 0; i < idMax; i++) {
            double x = Distance.toRadians(Math.scalb(i, 12));
            lambda[i] = (origin.longitude()
                    - Math.asin(
                            (Math.sin(alpha) * Math.sin(x)) / Math.cos(phi[i]))
                    + Math.PI) % Math2.PI2 - Math.PI;
        }

        return lambda;
    }

    /**
     * Method which computes the longitude and the latitude of the given
     * position of the profile
     * 
     * @param x
     *            position of the profile
     * @return GeopPoint of x (i.e the position at x)
     */
    public GeoPoint positionAt(double x) {
        Preconditions.checkArgument(x >= 0 && x <= this.length);
        /*
         * integer variable representing the index of the lowest position (for
         * which we have already computed the longitude and the latitude)
         * closest to x For example: if x=10240, then i = 2, since (4096*2) < x
         * < (4096*3);
         */
        double x2 = Math.scalb(x, -12);
        int i = (int) (x2);

        if (x2 == i) {
            return new GeoPoint(lambda[i], phi[i]);
        } else {
            double l = Math2.lerp(lambda[i], lambda[i + 1], x2 - i);
            double p = Math2.lerp(phi[i], phi[i + 1], x2 - i);
            return new GeoPoint(l, p);
        }
    }

    /**
     * Method which returns the elevation of the profile at the given position
     * of the profile
     * 
     * @param x
     *            is the position of the profile
     * @return the elevation of the profile at the given position of the profile
     */
    public double elevationAt(double x) {
        Preconditions.checkArgument(x >= 0 && x <= this.length);
        GeoPoint point = positionAt(x);
        return elevationModel.elevationAt(point);
    }

    /**
     * Method which returns the slope of the profile at the given position of
     * the profile
     * 
     * @param x
     *            is the position of the profile
     * @return the slope of the profile at the given position of the profile
     */
    public double slopeAt(double x) {
        Preconditions.checkArgument(x >= 0 && x <= this.length);
        GeoPoint point = positionAt(x);
        return elevationModel.slopeAt(point);
    }
}
