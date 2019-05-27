package ch.epfl.alpano;

import java.util.Arrays;

/**
 * Class Panorama: represents a panorama
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 * 
 */
public final class Panorama {

    /*
     * Fields:
     */
    private final PanoramaParameters parameters;
    private final float[] distance;
    private final float[] longitude;
    private final float[] latitude;
    private final float[] elevation;
    private final float[] slope;

    /**
     * Constructor of the class
     * 
     * @param p
     * @param d
     * @param longitude
     * @param latitude
     * @param elevation
     * @param slope
     */
    private Panorama(PanoramaParameters p, float[] d, float[] longitude,
            float[] latitude, float[] elevation, float[] slope) {
        parameters = p;
        distance = d;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.slope = slope;
    }

    /**
     * Method which checks if the given sample indicies are valid or not
     * 
     * @param x
     *            : sample index
     * @param y
     *            : sample index
     * @throws IndexOutOfBoundsException
     *             if the sample indicies are not valid
     */
    private void check(int x, int y) {
        if (!parameters.isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Getter: allows anyone to get the parameters of the panorama
     * 
     * @return PanoramaParameters parameters
     */
    public PanoramaParameters parameters() {
        return parameters;
    }

    /**
     * Method which returns the distance at the given sample indicies
     * 
     * @param x
     * @param y
     * @return
     */
    public float distanceAt(int x, int y) {
        check(x, y);
        return distance[parameters.linearSampleIndex(x, y)];
    }

    /**
     * Method which returns the longitude at the given sample indicies
     * 
     * @param x
     * @param y
     * @return
     */
    public float longitudeAt(int x, int y) {
        check(x, y);
        return longitude[parameters.linearSampleIndex(x, y)];
    }

    /**
     * Method which returns the latitude at the given sample indicies
     * 
     * @param x
     * @param y
     * @return
     */
    public float latitudeAt(int x, int y) {
        check(x, y);
        return latitude[parameters.linearSampleIndex(x, y)];
    }

    /**
     * Method which returns the elevation at the given sample indicies
     * 
     * @param x
     * @param y
     * @return
     */
    public float elevationAt(int x, int y) {
        check(x, y);
        return elevation[parameters.linearSampleIndex(x, y)];
    }

    /**
     * Method which returns the slope at the given sample indicies
     * 
     * @param x
     * @param y
     * @return
     */
    public float slopeAt(int x, int y) {
        check(x, y);
        return slope[parameters.linearSampleIndex(x, y)];
    }

    /**
     * In some cases, it is more usefull to have a default value instead of
     * having an exception the indicies are out of bounds That's what this
     * method is for
     * 
     * @param x
     * @param y
     * @param d:
     *            float variable representing the distance at the given indicies
     * @return
     */
    public float distanceAt(int x, int y, float d) {
        return parameters.isValidSampleIndex(x, y)
                ? distance[parameters.linearSampleIndex(x, y)] : d;
    }

    /**
     * Class Builder: represents the builder of a panorama
     * 
     * @author Saoud Akram (273661)
     * @author Karim Kabbani (261340)
     */
    public static final class Builder {

        /*
         * Fields:
         */
        private PanoramaParameters parameters;
        private float[] distance;
        private float[] longitude;
        private float[] latitude;
        private float[] elevation;
        private float[] slope;
        private boolean calledBuild;

        /**
         * Constructs a builder with the given parameters
         * 
         * @param parameters
         */
        public Builder(PanoramaParameters parameters) {
            if (parameters == null) {
                throw new NullPointerException();
            }
            this.parameters = parameters;
            longitude = new float[parameters.width() * parameters.height()];
            Arrays.fill(longitude, 0);
            latitude = new float[parameters.width() * parameters.height()];
            Arrays.fill(latitude, 0);
            elevation = new float[parameters.width() * parameters.height()];
            Arrays.fill(elevation, 0);
            slope = new float[parameters.width() * parameters.height()];
            Arrays.fill(slope, 0);
            distance = new float[parameters.width() * parameters.height()];
            Arrays.fill(distance, Float.POSITIVE_INFINITY);
        }

        private void check() {
            if (calledBuild) {
                throw new IllegalStateException();
            }
        }

        private void check(int x, int y) {
            check();
            try {
                parameters.isValidSampleIndex(x, y);
            } catch (IllegalArgumentException wrongException) {
                throw new IndexOutOfBoundsException();
            }
        }

        //////////////////////////////////////////////////////////

        /*
         * The Following methods are all setters:
         */

        public Builder setDistanceAt(int x, int y, float distance) {
            check(x, y);
            this.distance[parameters.linearSampleIndex(x, y)] = distance;
            return this;
        }

        public Builder setLongitudeAt(int x, int y, float longitude) {
            check(x, y);
            this.longitude[parameters.linearSampleIndex(x, y)] = longitude;
            return this;
        }

        public Builder setLatitudeAt(int x, int y, float latitude) {
            check(x, y);
            this.latitude[parameters.linearSampleIndex(x, y)] = latitude;
            return this;
        }

        public Builder setElevationAt(int x, int y, float elevation) {
            check(x, y);
            this.elevation[parameters.linearSampleIndex(x, y)] = elevation;
            return this;
        }

        public Builder setSlopeAt(int x, int y, float slope) {
            check(x, y);
            this.slope[parameters.linearSampleIndex(x, y)] = slope;
            return this;
        }

        /**
         * Method which allows us to create a panorama from the builder
         * 
         * @return Panorma which was constructed with the Builder
         */
        public Panorama build() {
            check();
            calledBuild = true;
            return new Panorama(parameters, distance, longitude, latitude,
                    elevation, slope);
        }
    }
}
