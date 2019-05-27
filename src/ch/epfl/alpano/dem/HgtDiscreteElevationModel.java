package ch.epfl.alpano.dem;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * Class HgtDiscreteElevationModel: reprsents a discrete DEM obtained from an
 * .hgt file
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 * 
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel {

    /*
     * Fields:
     */
    private ShortBuffer buffer;
    private final Interval2D anInterval;

    /**
     * Constructs a DEM whose samples are obtained by the HGT file
     * 
     * @param file:
     *            HGT File
     * @throws IOException
     */
    public HgtDiscreteElevationModel(File file) {
        String nom = file.getName();
        long l = file.length();
        
        try {
            Integer.parseInt(nom.substring(4, 7));
            Integer.parseInt(nom.substring(1, 3));

        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        boolean cond1 = (nom.charAt(0) == 'N') || (nom.charAt(0) == 'S');
        boolean cond2 = (nom.charAt(3) == 'W') || (nom.charAt(3) == 'E');
        boolean cond3;
        try {
            cond3 = (nom.substring(7, 11).equals(".hgt"));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        boolean cond4 = l == 25934402;

        Preconditions.checkArgument(cond1 && cond2 && cond3 && cond4);

        try (FileInputStream s = new FileInputStream(file)) {
            buffer = s.getChannel().map(MapMode.READ_ONLY, 0, l).asShortBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        try {
            Interval1D a = new Interval1D(
                    Integer.parseInt(nom.substring(4, 7)) * SAMPLES_PER_DEGREE,
                    (Integer.parseInt(nom.substring(4, 7)) + 1)
                            * SAMPLES_PER_DEGREE);
            Interval1D b1 = new Interval1D(
                    Integer.parseInt(nom.substring(1, 3)) * SAMPLES_PER_DEGREE,
                    (Integer.parseInt(nom.substring(1, 3)) + 1)
                            * SAMPLES_PER_DEGREE);
            this.anInterval = new Interval2D(a, b1);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void close() throws Exception {
        buffer = null;
    }

    @Override
    public Interval2D extent() {
        return anInterval;
    }

    @Override
    public double elevationSample(int x, int y) {
        Preconditions.checkArgument(extent().contains(x, y));
        return buffer.get(
                ((extent().iY().includedTo() - y) * (SAMPLES_PER_DEGREE + 1)
                        + (x - extent().iX().includedFrom())));
    }
}
