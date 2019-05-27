package ch.epfl.alpano.summit;


import java.util.Objects;
import ch.epfl.alpano.GeoPoint;

/**
 * Class Summit: represents a summit
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 * 
 */
public final class Summit {

    /*
     * Fields:
     */
    private final String name;
    private final GeoPoint position;
    private final int elevation;

    /**
     * Constructs a summit with the given name, position and elevation
     * 
     * @param name
     * @param position
     * @param elevation
     */
    public Summit(String name, GeoPoint position, int elevation) {
        this.name = Objects.requireNonNull(name);
        this.position = Objects.requireNonNull(position);
        this.elevation = elevation;
    }

    /*
     * The follwoing methods are getters
     */

    public String name() {
        return name;
    }

    public GeoPoint position() {
        return position;
    }

    public int elevation() {
        return elevation;
    }

    /**
     * Redefines the method toString in order to obtain a string composed of the
     * name of the summit, its position and its altitude
     */
    @Override
    public String toString() {
        String s = name + " " + position().toString() + " " + elevation;
        return s;
    }

}
