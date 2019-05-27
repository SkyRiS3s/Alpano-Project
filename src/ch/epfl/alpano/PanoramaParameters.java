package ch.epfl.alpano;

/**
 * Class PanoramaParameters: represents the parameters required to draw a
 * panorama
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 * 
 */
public final class PanoramaParameters {

    /**
     * Fields:
     */
    private final GeoPoint observerPosition;
    private final int observerElevation;
    private final double centerAzimuth;
    private final double horizontalFieldOfView;
    private final int maxDistance;
    private final int width;
    private final int height;
    private final double verticalFieldOfView;

    /**
     * Constructor of the class
     * 
     * @param observerPosition
     * @param observerElevation
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDistance
     * @param width
     * @param height
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        if (observerPosition == null) {
            throw new NullPointerException();
        }
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        Preconditions.checkArgument(0 < horizontalFieldOfView
                && horizontalFieldOfView <= Math2.PI2);
        Preconditions.checkArgument(maxDistance > 0 && width > 0 && height > 0);
        this.observerPosition = observerPosition;
        this.observerElevation = observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        this.verticalFieldOfView = this.horizontalFieldOfView
                * (double) (height - 1) / (width - 1);
    }

    /**
     * Method which returns the canonical azimuth angle corresponding to a given
     * index
     * 
     * @param x:
     *            index of the pixel horizontal x
     * @return: azimuth anfge in radians
     */
    public double azimuthForX(double x) {
        Preconditions.checkArgument(x >= 0 && x <= width - 1);
        return Azimuth.canonicalize(x * horizontalFieldOfView / (width - 1)
                - horizontalFieldOfView / 2 + centerAzimuth);
    }

    /**
     * Method which returns the index of the horizontal pixel
     * 
     * @param a
     * @return
     */
    public double xForAzimuth(double a) {
        Preconditions
                .checkArgument(centerAzimuth - horizontalFieldOfView / 2 <= a
                        && centerAzimuth + horizontalFieldOfView / 2 >= a);
        return (a - centerAzimuth + horizontalFieldOfView / 2) * (width - 1)
                / horizontalFieldOfView;
    }

    /**
     * Method which returns the elevation for a given y
     * 
     * @param y:
     *            double variable corresponding to the vertical pixel
     * @return the elevation for y
     */
    public double altitudeForY(double y) {
        Preconditions.checkArgument(y >= 0 && y <= height - 1);
        return verticalFieldOfView() / 2.0
                - y * horizontalFieldOfView / (width - 1);
    }

    /**
     * Method which returns the index of the vertical pixel corresponding the a
     * given elevation
     * 
     * @param a:
     *            double variable representing the elevation
     * @return the index if the vertical pixel corresponding to the elevation a
     */
    public double yForAltitude(double a) {
        Preconditions.checkArgument(a >= -verticalFieldOfView() / 2
                && a <= verticalFieldOfView() / 2);
        return (verticalFieldOfView() / 2.0 - a) * (width - 1)
                / horizontalFieldOfView;
    }

    /**
     * Method which verifies if the index is valid or not
     * 
     * @param x:
     *            x index
     * @param y:
     *            y index
     * @return boolean variables which says if the given indices were valid or
     *         not.
     */
    protected boolean isValidSampleIndex(int x, int y) {
        Preconditions.checkArgument(
                x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1);
        if (x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1) {
            return true;
        }
        return false;
    }

    /**
     * Method which returns the linear index of the pixel
     * 
     * @param x:
     *            x index
     * @param y:
     *            y index
     * @return the linear index of the pixel
     */
    protected int linearSampleIndex(int x, int y) {
        assert isValidSampleIndex(x, y);
        return y * width + x;
    }

    /////////////////////////////////////////////////////

    /*
     * The following methods are all getters
     */

    public GeoPoint observerPosition() {
        return observerPosition;
    }

    public int observerElevation() {
        return observerElevation;
    }

    public double centerAzimuth() {
        return centerAzimuth;
    }

    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }

    public int maxDistance() {
        return maxDistance;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double verticalFieldOfView() {
        return this.verticalFieldOfView;
    }
}
