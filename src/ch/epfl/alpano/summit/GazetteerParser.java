package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.epfl.alpano.GeoPoint;

/**
 * Class GazetteerParser: represents a reader of a file containing information
 * about summits
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public class GazetteerParser {
    /**
     * Constructor of the class: As we want the class to be non-instanciable,
     * the constucted is private and empty
     */
    private GazetteerParser() {
    }

    /**
     * Method which reads and retrieves data from a file to create a list of
     * Summits from the data the file contains
     * 
     * @param file:
     *            file from which the data is retrieved
     * @return List of Summits constructed from the data in the given file
     * @throws IOException
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException {
        List<Summit> summits = new ArrayList<Summit>();
        try {
            FileInputStream fr = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fr,
                    StandardCharsets.US_ASCII);
            BufferedReader textReader = new BufferedReader(isr);
            String aLine;

            while ((aLine = textReader.readLine()) != null) {
                String name = getName(aLine);
                double longitude = getLongitude(aLine);
                double latitude = getLatitude(aLine);
                GeoPoint position = new GeoPoint(longitude, latitude);
                int elevation = getElevation(aLine);
                Summit aSummit = new Summit(name, position, elevation);
                summits.add(aSummit);
            }
            textReader.close();
        } catch (IndexOutOfBoundsException e) {
            throw new IOException();
        }
        return Collections.unmodifiableList(new ArrayList<>(summits));
    }

    /**
     * Method which allows us to get the name of a summit
     * 
     * @param line:
     *            line of the file form which the name of the summit is
     *            retrieved
     * @return the name of the summit
     * @throws IOException
     */
    private static String getName(String line) throws IOException {
        try {
            return line.substring(36, line.length());
        } catch (StringIndexOutOfBoundsException wrongException) {
            throw new IOException();
        }
    }

    /**
     * Method which retrieves the angle from a line and converts into radians
     * 
     * @param line:
     *            line of the file form which the angle is retrieved
     * @param a,
     *            b : integer variables representing the index at which the
     *            angle is situated (i.e, between which columns we get as
     *            substring)
     * @return the angle converted in radians
     * @throws IOException
     */
    private static double getAngle(String line, int a, int b)
            throws IOException {
        String angleInText = line.substring(a, b).trim();
        String[] angleInDegMinSec = angleInText.split(":");
        int[] l = new int[3];
        for (int i = 0; i < 3; i++) {

            try {
                Integer.parseInt(angleInDegMinSec[i]);
            } catch (NumberFormatException wrongExeption) {
                throw new IOException();
            }

            l[i] = Integer.parseInt(angleInDegMinSec[i]);
        }
        double angleInDeg = l[0] + l[1] / 60.0 + l[2] / 3600.0;
        return Math.toRadians(angleInDeg);
    }

    /**
     * Method which gives us the longitude of a summit
     * 
     * @param line:
     *            line form which the required data is retrieved
     * @return the longitude in radians
     * @throws IOException
     */
    private static double getLongitude(String line) throws IOException {
        return getAngle(line, 0, 9);
    }

    /**
     * Method which gives us the latitude of a summit
     * 
     * @param line:
     *            line from which the required data data is retrieved
     * @return the latitude in radians
     * @throws IOException
     */
    private static double getLatitude(String line) throws IOException {
        return getAngle(line, 10, 18);
    }

    /**
     * Method which gives us the elevation of a summit
     * 
     * @param line:
     *            line form which the required data is retrieved
     * @return the elevation of the summit
     * @throws IOException
     */
    private static int getElevation(String line) throws IOException {
        String elevation = line.substring(20, 24).trim();
        try {
            return Integer.parseInt(elevation);
        } catch (NumberFormatException wrongException) {
            throw new IOException();
        }
    }
}
