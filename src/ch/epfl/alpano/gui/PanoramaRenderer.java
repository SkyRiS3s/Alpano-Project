package ch.epfl.alpano.gui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import ch.epfl.alpano.Panorama;

/**
 * Interface which allows us to render a panorama
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public interface PanoramaRenderer {

    /**
     * Method which allows us to obtain the image of a panorama given a Panorama
     * and an ImagePainter
     * 
     * @param panorama
     *            Panorama whose image will be rendered
     * @param painter
     *            ImagePainter which allows us to get the right colours for the
     *            image of the panorama
     * @return the image of a panorama
     */
    public static Image renderPanorama(Panorama panorama,
            ImagePainter painter) {
        int width = panorama.parameters().width();
        int height = panorama.parameters().height();
        WritableImage WI = new WritableImage(width, height);
        PixelWriter PW = WI.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = height - 1; y >= 0; y--) {
                PW.setColor(x, y, painter.colorAt(x, y));
            }
        }
        return WI;
    }
}