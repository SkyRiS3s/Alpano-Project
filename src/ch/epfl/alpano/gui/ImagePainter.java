package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.paint.Color;

/**
 * Functional Inteface representing an image painter.
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */

@FunctionalInterface
public interface ImagePainter {

    /**
     * Method which return the color at a given point
     * 
     * @param x
     *            x-coordinate of the point
     * @param y
     *            y-coordinate of the point
     * @return the colour at a given point
     */
    public Color colorAt(int x, int y);

    /**
     * Method which, given 4 channel painter, returns the corresponding
     * ImagePainteer
     * 
     * @param hue
     *            ChannelPainter representing the hue
     * @param saturation
     *            ChannelPainter representing the saturation
     * @param brightness
     *            ChannelPainter representing the birghtness
     * @param opacity
     *            ChannelPainter representing the opacity
     * @return the corresponding ImagePainter
     */
    public static ImagePainter hsb(ChannelPainter hue,
            ChannelPainter saturation, ChannelPainter brightness,
            ChannelPainter opacity) {
        return (x, y) -> Color.hsb((double) hue.valueAt(x, y),
                (double) saturation.valueAt(x, y),
                (double) brightness.valueAt(x, y),
                (double) opacity.valueAt(x, y));
    }

    /**
     * Method which, given two ChannelPainter, returns the corresponding
     * ImagePainter
     * 
     * @param gray
     *            ChannelPainter representing the level of gray
     * @param opacity
     *            ChannelPainter representing the opacity
     * @return the corresponding ImagePainter
     */
    public static ImagePainter gray(ChannelPainter gray,
            ChannelPainter opacity) {
        return (x, y) -> Color.gray((double) gray.valueAt(x, y),
                (double) opacity.valueAt(x, y));
    }

    /**
     * Method which allows to draw a panorama in grey levels
     * 
     * @param p
     *            panorama which will be drawn
     * @return ImagePainter which will be used to create an Image of the
     *         panorama p
     */
    public static ImagePainter grayPanoramaPainter(Panorama p) {
        ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p).sub(500)
                .div(4500).clamped().inverted();

        ChannelPainter distance = p::distanceAt;
        ChannelPainter opacity = distance
                .map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        return ImagePainter.gray(gray, opacity);
    }

    /**
     * Method which allows to draw the panorama in colour We decided to add this
     * method, because it turned out to be uselfull when creating the class
     * PanoramaComputerBean
     * 
     * @param p
     *            panorama which will be drawn
     * @return ImagePainter which will be used to create the Image of the
     *         panorama p
     */
    public static ImagePainter colourPanoramaPainter(Panorama p) {
        ChannelPainter hue = (x, y) -> p.distanceAt(x, y) / 100000 % 1 * 360;
        ChannelPainter saturation = (x, y) -> 1
                - Math.max(0, Math.min(p.distanceAt(x, y) / 200000, 1));
        ChannelPainter brightness = (x,
                y) -> (float) (0.3 + 0.7 * (1 - 2 * p.slopeAt(x, y) / Math.PI));
        ChannelPainter opacity = (x,
                y) -> p.distanceAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1;

        return hsb(hue, saturation, brightness, opacity);
    }
}
