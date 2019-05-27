package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.StringConverter;

/**
 * Class, which inherits from StringConverter<Integer>, which converts strings
 * into integers (and vice versa).
 * 
 * This class has the peculiarity that, given a list of strings, to convert a
 * string into an Integer, it finds the index at which the string is stored and
 * returms this index. To convert an Integer into a String it simply finds the
 * string at the given index of the list of strings and returns it
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public class LabeledListStringConverter extends StringConverter<Integer> {

    /**
     * List where the strings will be stored
     */
    private List<String> listOfStrings;

    /**
     * Constructor of the class taking a variable amount of strings as arguments
     * and storing them into a list
     * 
     * @param s
     *            represents an array of strings
     */
    public LabeledListStringConverter(String... s) {
        this.listOfStrings = new ArrayList<>(Arrays.asList(s));
    }

    /**
     * Method which, given a string, returns its position in the listOfStrings
     * 
     * @param aString
     *            string whose position (i.e index) in the listOfStrings will be
     *            returned
     * @return the position of the given string in the listOfStrings
     */
    @Override
    public Integer fromString(String aString) {
        return this.listOfStrings.indexOf(aString);
    }

    /**
     * Method which, given an Integer, converts it into a string. To do so, it
     * returns the string contained in listOfStrings at the given index
     * 
     * @param aNumber
     *            Integer representing the index at which the string will be
     *            retrieved from the listOfStrings
     * @return the string contained in listOfStrings at the given index
     */
    @Override
    public String toString(Integer aNumber) {
        String s = this.listOfStrings.get(aNumber);
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

}
