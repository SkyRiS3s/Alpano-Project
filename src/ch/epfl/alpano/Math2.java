package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

/**
 * Interface Math 2: Contains all the different mathematical methods usefull for
 * the project. It plays a similar role to the class java.lang.Math
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 */
public interface Math2 {

    /*
     * PI2 is a constant which is approximately equivalent to 2*PI
     */
    public static final double PI2 = 2 * Math.PI;

    /**
     * Method which computes the square of a given number
     * 
     * @param x
     *            is the number (double variable) whose square is computed
     * @return the square of the given double
     */
    public static double sq(double x) {
        return x * x;
    }

    /**
     * Method which returns the largest integer less than or equal to "x mod y"
     * 
     * @param x
     *            double variable
     * @param y
     *            double variable
     * @return double variable which returns floor(x mod y)
     */
    public static double floorMod(double x, double y) {
        return x - y * (Math.floor(x / y));
    }

    /**
     * Method which computes the haversin of x, knowing that its formula is :
     * haversin(x) = [sin(x/2)]^2
     * 
     * @param x
     *            double variable whose haversin this method computes
     * @return the haversin of x
     */
    public static double haversin(double x) {
        return sq(Math.sin(x / 2));
    }

    /**
     * Mathod which computes the angular distance between two angles
     * 
     * @param a1:
     *            double variable indicating the value of the first angle (in
     *            radians)
     * @param a2:
     *            double variable indicating the value of the second angle (in
     *            radians)
     * @return The angular distance, knowing that its formula is :
     *         floor(a2-a1+pi, 2*pi) - pi
     */
    public static double angularDistance(double a1, double a2) {
        return floorMod((a2 - a1 + Math.PI), PI2) - Math.PI;
    }

    /**
     * Linear interpolation to estimate the value taken by a continuous function
     * between two given points.
     * 
     * @param y0
     *            double variable which corresponds to f(x0), where x0 = 0
     * @param y1
     *            double variable which corresponds to f(x1), where x1 = 1
     * @param x
     *            double variable which serves as input to compute the value the
     *            the function (obtained by linear interpolation) will take
     * @return
     */
    public static double lerp(double y0, double y1, double x) {
        double m = y1 - y0;
        double b = y0;
        return m * x + b; // i.e, linear function
    }

    /**
     * Bilinear interpolation which allows to compute the value of a function at
     * a given point, from its two closest neighbours in each direction
     * 
     * @param z00
     *            double variable which represents f(x0,y0), where x0=0 and y0=0
     * @param z10
     *            double variable which represents f(x1,y0), where x1=1 and y0=0
     * @param z01
     *            double variable which represents f(x0,y1), where x0=0 and y1=1
     * @param z11
     *            double variable which represents f(x1,y1), where x1=1 and y1=1
     * @param x
     *            double variable which is the first input of our two variable
     *            function
     * @param y
     *            double variable which is the second input of our two variable
     *            function
     * @return f(x,y)
     */
    public static double bilerp(double z00, double z10, double z01, double z11,
            double x, double y) {
        double a = lerp(z00, z10, x);
        double b = lerp(z01, z11, x);
        return lerp(a, b, y);

    }

    /**
     * Method which finds out the inferior boundary of a subinterval of size dX
     * which contains a zero
     * 
     * @param f
     *            is the function
     * @param minX
     *            double variable, left boundary of the interval
     * @param maxX
     *            double variable, right boundary of the interval
     * @param dX
     *            size of the subintervals
     * @return the inferior boundary of a subinterval of size dX which contains
     *         a zero (a double variable)
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f,
            double minX, double maxX, double dX) {
        Preconditions.checkArgument(maxX >= minX && dX > 0);
        double infSub = minX;
        double nextSub = minX + dX;
        while (f.applyAsDouble(infSub) * f.applyAsDouble(nextSub) > 0
                && nextSub <= maxX - dX) {
            infSub += dX;
            nextSub += dX;
        }
        return f.applyAsDouble(infSub) * f.applyAsDouble(nextSub) > 0
                ? Double.POSITIVE_INFINITY : infSub;
    }

    /**
     * Method which finds the lowest boundary of a subinterval of size equal or
     * inferior to epsilon and between x1 and x2
     * 
     * @param f
     *            function
     * @param x1
     *            first boundary of the interval
     * @param x2
     *            second boundary of the interval
     * @param epsilon
     *            error
     * @return lowest boundary of a subinterval of size equal or inferior to
     *         epsilon and between x1 and x2
     * @throws IllegalArgumentException
     *             if x1 and x2 have the same sign
     */
    public static double improveRoot(DoubleUnaryOperator f, double x1,
            double x2, double epsilon) {
        double a = x1;
        double b = x2;
        double c;
        
        if (f.applyAsDouble(a) == 0) {
            return a;
        } else if (f.applyAsDouble(b) == 0) {
            return b;
        }
        
        Preconditions.checkArgument(
                f.applyAsDouble(a) * f.applyAsDouble(b) < 0 && epsilon > 0);
        while (Math.abs(b - a) > epsilon) {
            c = (a + b) / 2;
            if (f.applyAsDouble(c) == 0) {
                return c;
            } else if (f.applyAsDouble(c) * f.applyAsDouble(a) > 0) {
                a = c;
            } else if (f.applyAsDouble(c) * f.applyAsDouble(b) > 0) {
                b = c;
            }
        }
        return a < b ? a : b;
    }
}
