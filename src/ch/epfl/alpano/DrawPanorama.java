package ch.epfl.alpano;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

final class DrawPanorama {
    final static File HGT_FILE = new File("N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

//    final static int IMAGE_WIDTH = 2500;
//    final static int IMAGE_HEIGHT = 800;
    
    final static double ORIGIN_LON = Math.toRadians(7.65);
    final static double ORIGIN_LAT = Math.toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = Math.toRadians(180);
    final static double HORIZONTAL_FOV = Math.toRadians(60);
    final static int MAX_DISTANCE = 100_000;
    
//    final static double HORIZONTAL_FOV = Math.toRadians(110);
//    final static int MAX_DISTANCE = 300_000;

    final static PanoramaParameters PARAMS = new PanoramaParameters(
            new GeoPoint(ORIGIN_LON, ORIGIN_LAT), ELEVATION, CENTER_AZIMUTH,
            HORIZONTAL_FOV, MAX_DISTANCE, IMAGE_WIDTH, IMAGE_HEIGHT);

    public static void main(String[] as) throws Exception {
        try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(
                HGT_FILE)) {
            ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
            Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);

            BufferedImage i = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
                    BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < IMAGE_WIDTH; ++x) {
                for (int y = 0; y < IMAGE_HEIGHT; ++y) {
                    float d = p.distanceAt(x, y);
                    int c = (d == Float.POSITIVE_INFINITY) ? 0x87_CE_EB
                            : gray((d - 2_000) / 15_000);
                    i.setRGB(x, y, c);
                }
            }
            ImageIO.write(i, "png", new File("niesen.png"));

//            ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p)
//                    .sub(500).div(4500).clamped().inverted();
//
//            ChannelPainter distance = p::distanceAt;
//            ChannelPainter opacity = distance
//                    .map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
//
//            ImagePainter l = ImagePainter.gray(gray, opacity);
            
            ImagePainter l = ImagePainter.grayPanoramaPainter(p);

//             Image image1 = PanoramaRenderer.renderPanorama(p, l);
//            
//             ImageIO.write(SwingFXUtils.fromFXImage(image1, null),
//             "png",
//             new File("niesen-profile.png"));

//            ChannelPainter hue = (x, y) -> p.distanceAt(x, y) / 100000 % 1
//                    * 360;
//            ChannelPainter saturation = (x, y) -> 1
//                    - Math.max(0, Math.min(p.distanceAt(x, y) / 200000, 1));
//            ChannelPainter brightness = (x, y) -> (float) (0.3
//                    + 0.7 * (1 - 2 * p.slopeAt(x, y) / Math.PI));
//            ChannelPainter opacitiy = (x,
//                    y) -> p.distanceAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1;
//
//            ImagePainter IP = ImagePainter.hsb(hue, saturation, brightness,
//                    opacitiy);
            
            ImagePainter IP = ImagePainter.colourPanoramaPainter(p);
            
            Image image2 = PanoramaRenderer.renderPanorama(p, IP);
            ImageIO.write(SwingFXUtils.fromFXImage(image2, null), "png",
                    new File("niesen-profile.png"));
        }
    }

    private static int gray(double v) {
        double clampedV = Math.max(0, Math.min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
    }
}