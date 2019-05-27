package ch.epfl.alpano;

import java.util.Objects;

/**
 * Class Interval2D: creates a two-dimensional interval of integers.
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public class Interval2D {

    /*
     * First 1D interval of the Cartesian product
     */
    private final Interval1D iX;

    /*
     * Second 1D interval of the Cartesian product
     */
    private final Interval1D iY;

    /**
     * Constructs a two-dimensional interval from the tow given one-dimensional
     * intervals
     * 
     * @param iX
     * @param iY
     */
    public Interval2D(Interval1D iX, Interval1D iY) {
        if (iX == null || iY == null) {
            throw new NullPointerException();
        }
        this.iX = iX;
        this.iY = iY;
    }

    /**
     * Method which allows anyone to get the first 1D interval
     * 
     * @return the value of the first interval
     */
    public Interval1D iX() {
        return iX;
    }

    /**
     * Method which allows anyone to get the second 1D interval
     * 
     * @return the value of the second interval
     */
    public Interval1D iY() {
        return iY;
    }

    /**
     * Method which verifies if a given value is contained in the 2D interval
     * 
     * @param x
     * @param y
     * @return boolean variable confirming (or not) that (x,y) is in the
     *         interval
     */
    public boolean contains(int x, int y) {
        return (iX.contains(x) && iY.contains(y));
    }

    /**
     * Method that gives the size of the 2D interval
     * 
     * @return the size of the 2D interval
     */
    public int size() {
        return iX.size() * iY.size();
    }

    /**
     * Method that gives the size of the intersection of the intervals this and
     * that
     * 
     * @param that
     * @return size of the intersection of the intervals this and that
     */
    public int sizeOfIntersectionWith(Interval2D that) {
        return iX().sizeOfIntersectionWith(that.iX())
                * iY().sizeOfIntersectionWith(that.iY());
    }

    /**
     * Method which returns the surrounding union of the receptor and the
     * argument
     * 
     * @param that
     * @return the surrounding union
     */
    public Interval2D boundingUnion(Interval2D that) {
        return new Interval2D(this.iX.boundingUnion(that.iX),
                this.iY.boundingUnion(that.iY));
    }

    /**
     * Method which verifies if two intervals are unionable
     * 
     * @param that
     *            interval
     * @return boolean variable which confirms (or not) if the two intervals are
     *         unionable or not.
     */
    public boolean isUnionableWith(Interval2D that) {
        Interval2D I = this.boundingUnion(that);
        int area = size() + that.size() - sizeOfIntersectionWith(that);
        return this.iX().isUnionableWith(that.iX())
                && this.iY().isUnionableWith(that.iY()) && area == I.size();
    }

    /**
     * Method which returns the union of two intervals
     * 
     * @param that
     *            interval
     * @return the union of two intervals (this and that)
     */
    public Interval2D union(Interval2D that) {
        Preconditions.checkArgument(this.isUnionableWith(that));
        return this.boundingUnion(that);
    }

    /**
     * Method which verifies if two objects are the same
     * 
     * @param that0:
     *            object
     * @return boolean variable which says if the two given objects are the same
     */
    @Override
    public boolean equals(Object thatO) {
        return (thatO instanceof Interval2D)
                && this.iX().equals(((Interval2D) thatO).iX())
                && this.iY().equals(((Interval2D) thatO).iY());
    }

    /**
     * Method which is necessary to be redefined, since we redefined the method
     * "equals" of the class "Object"
     */
    @Override
    public int hashCode() {
        return Objects.hash(iX(), iY());
    }

    /**
     * Method which returns a string representing the interval
     * 
     * @return string
     */
    @Override
    public String toString() {
        return iX().toString() + " x " + iY().toString();
    }
}
