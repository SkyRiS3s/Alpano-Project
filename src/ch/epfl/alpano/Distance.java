package ch.epfl.alpano;

/**
 * Interface Distance: Provides methods which allow to convert distances in
 * radians into meters (and vice vera)
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public interface Distance {

    /*
     * Constant which indicates the length of Earth's radius (in meters)
     */
    public static final double EARTH_RADIUS = 6371000;

    /**
     * Method which converts distances (in meters) at the surface of the Earth
     * into radians
     * 
     * @param distanceInMeters
     *            double variable indicating the distance at the surface of the
     *            Earth
     * @return angle in radians
     */
    public static double toRadians(double distanceInMeters) {
        return distanceInMeters / EARTH_RADIUS;
    }

    /**
     * Method which converts radian into meters
     * 
     * @param distanceInRadians
     *            double variable which indicates the value of the angle in
     *            radians
     * @return the corresponding distance in meters
     */
    public static double toMeters(double distanceInRadians) {
        return distanceInRadians * EARTH_RADIUS;
    }

}
