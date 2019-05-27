package ch.epfl.alpano.dem;

import static java.util.Objects.requireNonNull;
import ch.epfl.alpano.Interval2D;

/**
 * Class CompositeDiscreteElevationModel: represents the union of two discrete
 * DEM
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public final class CompositeDiscreteElevationModel
        implements DiscreteElevationModel {

    /*
     * First DEM, which is used to create the composite DEM
     */
    private final DiscreteElevationModel dem1;

    /*
     * Second DEM which is used to create the composite DEM
     */
    private final DiscreteElevationModel dem2;

    /*
     * The union of the extent of the first two dem
     */
    private final Interval2D unionExtent;

    /**
     * Constructor of the class
     * 
     * @param dem1
     *            first DEM used to create the composite DEM
     * @param dem2
     *            second DEM used to create the compotsite DEM
     */
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1,
            DiscreteElevationModel dem2) {
        this.dem1 = requireNonNull(dem1);
        this.dem2 = requireNonNull(dem2);
        this.unionExtent = dem1.extent().union(dem2.extent());
    }

    /**
     * Method which returns the union of the extent of the two discrete DEM used
     * to construct the composite DEM
     */
    @Override
    public Interval2D extent() {
        return this.unionExtent;
    }

    /**
     * Retuens the elevation sample
     */
    @Override
    public double elevationSample(int x, int y) {
        if (dem1.extent().contains(x, y)) {
            return dem1.elevationSample(x, y);
        } else if (dem2.extent().contains(x, y)) {
            return dem2.elevationSample(x, y);
        }
        throw new IllegalArgumentException();
    }

    /**
     * Method which closes the DEM
     */
    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }
}
