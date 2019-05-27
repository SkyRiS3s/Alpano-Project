package ch.epfl.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

public class GazetteerParserTest {
    private File alps=new File("alps.txt");
    private List<Summit> list;
    
    private void createList(){
        list=new ArrayList<Summit>();
        try {
            list=GazetteerParser.readSummitsFrom(alps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void test()  {
        createList();
        assertEquals("MONTE CURT", list.get(0).name());

        assertEquals(Math.toRadians(7+25/60.0+12/3600.0), list.get(0).position().longitude(), 0);
        assertEquals(Math.toRadians(45+8/60.0+25/3600.0), list.get(0).position().latitude(), 0);
        
        assertEquals(Math.toRadians(10+10/60.0+32/3600.0), list.get(207).position().longitude(), 0);
        assertEquals(Math.toRadians(47+17/60.0+16/3600.0), list.get(207).position().latitude(), 0);
        
        assertEquals(Math.toRadians(14+17/60.0+05/3600.0), list.get(21068).position().longitude(), 0);
        assertEquals(Math.toRadians(47+49/60.0+42/3600.0), list.get(21068).position().latitude(), 0);
        
        assertEquals(1325, list.get(0).elevation(), 0);
        assertEquals(2366, list.get(207).elevation(), 0);
        assertEquals(3104, list.get(7526).elevation(), 0);        
    }
    
}
