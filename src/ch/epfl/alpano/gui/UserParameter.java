package ch.epfl.alpano.gui;

/**
 * Enumeration which enumerates the nine user parameters
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */

public enum UserParameter {
    OBSERVER_LONGITUDE(60000, 120000),
    OBSERVER_LATITUDE(450000,480000),
    OBSERVER_ELEVATION(300,10000),
    CENTER_AZIMUTH(0, 359),
    HORIZONTAL_FIELD_OF_VIEW(1, 360),
    MAX_DISTANCE(10, 600),
    WIDTH(30, 16000),
    HEIGHT(10, 4000),
    SUPER_SAMPLING_EXPONENT(0,2);
    
    // Fields: Each UserParameter is bounded below and above by a min and a max
    // respectively
    private final int min;
    private final int max;
    
    /**
     * Constructor of a UserParameter
     * 
     * @param min,
     *            inferior bound of the parameter
     * @param max,
     *            superior bound of the parameter
     */
    private UserParameter(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    /**
     * Public method which verfies if the value is not greater than the max (or,
     * respectively, less than the min). If it is the case, then it returns the
     * max (respectively, the min)
     * 
     * @param value:
     *            interger that will be sanitized
     * @return the saniized value
     */
    public int sanitize(int value) {
        if (value < this.min) {
            return this.min;
        } else if(value > this.max) {
            return this.max;
        } else {
            return value;
        }
    }
}
