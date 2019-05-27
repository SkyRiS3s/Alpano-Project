package ch.epfl.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public class Interval2DTest {
    
    @Test
    public void iXNormalTest() {
        Interval1D ix=new Interval1D(1,4);
        Interval1D iy=new Interval1D(2,7);
        Interval2D i = new Interval2D(ix, iy);
        assertEquals(ix, i.iX());
    }
    
    @Test
    public void iYNormalTest() {
        Interval1D ix=new Interval1D(1,4);
        Interval1D iy=new Interval1D(2,7);
        Interval2D i = new Interval2D(ix, iy);
        assertEquals(iy, i.iY());
    }
    @Test
    public void iXTest() {
        Interval1D ix=new Interval1D(1,4);
        Interval1D iy=new Interval1D(2,7);
        Interval2D i = new Interval2D(ix, iy);
        Interval1D a=i.iX();
        a=new Interval1D(1,2);
        assertEquals(ix, i.iX());
    }
    @Test
    public void iYTest() {
        Interval1D ix=new Interval1D(1,4);
        Interval1D iy=new Interval1D(2,7);
        Interval2D i = new Interval2D(ix, iy);
        Interval1D a=i.iY();
        a=new Interval1D(1,2);
        assertEquals(iy, i.iY());
    }
    @Test
    public void containsTest() {
        Interval2D i = new Interval2D(new Interval1D(1,4), new Interval1D(-3,20));
        Interval2D j = new Interval2D(new Interval1D(1,1), new Interval1D(-3,20));
        //Case 1:
        assertFalse(i.contains(5,-3));
        assertFalse(i.contains(0,0));
        
        //Case 2:
        assertTrue(i.contains(1,-3));
        
        //Case 3:
        assertTrue(i.contains(4,20));
        
        //Case 4:
        assertTrue(i.contains(1,-3));
    }
    @Test
    public void sizeTest() {
        //Case 1:
        Interval2D i = new Interval2D(new Interval1D(1,4), new Interval1D(-3,20));
        assertEquals(96,i.size(), 0);
        
        //Case 2:
        Interval2D j = new Interval2D(new Interval1D(1,2), new Interval1D(4,4));
        assertEquals(2 ,j.size(), 0);
        
        //Case 3:
        Interval2D k = new Interval2D(new Interval1D(9,10), new Interval1D(-40,27));
        assertEquals(136 ,k.size(), 0);
    }
    @Test
    public void sizeOfIntersectionWithTest() {
        //Case 1:
        Interval2D i = new Interval2D(new Interval1D(1,1), new Interval1D(-3,20));
        Interval2D j = new Interval2D(new Interval1D(1,4), new Interval1D(2,4));
        assertEquals(3, j.sizeOfIntersectionWith(i), 0);
        
        //Case 2:
        Interval2D m = new Interval2D(new Interval1D(1,5), new Interval1D(0,3));
        Interval2D n = new Interval2D(new Interval1D(2,6), new Interval1D(2,5));
        assertEquals(8, m.sizeOfIntersectionWith(n), 0);
    }
    @Test
    public void boundingUnionTest() {
        Interval2D m = new Interval2D(new Interval1D(1,5), new Interval1D(0,3));
        Interval2D n = new Interval2D(new Interval1D(5,6), new Interval1D(2,4));
        assertEquals(new Interval2D(new Interval1D(1,6), new Interval1D(0,4)), m.boundingUnion(n));
    }
    @Test
    public void isUnionableTest() {
        //Case 1: 
        Interval2D m = new Interval2D(new Interval1D(1,5), new Interval1D(0,3));
        Interval2D n = new Interval2D(new Interval1D(5,6), new Interval1D(2,4));
        assertTrue(m.isUnionableWith(n));
        assertTrue(n.isUnionableWith(m));
        //Case 2: 
        Interval2D i = new Interval2D(new Interval1D(1,5), new Interval1D(0,3));
        Interval2D j = new Interval2D(new Interval1D(7,7), new Interval1D(2,4));
        assertFalse(i.isUnionableWith(j));
        assertFalse(i.isUnionableWith(j));
    }
    @Test(expected = IllegalArgumentException.class)
    public void unionTest() {
        //Case 1:
        Interval2D m = new Interval2D(new Interval1D(1,5), new Interval1D(0,3));
        Interval2D n = new Interval2D(new Interval1D(5,6), new Interval1D(2,4));
        assertEquals(new Interval2D(new Interval1D(1,6), new Interval1D(0,4)), n.union(m));
        
        //Case 2:
        Interval2D i = new Interval2D(new Interval1D(1,5), new Interval1D(0,1));
        Interval2D j = new Interval2D(new Interval1D(5,6), new Interval1D(3,4));
        i.union(j);
    }
    @Test
    public void equalsTest() {
        Interval2D m = new Interval2D(new Interval1D(1,5), new Interval1D(0,3));
        Interval2D n = new Interval2D(new Interval1D(1,5), new Interval1D(0,3));
        assertEquals(m,n);
    }
}