package ch.epfl.alpano.gui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
/**
 * Class Alpano: Graphical Interface
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 */
public final class Alpano extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }
    /**
     * Method that contains the principal code of the program
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        //Loading of the files and initialization of important variables
        List<Summit> summits = GazetteerParser
                .readSummitsFrom(new File("alps.txt"));
        ContinuousElevationModel cem = readHgt();
        PanoramaParametersBean parametersBean = new PanoramaParametersBean(
                PredefinedPanoramas.ALPES_DU_JURA);
        PanoramaComputerBean computerBean = new PanoramaComputerBean(cem,
                summits);

        //Creation of panoView, setting of the different properties and bindings
        ImageView panoView = new ImageView();
        panoView.imageProperty().bind(computerBean.imageProperty());
        panoView.fitWidthProperty().bind(parametersBean.widthProperty());
        panoView.setPreserveRatio(true);
        panoView.setSmooth(true);
        
        //Creation of paramsGrid
        GridPane paramsGrid = new GridPane();
        paramsGrid.setPadding(new Insets(10, 10, 10, 10));
        
        //Creation of the TextArea, setting of the different properties and bindings
        ObjectProperty<String> stringProperty = new SimpleObjectProperty<>();
        TextArea area = new TextArea();
        area.textProperty().bind(stringProperty);
        area.setPrefRowCount(2);
        area.setEditable(false);
        paramsGrid.add(area, 6, 0, 6, 3);
        
        //Once the mouse is moved, makes the information text appear in the TextArea
        panoView.setOnMouseMoved(e -> stringProperty.set(text(computerBean, parametersBean, e)));
        
        //When the mouse is clicked, opens the link to OpenStreetMap
        panoView.setOnMouseClicked(e -> map(computerBean, parametersBean, e));
        
        //Creation of labelsPane, setting of the different properties and bindings
        Pane labelsPane = new Pane();
        labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
        labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());
        labelsPane.setMouseTransparent(true);
        Bindings.bindContent(labelsPane.getChildren(),
                computerBean.getLabels());
        
        //Creation of updateText and setting of its properties
        Text updateText = new Text(
                "Les paramètres du panorama ont changé. \n Cliquez ici pour mettre le dessin à jour.");
        updateText.setFont(new Font(40));
        updateText.setTextAlignment(TextAlignment.CENTER);
        //Creation of updateNotice, setting of the different properties and bindings
        StackPane updateNotice = new StackPane(updateText);
        updateNotice.setBackground(new Background(
                new BackgroundFill(Color.color(1, 1, 1, 0.9), null, null)));
        updateNotice.visibleProperty().bind(computerBean.parametersProperty()
                .isNotEqualTo(parametersBean.parametersProperty()));
        //When the mouse is clicked on the updateNotice, the panorama is computed and displayed
        updateNotice.setOnMouseClicked(e -> {
            computerBean.setParameters(parametersBean.parametersProperty().get());
        });
        
        //Creation of panoPane by creating and using panoGroup and panoScrollPane
        StackPane panoGroup = new StackPane(panoView, labelsPane);
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);
        StackPane panoPane = new StackPane(panoScrollPane, updateNotice);
        
        //Adds all the labels for each TextField
        addLabel(paramsGrid, "Latitude (°):", 0, 0);
        addLabel(paramsGrid, "Longitude (°):", 0, 2);
        addLabel(paramsGrid, "Altitude (m):", 0, 4);
        addLabel(paramsGrid, "Azimuth (°):", 1, 0);
        addLabel(paramsGrid, "Angle de vue (°):", 1, 2);
        addLabel(paramsGrid, "Visibilité (km):", 1, 4);
        addLabel(paramsGrid, "Largeur (px):", 2, 0);
        addLabel(paramsGrid, "Hauteur (px):", 2, 2);
        addLabel(paramsGrid, "Suréchantillonage:", 2, 4);

        //TEXT FIELDS:
        StringConverter<Integer> fixedStringConverter = new FixedPointStringConverter(
                4);
        StringConverter<Integer> integerStringConverter = new IntegerStringConverter();
        TextFormatter<Integer> formatter;
        TextField field;
            //Creation of a Map to associate each element of UserParameter (exception for the super sampling exponent) to its PrefColumnCount value
        Map<UserParameter, Integer> map = new HashMap<>();
        map.put(UserParameter.OBSERVER_LATITUDE, 7);
        map.put(UserParameter.OBSERVER_LONGITUDE, 7);
        map.put(UserParameter.OBSERVER_ELEVATION, 4);
        map.put(UserParameter.CENTER_AZIMUTH, 3);
        map.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, 3);
        map.put(UserParameter.MAX_DISTANCE, 3);
        map.put(UserParameter.WIDTH, 4);
        map.put(UserParameter.HEIGHT, 4);
            //adds the corresponding text field for each UserParameter, with the required properties and bindings
        int counter = 0;
        for (UserParameter e : UserParameter.values()) {
            if (!e.equals(UserParameter.SUPER_SAMPLING_EXPONENT)) {
                //choice of the TextFormatter:
                if (e.equals(UserParameter.OBSERVER_LATITUDE)
                        || e.equals(UserParameter.OBSERVER_LONGITUDE)) {
                    formatter = new TextFormatter<>(fixedStringConverter);
                } else {
                    formatter = new TextFormatter<>(integerStringConverter);
                }
                //uses the public method "getProperty(UserParameter aUserParameter)" we added in PanoramaParametersBean
                formatter.valueProperty()
                .bindBidirectional(parametersBean.getProperty(e));
                field = new TextField(String.valueOf(
                        parametersBean.getProperty(e).get()));
                field.setTextFormatter(formatter);
                field.setAlignment(Pos.CENTER_RIGHT);
                field.setPrefColumnCount(map.get(e));
                //Computation of the positions on the grid
                if (e.equals(UserParameter.OBSERVER_LATITUDE)) {
                    paramsGrid.add(field, 1, 0);
                } else if (e.equals(UserParameter.OBSERVER_LONGITUDE)) {
                    paramsGrid.add(field, 3, 0);
                } else {
                    paramsGrid.add(field, 2 * (counter % 3) + 1, counter / 3);
                }
                ++counter;
            }
        }
        
        //Creation of the ChoiceBox, setting of the different properties and bindings
        ChoiceBox<Integer> cb = new ChoiceBox<>();
        cb.getItems().addAll(0, 1, 2);
        cb.setConverter(new LabeledListStringConverter("non", "2x", "4x"));
        cb.setValue(parametersBean.parametersProperty().get().samplingExponent());
        paramsGrid.add(cb, 5, 2);
        parametersBean.superSamplingExponentProperty().bindBidirectional(cb.valueProperty());
        
        //Creation of root and scene
        BorderPane root = new BorderPane(panoPane, null, null, paramsGrid,
                null);
        Scene scene = new Scene(root);
        
        //Displays the graphical interface
        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Auxiliary method that returns the text that appears in the information text according to the position of the mouse
     * @param computerBean
     * @param parametersBean
     * @param e
     * @return information text
     */
    private static String text(PanoramaComputerBean computerBean, PanoramaParametersBean parametersBean, MouseEvent e){
        Panorama pano = computerBean.panoramaProperty().get();
        double eX=withSampling(e.getX(), parametersBean);
        double eY=withSampling(e.getY(), parametersBean);
        
        double azimuth = Azimuth.canonicalize(computerBean.getParameters().panoramaParameters().azimuthForX(eX));
        double elevation = computerBean.getParameters().panoramaParameters().altitudeForY(eY);

        int x = (int) Math.round(eX);
        int y = (int) Math.round(eY);
        
        StringBuilder b = new StringBuilder("Position : "
                + Math.round(Math.toDegrees(pano.latitudeAt(x, y)) * 10000d)/10000d + "°N "
                + Math.round(Math.toDegrees(pano.longitudeAt(x, y)) * 10000d)/10000d + "°E\n");
        
        b.append("Distance : " + Math.round(pano.distanceAt(x, y)/100d)/10d + " km\n");
        b.append("Altitude : " + (int) pano.elevationAt(x, y) + " m\n");
        b.append("Azimut : " +  Math.round(Math.toDegrees(azimuth)*10d)/10d+ "° ("
                + Azimuth.toOctantString(azimuth, "N", "E", "S", "W")
                + ")     Elévation : " + Math.round(Math.toDegrees(elevation)*10d)/10d + "°\n");
        return b.toString();
    }
    /**
     * Auxiliary method that opens the OpenStreetMap link according to the position of the mouse
     * @param computerBean
     * @param parametersBean
     * @param e
     */
    private static void map(PanoramaComputerBean computerBean, PanoramaParametersBean parametersBean, MouseEvent e){
        Panorama pano = computerBean.panoramaProperty().get();
        
        int x = (int) withSampling(e.getX(), parametersBean);
        int y = (int) withSampling(e.getY(), parametersBean);
        
        double latitude = Math.toDegrees(pano.latitudeAt(x, y));
        double longitude = Math.toDegrees(pano.longitudeAt(x, y));

        String qy = "mlat=" + latitude + "&mlon=" + longitude;
        String fg = "map=15/" + latitude + "/" + longitude; 

        try {
            URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy,
                    fg);
            java.awt.Desktop.getDesktop().browse(osmURI);
        } catch (Exception exception) {
            throw new Error();
        }
    }
    /**
     * Auxiliary method that computes 
     * @param d
     * @param parametersBean
     * @return
     */
    private static double withSampling(double d, PanoramaParametersBean parametersBean){
        return Math.scalb(d, parametersBean.superSamplingExponentProperty().get());
    }
    /**
     * Method that creates a label for text fields
     * @param paramsGrid
     * @param s
     * @param row
     * @param column
     * @return
     */
    private static GridPane addLabel(GridPane paramsGrid, String s, int row,
            int column) {
        Label l = new Label(s);
        l.setPadding(new Insets(10, 10, 10, 10));
        paramsGrid.add(l, column, row);
        return paramsGrid;
    }
    /**
     * Reads the .hgt files from 45° to 47° for latitude and from 6° to 10° for longitude
     * @return the cem represented by the union of all the files
     */
    private static ContinuousElevationModel readHgt() {
        DiscreteElevationModel d1 = new HgtDiscreteElevationModel(
                new File("N45E006.hgt"));
        for (int j = 6; j < 10; ++j) {
            d1 = d1.union(new HgtDiscreteElevationModel(
                    new File("N45E00" + j + ".hgt")));
        }
        d1 = d1.union(
                new HgtDiscreteElevationModel(new File("N45E0" + 10 + ".hgt")));

        DiscreteElevationModel d2 = new HgtDiscreteElevationModel(
                new File("N46E006.hgt"));
        for (int j = 6; j < 10; ++j) {
            d2 = d2.union(new HgtDiscreteElevationModel(
                    new File("N46E00" + j + ".hgt")));
        }
        d2 = d2.union(
                new HgtDiscreteElevationModel(new File("N46E0" + 10 + ".hgt")));
        
        return new ContinuousElevationModel(d1.union(d2));
    }
}