package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

/**
 * Class representing the parameters of a panorama from the point of view of the
 * user of the final software. In simple terms, it stores the value of the nine
 * parameters described in section 1.1 in the form of intgers expressed in their
 * corresponding unit
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 *
 */
public final class PanoramaUserParameters {

    /*
     * Map which stores the name of the parameter and its value (in its
     * corresponding unit)
     */
    private Map<UserParameter, Integer> userParams;

    /**
     * First constructor of the class which, from an associative table, assigns
     * to each value of the enumeration UserParameter the value of the
     * corresponding parameter
     * 
     * @param aMap
     *            associative table from which the PanoramaUserParameter is
     *            constructed
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> aMap) {
        for (Map.Entry<UserParameter, Integer> e : aMap.entrySet()) {
            UserParameter key = e.getKey();
            int newValue = key.sanitize(e.getValue());
            aMap.replace(key, newValue);
        }
        int height = aMap.get(UserParameter.HEIGHT);
        int width = aMap.get(UserParameter.WIDTH);
        int hFOV = aMap.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
        double newHeight = 170d * (width - 1d) / hFOV + 1d;
        if (height > newHeight) {
            aMap.replace(UserParameter.HEIGHT, (int) newHeight);
        }
        this.userParams = Collections.unmodifiableMap(new EnumMap<>(aMap));
    }

    /**
     * Second constructor of the class taking nine individual arguments as its
     * parameters and calling the first constructor.
     * 
     * @param observerLong
     *            represents the value of the observer's longitude
     * @param observerLat
     *            represents the value of the observer's latitude
     * @param observerElevation
     *            represents the value of the observer's elevation
     * @param centerAzimuth
     *            represents the value of the center azimuth
     * @param hFOV
     *            represents the value of the horizontal field of view
     * @param maxD
     *            represents the value of the max distance
     * @param width
     *            represents the value of the width
     * @param height
     *            represents the value of the height
     * @param samplingExp
     *            represents the value of the samping exponent
     */
    public PanoramaUserParameters(int observerLong, int observerLat,
            int observerElevation, int centerAzimuth, int hFOV, int maxD,
            int width, int height, int samplingExp) {

        this(inMap(observerLong, observerLat, observerElevation, centerAzimuth,
                hFOV, maxD, width, height, samplingExp));
    }

    /**
     * Private method which puts a variable amount of integers into an
     * associative table. The keys of this table are the values of the
     * enumeration "UserParameters" and its values are the integers passed into
     * argument in this method
     * 
     * @param parameters
     *            integers which will be put in an associative table (as its
     *            values)
     * @return an associative table whose keys are the values of the enumeration
     *         "UserParameters" and whose values are the integers passed as
     *         arguments in the method
     */
    private static Map<UserParameter, Integer> inMap(int... parameters) {
        Map<UserParameter, Integer> aMap = new EnumMap<>(UserParameter.class);
        for (int i = 0; i < parameters.length; i++) {
            aMap.put(UserParameter.values()[i], parameters[i]);
        }

        return aMap;
    }

    /**
     * Method which allows us to get the value of the parameter given a
     * parameter.
     * 
     * @param aParameter
     *            UserParameter whose value will be returned
     * @return the value of the parameter passed as argument
     */
    public int get(UserParameter aParameter) {
        return userParams.get(aParameter);
    }

    /**
     * Method which allows us to retrieve the value of the observer's longitude
     * 
     * @return the observer's longitude (in its corresponding unit) (i.e in 10
     *         thousand of a degree)
     */
    public int observerLongitude() {
        return get(UserParameter.OBSERVER_LONGITUDE);
    }

    /**
     * Method which allows us to retrieve the value of the observer's latitude
     * 
     * @return the observer's latitude (in its corresponding unit) (i.e in 10
     *         thousand of a degree)
     */
    public int observerLatitude() {
        return get(UserParameter.OBSERVER_LATITUDE);
    }

    /**
     * Method which allows us to retrieve the value of the observer's elevation
     * 
     * @return the observer's elevation (in its corresponding unit)
     */
    public int observerElevation() {
        return get(UserParameter.OBSERVER_ELEVATION);
    }

    /**
     * Method which allows us to retrieve the value of the center azimuth
     * 
     * @return the center azimuth (in its corresponding unit)
     */
    public int centerAzimuth() {
        return get(UserParameter.CENTER_AZIMUTH);
    }

    /**
     * Method which allows us to retrieve the value of the horizontal field of
     * view
     * 
     * @return the horizontal field of view (in its corresponding unit)
     */
    public int horizontalFOV() {
        return get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * Method which allows us to retrieve the value of the max distance
     * 
     * @return the max distance (in tis corresponding unit)
     */
    public int maxDistance() {
        return get(UserParameter.MAX_DISTANCE);
    }

    /**
     * Method which allows us to retrieve the value of the width
     * 
     * @return the width (in its corresponding unit)
     */
    public int width() {
        return get(UserParameter.WIDTH);
    }

    /**
     * Method which allows us to retrieve the value of the height
     * 
     * @return the height (in its corresponding unit)
     */
    public int height() {
        return get(UserParameter.HEIGHT);
    }

    /**
     * Method which allows us to retrieve the value of the sampling exponent
     * 
     * @return the sampling exponent
     */
    public int samplingExponent() {
        return get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }

    /**
     * Method which computes and returns a PanoramaParameters (the way it is
     * meant to be computed). Calls prePanoramaParameters (which is a private
     * mehtod)
     * 
     * @return a PanoramaParameters (the way it is meant to be computed)
     */
    public PanoramaParameters panoramaParameters() {
        return prePanoramaParameters(samplingExponent());
    }

    /**
     * Method which computes and returns a PanoramaParameters (the way it is
     * meant to be displayed). Calls prePanoramaParameters (which is a private
     * mehtod)
     * 
     * @return a PanoramaParameters (the way it is meant to be displayed, i.e
     *         the sampling exponent is 0)
     */
    public PanoramaParameters panoramaDisplayParameters() {
        return prePanoramaParameters(0);
    }

    /**
     * Private method which computes a panorama given a sampling exponent
     * 
     * @param sampling
     *            corresponds to the value of the samping exponent
     * @return a PanoramaParameters computed
     */
    private PanoramaParameters prePanoramaParameters(int sampling) {
        double longitude = Math.toRadians(observerLongitude() / 10000d);
        double latitude = Math.toRadians(observerLatitude() / 10000d);
        GeoPoint position = new GeoPoint(longitude, latitude);
        int observerElevation = observerElevation();
        double centerAzimuth = Math.toRadians(centerAzimuth());
        double hFOV = Math.toRadians(horizontalFOV());
        int maxDistance = maxDistance() * 1000;
        int width = (int) Math.scalb(width(), sampling);
        int height = (int) Math.scalb(height(), sampling);
        return new PanoramaParameters(position, observerElevation,
                centerAzimuth, hFOV, maxDistance, width, height);
    }
    
    
    /**
     * Redefinition of equals method to have a comparison by structure (here the maps are compared)
     */    
    @Override
    public boolean equals(Object o) {
        return (o instanceof PanoramaUserParameters &&
                ((PanoramaUserParameters) o).userParams.equals(this.userParams));
    }

    
    /**
     * Redefinition of hashCode method
     */
    @Override
    public int hashCode(){
        return Objects.hash(userParams);
    }

}