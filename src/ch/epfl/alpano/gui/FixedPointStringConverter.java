package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.util.StringConverter;

/**
 * Class, which inherits from StringConverter<Integer>, and which converts
 * strings into Integers (and vice versa).
 * 
 * This class has the peculiarity that when it converts a string into an
 * Integer, it first converts it into a number, which is first stripped off from
 * its decimal point (i.e 1.23456 becomes 123456), then rounded (using the
 * HALF_UP method). To convert an Integer into a string, we do the inverse
 * operation (pretty much).
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public class FixedPointStringConverter extends StringConverter<Integer> {

    /*
     * integer representing up to how many decimal a number will be rounded to
     */
    int nbOfDecimals;

    /**
     * Constructor of the class
     * 
     * @param anInt
     *            integer representing up to how many decimal a number will be
     *            rounded to
     */
    public FixedPointStringConverter(int anInt) {
        this.nbOfDecimals = anInt;
    }

    /**
     * Method which converts a string into a Integer. The technique utilised is
     * the same as the one described in the description of the class
     * 
     * @param aString
     *            string who will be converted into an Integer
     * @return integer which was obtained by converting the givem string
     */
    @Override
    public Integer fromString(String aString) {
        BigDecimal scaled = new BigDecimal(aString)
                .setScale(this.nbOfDecimals, RoundingMode.HALF_UP)
                .movePointRight(this.nbOfDecimals);
        return scaled.intValueExact();
    }

    /**
     * Method which converts a Integer into a string. The technique utilised is
     * the same as the one described in the description of the class
     * 
     * @param aNumber
     *            Integer which will be converted into a string
     * @return string which was obtained by conveting the given integer
     */
    @Override
    public String toString(Integer aNumber) {
        if (aNumber == null) {
            return "";
        }
        BigDecimal value = new BigDecimal(aNumber)
                .movePointLeft(this.nbOfDecimals).stripTrailingZeros();
        return value.toPlainString();
    }

}
