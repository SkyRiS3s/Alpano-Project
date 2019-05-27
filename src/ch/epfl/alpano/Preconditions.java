package ch.epfl.alpano;

/**
 * Interface Preconditions: Provides methods which verify if the given arguments
 * are correct or not.
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 * 
 */
public interface Preconditions {

    /**
     * Method which signals an exception (without showing a message) because the
     * input (i.e the argument) was incorrect.
     * 
     * @param b
     *            boolean variable
     */
    public static void checkArgument(boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Method which signals an exception (by showing a message) because the
     * input (i.e the argument) was incorrect.
     * 
     * @param b
     *            boolean variable
     * @param message
     *            error message to the user
     */
    public static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }
}
