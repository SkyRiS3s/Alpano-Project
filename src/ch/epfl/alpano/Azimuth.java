package ch.epfl.alpano;

/**
 * Interface Azimuth: Contains the methods allowing to manipulate the values
 * representing the azimuts expressed in radians
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public interface Azimuth {

    /**
     * Method which verifies if the azimuth angle is in between 0 and 2*Pi
     * (where 2Pi is not included)
     * 
     * @param azimuth
     *            angle in rad
     * @return boolean variable which tells if the angle is canonical or not
     */
    public static boolean isCanonical(double azimuth) {
        return azimuth >= 0 && azimuth < Math2.PI2;
    }

    /**
     * Method which verifies if the azimuth angle is in between 0 and 2*Pi
     * (where 2Pi is not included)
     * 
     * @param azimuth
     *            angle in rad
     * @return boolean variable which tells if the angle is canonical or not
     */
    public static double canonicalize(double azimuth) {
        if (!isCanonical(azimuth)) {
            while (azimuth < 0) {
                azimuth += Math2.PI2;
            }
            while (azimuth >= Math2.PI2) {
                azimuth -= Math2.PI2;
            }
        }
        return azimuth;
    }

    /**
     * Method which verifies if the azimuth angle is in between 0 and 2*Pi
     * (where 2Pi is not included)
     * 
     * @param azimuth
     *            angle in rad
     * @return Mathematical angle in radians
     */
    public static double toMath(double azimuth)
            throws IllegalArgumentException {
        Preconditions.checkArgument(isCanonical(azimuth));
        return canonicalize(-azimuth);
    }

    /**
     * Method which transforms a mathematical angle into an azimuth angle
     * 
     * @param mathematical
     *            angle
     * @return angle azimuth
     * @throws IllegalArgumentException
     *             if the angle is not between 0 and 2PI (with 2PI not included)
     */
    public static double fromMath(double angle)
            throws IllegalArgumentException {
        Preconditions.checkArgument(isCanonical(angle));
        return canonicalize(-angle);
    }

    /**
     * Method which allow us to tell whether the angle is situated on the north,
     * south, south-east, etc.
     * 
     * @param azimuth
     *            angle in rad
     * @param n
     *            north
     * @param e
     *            east
     * @param s
     *            south
     * @param w
     *            west
     * @return String variable roughly showing the value of the azimuth angle in
     *         terms of the cardinal points
     */
    public static String toOctantString(double azimuth, String n, String e,
            String s, String w) {
        Preconditions.checkArgument(isCanonical(azimuth));
        if ((azimuth >= 15 * Math.PI / 8 && azimuth - Math2.PI2 <= Math.PI / 8)
                || (azimuth + Math2.PI2 >= 15 * Math.PI / 8
                        && azimuth <= Math.PI / 8)) {
            return n;
        } else if (azimuth > Math.PI / 8 && azimuth < 3 * Math.PI / 8) {
            return n + e;
        } else if (azimuth >= 3 * Math.PI / 8 && azimuth <= 5 * Math.PI / 8) {
            return e;
        } else if (azimuth > 5 * Math.PI / 8 && azimuth < 7 * Math.PI / 8) {
            return s + e;
        } else if (azimuth >= 7 * Math.PI / 8 && azimuth <= 9 * Math.PI / 8) {
            return s;
        } else if (azimuth > 9 * Math.PI / 8 && azimuth < 11 * Math.PI / 8) {
            return s + w;
        } else if (azimuth >= 11 * Math.PI / 8 && azimuth <= 13 * Math.PI / 8) {
            return w;
        } else {
            return n + w;
        }
    }
}