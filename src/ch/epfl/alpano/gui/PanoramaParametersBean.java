package ch.epfl.alpano.gui;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * This class is a JavaFX bean containing the user parameters of a panorama
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 */
public final class PanoramaParametersBean {

    /**
     * Property containing the user parameters of a panorama
     */
    private ObjectProperty<PanoramaUserParameters> parametersProperty;
    
    /**
     * Map associating a user parameter to a property containing an integer.
     */
    private Map<UserParameter, ObjectProperty<Integer>> allParameters;
    
    /**
     * Constructor of the class
     * @param aPanoramaUserParameters
     */
    public PanoramaParametersBean(
            PanoramaUserParameters aPanoramaUserParameters) {

        Objects.requireNonNull(aPanoramaUserParameters);

        this.parametersProperty = new SimpleObjectProperty<PanoramaUserParameters>(
                aPanoramaUserParameters);

        this.allParameters = new EnumMap<>(UserParameter.class);

        for (UserParameter e : UserParameter.values()) {

            ObjectProperty<Integer> aProperty = new SimpleObjectProperty<Integer>(
                    aPanoramaUserParameters.get(e));
            aProperty.addListener((b, o, n) -> Platform.runLater(this::synchronizer));
            allParameters.put(e, aProperty);
        }
    }

    /**
     * Method which allows to get the property containing the (user) parameters
     * 
     * @return the property containing the (user) parameters, which is only
     *         readable
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parametersProperty;
    }

    /**
     * Public method which, given a user parameter, allows us to get a property
     * from the associative table.
     * 
     * @param aUserParameter
     *            used to get the property from the map
     * @return property containing an Integer
     */
    public ObjectProperty<Integer> getProperty(UserParameter aUserParameter) {
        return allParameters.get(aUserParameter);
    }

    /**
     * Method which allows to get the property containing the longitude
     * 
     * @return the property containing the longitude, which is wrtitable and
     *         readable
     */
    public ObjectProperty<Integer> observerLongitudeProperty() {
        return getProperty(UserParameter.OBSERVER_LONGITUDE);
    }
    
    /**
     * Method which allows to get the property containing the latitude
     * 
     * @return the property containing the latitude, which is wrtitable and
     *         readable
     */
    public ObjectProperty<Integer> observerLatitudeProperty() {
        return getProperty(UserParameter.OBSERVER_LATITUDE);
    }
    
    /**
     * Method which allows to get the property containing the observer's elevation
     * 
     * @return the property containing the observer's elevation, which is wrtitable and
     *         readable
     */
    public ObjectProperty<Integer> observerElevationProperty() {
        return getProperty(UserParameter.OBSERVER_ELEVATION);
    }
    
    /**
     * Method which allows to get the property containing the center Azimuth
     * 
     * @return the property containing the center azimuth, which is wrtitable
     *         and readable
     */
    public ObjectProperty<Integer> centerAzimuthProperty() {
        return getProperty(UserParameter.CENTER_AZIMUTH);
    }

    /**
     * Method which allows to get the property containing the horizontal Field
     * Of View
     * 
     * @return the property containing the horizontal Field Of View, which is
     *         wrtitable and readable
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
        return getProperty(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    
    /**
     * Method which allows to get the property containing the max distance
     * 
     * @return the property containing the max distance, which is wrtitable and
     *         readable
     */
    public ObjectProperty<Integer> maxDistanceProperty() {
        return getProperty(UserParameter.MAX_DISTANCE);
    }

    /**
     * Method which allows to get the property containing the width
     * 
     * @return the property containing the width, which is wrtitable and
     *         readable
     */
    public ObjectProperty<Integer> widthProperty() {
        return getProperty(UserParameter.WIDTH);
    }

    /**
     * Method which allows to get the property containing the height
     * 
     * @return the property containing the height, which is wrtitable and
     *         readable
     */
    public ObjectProperty<Integer> heightProperty() {
        return getProperty(UserParameter.HEIGHT);
    }

    /**
     * Method which allows to get the property containing the sampling exponent
     * 
     * @return the property containing the sampling exponent, which is wrtitable
     *         and readable
     */
    public ObjectProperty<Integer> superSamplingExponentProperty() {
        return getProperty(UserParameter.SUPER_SAMPLING_EXPONENT);

    }

    /**
     * Private method called by the constructor It allows us to synchronize the
     * individual parameters with the instance of PanoramaUserParameters.
     */
    private void synchronizer() {
        Map<UserParameter, Integer> aMap = new EnumMap<>(UserParameter.class);
        for (Map.Entry<UserParameter, ObjectProperty<Integer>> e : allParameters
                .entrySet()) {
            aMap.put(e.getKey(), e.getValue().get());
        }
        PanoramaUserParameters aParams = new PanoramaUserParameters(aMap);
        for (UserParameter e : UserParameter.values()) {
            getProperty(e).set(aParams.get(e));
        }
        this.parametersProperty.set(aParams);
    }

}
