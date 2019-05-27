package ch.epfl.alpano.gui;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.Preconditions;

/**
 * Functional Interface representing a channel painter
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */

@FunctionalInterface
public interface ChannelPainter {

    /**
     * Abstract method which returns the value of the channel painter at a given
     * point
     * 
     * @param x
     *            coordinate
     * @param y
     *            coordinate
     * @return value of the channel painter
     */
    public abstract float valueAt(int x, int y);

    /**
     * Static method which, given a Panorama, returns a channel painter whose
     * value at a point is the difference in distance of the furthest neighbour
     * and the point.
     * 
     * @param aPanorama
     *            panorama
     * @return Channel painter whose value at a given point is the difference in
     *         distance between the furthest neighbour and the poitn
     */
    public static ChannelPainter maxDistanceToNeighbors(Panorama aPanorama) {
        Objects.requireNonNull(aPanorama);
        return (x, y) -> {
            float dPixel = aPanorama.distanceAt(x, y);
            float d1;
            float d2;
            float d3;
            float d4;
            if (x == 0) {
                d2 = 0;
            } else {
                d2 = aPanorama.distanceAt(x - 1, y, 0);
            }
            if (x == aPanorama.parameters().width() - 1) {
                d1 = 0;
            } else {
                d1 = aPanorama.distanceAt(x + 1, y, 0);
            }
            if (y == 0) {
                d4 = 0;
            } else {
                d4 = aPanorama.distanceAt(x, y - 1, 0);
            }
            if (y == aPanorama.parameters().height() - 1) {
                d3 = 0;
            } else {
                d3 = aPanorama.distanceAt(x, y + 1, 0);
            }

            return Math.max(Math.max(Math.max(d1, d2), d3), d4) - dPixel;
        };
    }

    /**
     * Method which allows to add a constant to the value at a given point of a
     * channel painter
     * 
     * @param term
     *            constant
     * @return channel painter whose value is the sum of the constant and the
     *         value of the channel painter at the given point
     */
    default ChannelPainter add(float term) {
        return (x, y) -> {
            return valueAt(x, y) + term;
        };
    }

    /**
     * Method which allows to substract a constant to the value at a given point
     * of a channel painter
     * 
     * @param term
     *            constant
     * @return channel painter whose value is the difference between the value
     *         at a given point and the constant
     */
    default ChannelPainter sub(float term) {
        return (x, y) -> {
            return valueAt(x, y) - term;
        };
    }

    /**
     * Method which allows to multiply a constant to the value at a given point
     * of a channel painter
     * 
     * @param term
     *            constant term
     * @return channel painter whose value is the product between constant and
     *         the value of the channel painter at the given point
     */
    default ChannelPainter mul(float term) {
        return (x, y) -> {
            return valueAt(x, y) * term;
        };
    }

    /**
     * Method which allows to divide the value at a given point of a channel
     * painter by a constant
     * 
     * @param term
     *            constant
     * @return channel painter whose value is the quotient of tthe value of the
     *         channel painter at the given point and the constant term
     */
    default ChannelPainter div(float term) {
        Preconditions.checkArgument(term != 0);
        return (x, y) -> {
            return valueAt(x, y) / term;
        };
    }

    /**
     * Method which allows us to apply a mapping to the value at a given point
     * of a channel painter
     * 
     * @param f
     *            mapping (which basically represents a function)
     * @return A channel painter whose value at a given point is the image of
     *         the mapping given by f ( valueAt(x,y) )
     */
    default ChannelPainter map(DoubleUnaryOperator f) {
        return (x, y) -> {
            return (float) f.applyAsDouble(valueAt(x, y));
        };
    }

    /**
     * Useful function to define a channel
     * 
     * @return A channel painter whose value at a given point is equal to
     *         1-valueAt(x,y)
     */
    default ChannelPainter inverted() {
        return (x, y) -> {
            return 1f - valueAt(x, y);
        };
    }

    /**
     * Useful function to define a channel
     * 
     * @return A channel painter whose value at a given point is equal to
     *         valueAt(x,y) mod 1
     */
    default ChannelPainter cylce() {
        return (x, y) -> {
            return valueAt(x, y) % 1f;
        };
    }

    /**
     * Useful function to define a channel
     * 
     * @return A channel painter whose value at a given point is equal to max
     *         (0, min (valueAt(x,y),1) )
     */
    default ChannelPainter clamped() {
        return (x, y) -> {
            return Math.max(0f, Math.min(valueAt(x, y), 1f));
        };
    }

}