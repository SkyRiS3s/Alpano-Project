package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Class Labelizer: its main method creates the labels for a given panorama by deciding which summits will have their label visible on the screen,
 * an instance is created by using a ContinuousElevationModel and a list of summits
 * 
 * @author Saoud Akram (273661)
 * @author Karim Kabbani (275044)
 */
public final class Labelizer {
    /*
     * Continuous elevation model the constructor takes in arguments
     */
    private final ContinuousElevationModel cem;
    /*
     * List of summits the constructor takes in arguments
     */
    private final List<Summit> summits;
    
    /**
     * Constructor of the class
     * @param cem
     * @param summits
     */
    public Labelizer(ContinuousElevationModel cem, List<Summit> summits){
        this.cem=Objects.requireNonNull(cem);
        Objects.requireNonNull(summits);
        this.summits=Collections.unmodifiableList(new ArrayList<>(summits));
    }
    /**
     * method that takes a PanoramaParameters and returns a list of nodes (one Text and one Line for each visible summit on the panorama) by starting with 
     * a sorted list of all the visible summits (using method visibleSummits()). Using a BitSet to represent the free positions (1: occupied, 0: free) we filled them according to 
     * the asked conditions 
     * @param p
     * @return list of nodes
     */
    public List<Node> labels(PanoramaParameters p){
        List<Node> nodes=new LinkedList<>();
        List<Triples> visible=visibleSummits(p);
        if(visible.size()!=0){
            BitSet positions=new BitSet(p.width());
            for(int i=0;i<20;++i){
                positions.set(i, true);
            }
            for(int i=p.width()-1;i>p.width()-21;--i){
                positions.set(i, true);
            }

            int textY=Integer.MAX_VALUE;
            for(Triples t : visible){
                if(t.getY()>=170){
                    if(textY==Integer.MAX_VALUE){
                        textY=t.getY()-22;
                    }
                    int count=0;
                    for(int i=0; i<20; ++i){
                        if(!positions.get((int)Math.round(t.getX())+i)){
                            ++count;
                        }
                    }
                    if(count==20){
                        positions.set((int)Math.round(t.getX()), true);
                        positions.set((int)Math.round(t.getX()+20), true); 
                        Text text = new Text(0, 0, t.summit().name()+" ("+t.summit().elevation()+" m)");
                        text.getTransforms().addAll(new Translate(t.getX(), textY), new Rotate(-60, 0, 0));
                        nodes.add(text);
                        nodes.add(new Line(t.getX(), textY+2, t.getX(), t.getY()));
                    }
                }
            }
        }
        return nodes;
    }
    /**
     * auxiliary private method used in the methods labels(), it takes a PanoramaParameters and returns the list of visible summits as a Triples type (includes the summit, the x component and the y component) by 
     * making sure that each summit added to the returned list is in the paronama using their x and y coordinates. To find these, we used for x the azimuth angle and for y the altitude angle.
     * @param p
     * @return list of Triples
     */
    public List<Triples> visibleSummits(PanoramaParameters p){
        List<Triples> visible=new LinkedList<>();
        int x;
        int y;
        double distance;
        for(Summit s : summits){
            distance=p.observerPosition().distanceTo(s.position());
            double a=p.observerPosition().azimuthTo(s.position());
            if(Math.abs(Math2.angularDistance(p.centerAzimuth(), a))<=p.horizontalFieldOfView()/2){
                x=(int)Math.round(p.xForAzimuth(a));
                ElevationProfile profile=new ElevationProfile(cem, p.observerPosition(), a, distance);
                DoubleUnaryOperator fY=PanoramaComputer.rayToGroundDistance(profile, p.observerElevation(), 0);
                double difElevation=-fY.applyAsDouble(distance);
                double angle=Math.atan2(difElevation, distance);
                if(Math.abs(angle) <= p.verticalFieldOfView() / 2){
                    y=(int)Math.round(p.yForAltitude(angle));
                    DoubleUnaryOperator fH=PanoramaComputer.rayToGroundDistance(profile, p.observerElevation(), difElevation / distance);
                    double root=Math2.firstIntervalContainingRoot(fH, 0, distance, 64d);
                    if(distance <= p.maxDistance() && (x>=0 && x<=p.width()-1) && (y>=0 && y<=p.height()-1) && root>=distance-200){
                       visible.add(new Triples(s, x, y));
                    }
                }
            }
        } 
        Collections.sort(visible, (Triples t1, Triples t2)-> Double.compare(t1.getY(), t2.getY())!=0 ?  Double.compare(t1.getY(), t2.getY()) : -Double.compare(t1.summit().elevation(), t2.summit().elevation()));
        return visible;
    }
    /**
     * auxiliary private class used to stock the summits with their owm x and y components
     * @author karimkabbani
     *
     */
    public final static class Triples{
        /*
         * Summit of the class
         */
        private Summit s;
        /*
         * x component of the Summit
         */
        private int x;
        /*
         * y component of the Summit
         */
        private int y;
        
        /**
         * Constructor
         * @param s
         * @param x
         * @param y
         */
        public Triples(Summit s, int x, int y) {
            this.s=Objects.requireNonNull(s);
            this.x=x;
            this.y=y;
        }
        /**
         * returns the Summit
         * @return s
         */
        public Summit summit(){
            return s;
        }
        /**
         * returns the x component
         * @return x
         */
        public int getX(){
            return x;
        }
        /**
         * returns the y component
         * @return y
         */
        public int getY(){
            return y;
        }
    }
}
