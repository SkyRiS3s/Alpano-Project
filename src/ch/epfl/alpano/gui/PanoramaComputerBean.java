package ch.epfl.alpano.gui;

import java.util.List;
import java.util.Objects;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.unmodifiableObservableList;

/**
 * This class is a JavaFX bean which has the four following properties: the
 * panorama, its user parameters, its image and its labels
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 */
public final class PanoramaComputerBean {

    
    /*
     * Property which contains the panorama. It is initialized when the
     * constructor is called
     */
    private ObjectProperty<Panorama> panoramaProperty;
    
    /*
     * Property which contains the user parameters. It is initialized when the
     * constructor is called
     */
    private ObjectProperty<PanoramaUserParameters> parametersProperty;
    
    /*
     * Property which contains the image of the panorama.It is initialized when
     * the constructor is called
     */
    private ObjectProperty<Image> imageProperty;
    
    /*
     * Property which contains the list of labels. It is initialized when the
     * constructor is called
     */
    private ObjectProperty<ObservableList<Node>> labelsProperty;
    
    /**
     * Panorama Computer which is used to create (or, namely, compute) a
     * panorama.
     */
    private PanoramaComputer aPanoramaComputer;
    
    /**
     * Labelizer which is used to get the labels of a given panorama
     */
    private Labelizer aLabelizer;
    
    /**
     * List of labels
     */
    private ObservableList<Node> labels;
    
    /**
     * Constructor of the class
     * 
     * @param dem
     *            used to create a panorama
     * @param listOfSummits
     *            used to create a labelizer
     */
    public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> listOfSummits) {
        Objects.requireNonNull(cem);
        Objects.requireNonNull(listOfSummits);
        
        this.aPanoramaComputer = new PanoramaComputer(cem);
        this.aLabelizer = new Labelizer(cem, listOfSummits);
        this.labels = observableArrayList();
        this.panoramaProperty = new SimpleObjectProperty<Panorama>();
        this.imageProperty = new SimpleObjectProperty<Image>();
        this.labelsProperty = new SimpleObjectProperty<ObservableList<Node>>(
                unmodifiableObservableList(this.labels)); 
        this.parametersProperty =  new SimpleObjectProperty<PanoramaUserParameters>();
        this.parametersProperty.addListener((b, o, n) -> {
            PanoramaParameters aPanoramaParameters = n.panoramaParameters();
            PanoramaParameters bPanoramaParameters = n.panoramaDisplayParameters();
            Panorama aPanorama = this.aPanoramaComputer.computePanorama(aPanoramaParameters);
            panoramaProperty.set(aPanorama);

            ImagePainter anImagePainter = ImagePainter.colourPanoramaPainter(aPanorama);
            Image anImage = PanoramaRenderer.renderPanorama(aPanorama, anImagePainter);
            imageProperty.set(anImage);

            List<Node> updatedLabels = this.aLabelizer.labels(bPanoramaParameters);
            labels.setAll(updatedLabels);
        });
        
    }
    
    /**
     * Method which allows to get the property containing the (user) parameters
     * 
     * @return the property containing the (user) parameters, which is wrtitable
     *         and readable
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty() {
        return this.parametersProperty;
    }

    /**
     * Method which allow to obtain the parameters stored inside the property
     * containing it
     * 
     * @return the panorama user parameters of the panorama
     */
    public PanoramaUserParameters getParameters() {
        return this.parametersProperty.get();
    }
    
    /**
     * Method which allows us to set the user parameters
     * 
     * @param newParameters
     *            represents the new parameters
     */
    public void setParameters(PanoramaUserParameters newParameters) {
        Objects.requireNonNull(newParameters);
        this.parametersProperty.set(newParameters);
    }
    
    /**
     * Method which allows to get the property containing the (user) parameters
     * 
     * @return the property containing the (user) parameters, which is only
     *         readable
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty() {
        return panoramaProperty;
    }

    /**
     * Method which allows to get the property containing the Image of the
     * panorama
     * 
     * @return the property containing the image of a panorama, which is only
     *         readable
     */
    public ReadOnlyObjectProperty<Image> imageProperty() {
        return this.imageProperty;
    }

    /**
     * Method which allow to obtain the Image stored inside the property
     * containing it
     * 
     * @return the Image of the panorama
     */
    public Image getImage() {
        return this.imageProperty.get();
    }
    
    /**
     * Method which returns the property containing the list of labels
     * 
     * @return the property containing the list of labels. which is only
     *         readable
     */
    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty() {
        return this.labelsProperty;
    }
    
    /**
     * Method which allows us to obtian the list of labels stored in the
     * property containing it
     * 
     * @return the list of labels
     */
    public ObservableList<Node> getLabels() {
        return this.labelsProperty.get();
    }
}
