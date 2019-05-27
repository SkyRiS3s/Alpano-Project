package ch.epfl.alpano;

import java.util.Objects;

/**
 * Class Interval1D: represents a one-dimensional interval of integers
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public final class Interval1D {

    /*
     * Integer representing the first boundary of the interval
     */
    private final int includedFrom;

    /*
     * Interval representing the second boundary of the interval
     */
    private final int includedTo;

    /**
     * Constructor a one-dimensional interval with the given boundaries
     * 
     * @param includedFrom
     * @param includedTo
     */
    public Interval1D(int includedFrom, int includedTo) {
        Preconditions.checkArgument(includedTo >= includedFrom);
        this.includedTo = includedTo;
        this.includedFrom = includedFrom;
    }

    /**
     * Constructor which allows us to copy an interval
     * 
     * @param i
     *            interval
     */
    public Interval1D(Interval1D i) {
        this.includedTo = i.includedTo;
        this.includedFrom = i.includedFrom;
    }

    /**
     * Method which allows anyone to get the value of "includedFrom"
     * 
     * @return the value of "includedFrom"
     */
    public int includedFrom() {
        return includedFrom;
    }

    /**
     * Method which allows anyone to get the value of "included to"
     * 
     * @return the value of "includedTo"
     */
    public int includedTo() {
        return includedTo;
    }

    /**
     * Method which verifies if a given value is contained in the interval
     * 
     * @param v
     *            integer that is verified
     * @return boolean variable confirming (or not) that the value is within the
     *         interval
     */
    public boolean contains(int v) {
        return v <= includedTo && v >= includedFrom;
    }

    /**
     * Method which returns the size of the interval, i.e the number of values
     * it contains
     * 
     * @return the interval's size
     */
    public int size() {
        return includedTo - includedFrom + 1;
    }

    /**
     * Method which returns the size of the intersection of two intervals
     * 
     * @param that
     *            interval (in 1D)
     * @return the size of the intersection of two intervals
     */
    public int sizeOfIntersectionWith(Interval1D that) {
        if (includedTo >= that.includedFrom
                && includedFrom <= that.includedFrom) {
            if (that.includedTo <= includedTo) {
                return that.includedTo - that.includedFrom + 1;
            }
            return includedTo - that.includedFrom + 1;
        } else if (includedFrom > that.includedFrom
                && includedFrom <= that.includedTo) {
            if (includedTo <= that.includedTo) {
                return includedTo - includedFrom + 1;
            }
            return that.includedTo - includedFrom + 1;
        } else {
            return 0;
        }
        
        
//        if(includedFrom <= that.includedTo && includedFrom > that.includedFrom || includedTo>= that.includedFrom  && includedFrom <= that.includedFrom) {
//            return Math.max(includedTo, that.includedTo) - Math.min(includedFrom, that.includedFrom) + 1;
//        } else {
//            return 0;
//        }
        
    }
    

    /**
     * Method which returns the surrounding union of the receptor and the
     * argument
     * 
     * @param that
     *            interval
     * @return the surrounding union
     */
    public Interval1D boundingUnion(Interval1D that) {
        return new Interval1D(Math.min(this.includedFrom, that.includedFrom),
                Math.max(this.includedTo, that.includedTo));
    }

    /**
     * Method which verifies if two intervals are unionable
     * 
     * @param that
     *            interval
     * @return boolean variable which confirms (or not) if the two intervals are
     *         unionable or not.
     */
    public boolean isUnionableWith(Interval1D that) {
        return this.boundingUnion(that).size() == this.size() + that.size()
                - this.sizeOfIntersectionWith(that);
    }

    /**
     * Method which returns the union of two intervals
     * 
     * @param that:
     *            interval
     * @return the union of two intervals (this and that)
     */
    public Interval1D union(Interval1D that) {
        Preconditions.checkArgument(this.isUnionableWith(that));
        return this.boundingUnion(that);
    }

    /**
     * Method which verifies if two objects are the same
     * 
     * @param that0
     *            object
     * @return boolean variable which says if the two given objects are the same
     */
    @Override
    public boolean equals(Object thatO) {
        return (thatO instanceof Interval1D)
                && ((Interval1D) thatO).includedFrom() == this.includedFrom()
                && ((Interval1D) thatO).includedTo() == this.includedTo();
    }

    /**
     * Method which is necessary to be redefined, since we redefined the method
     * "equals" of the class "Object"
     */
    @Override
    public int hashCode() {
        return Objects.hash(includedFrom(), includedTo());
    }

    /**
     * Method which returns a string representing the interval
     * 
     * @return string
     */
    @Override
    public String toString() {
        return "[" + this.includedFrom() + ".." + this.includedTo() + "]";
    }
}