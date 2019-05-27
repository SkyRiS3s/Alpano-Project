package ch.epfl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import ch.epfl.alpano.Interval1D;

public class Interval1DTest {
    
    /**
     * Test on the constructor when includedFrom> includedTo
     */
    @Test
    public void interval1DConstructorTest() {
        
    }
    
    /**
     * Test: includedFrom()
     * Verifies if the given integer is the correct one
     */
    @Test
    public void includedFromNormalTest() {
        Interval1D i = new Interval1D(1,5);
        assertEquals(1, i.includedFrom(), 0);
    }
    
    /**
     * Test: includedFrom() 
     * Verifies that an object's attributes cannot be modified
     */
    @Test
    public void includedFromTest() {
        Interval1D i = new Interval1D(1,4);
        Interval1D j = new Interval1D(1,4);
        int a = i.includedFrom();
        a = 2;
        assertEquals(j.includedFrom(),i.includedFrom(), 0); 
    }
    
    /**
     * Test: includedTo()
     * Verifies if the given integer is the correct one
     */
    @Test
    public void includedToNormalTest() {
        Interval1D i = new Interval1D(1,5);
        assertEquals(5, i.includedTo(), 0);
    }
    
    /**
     * Test: includedTo() 
     * Verifies that an object's attributes cannot be modified
     */
    @Test
    public void includedToTest() {
        Interval1D i = new Interval1D(1,4);
        Interval1D j = new Interval1D(1,4);
        int a = i.includedTo();
        a = 2;
        assertEquals(j.includedTo(), i.includedTo(), 0); 
    }
    
    /**
     * Test: contains()
     * Case 1: Verifies if the method returns false when an integer is not inside the interval
     * Case 2: Verifies if the method returns true when an integer is inside the interval
     * Case 3: Verifies if the method returns true when the integer is equal to includedFrom
     * Case 4: Verifies if the method returns true when the integer is equal to includedTo
     */
    @Test
    public void containsTest() {
        Interval1D i = new Interval1D(1,4);
        //Case 1:
        assertFalse(i.contains(5));
        assertFalse(i.contains(0));
        
        //Case 2:
        assertTrue(i.contains(3));
        
        //Case 3:
        assertTrue(i.contains(1));
        
        //Case 4:
        assertTrue(i.contains(4));
    }
    
    /**
     * Test: size()
     * Case 1: Verifies if an interval of length 0 (i.e where includedFrom and includedTo are equal) still contains one element
     * Case 2: Verifies if the given size is the correct one (normal case)
     * Case 3: Verifies that the size of the interval is 2, given that includedTo = includedFrom + 1
     */
    @Test
    public void sizeTest() {
        //Case 1:
        Interval1D i = new Interval1D(2,2);
        assertEquals(1,i.size(), 0);
        
        //Case 2:
        Interval1D k = new Interval1D(16,20);
        assertEquals(5,k.size(), 0);
        
        //Case 3:
        Interval1D j = new Interval1D(2,3);
        assertEquals(2,j.size(), 0);
    }
    
    /**
     * Test: sizeOfIntersectionWith()
     * Case 1: Normal Case
     * Case 2: When the two intervals do not have an intersection (i.e size = 0)
     * Case 3: When the intersection is of length 1
     * Case 4: When two intervals are the same
     * Case 5: 
     */
    @Test
    public void sizeOfIntersectionWithTest() {
        //Case 1:
        Interval1D i = new Interval1D(3,9);
        Interval1D j = new Interval1D(6,12);
        assertEquals(4, i.sizeOfIntersectionWith(j), 0);
        
        //Case 2:
        Interval1D k = new Interval1D(12,21);
        assertEquals(0, i.sizeOfIntersectionWith(k), 0);
        
        //Case 3:
        Interval1D l = new Interval1D(9,21);
        assertEquals(1, i.sizeOfIntersectionWith(l), 0);
        
        //Case 4:
        assertEquals(7, i.sizeOfIntersectionWith(i), 0);
        
        //Case 5:
        Interval1D p = new Interval1D(1,5);
        assertEquals(3, i.sizeOfIntersectionWith(p), 0);
    }
        
    
    /**
     * Test: boundingUnion()
     * Case 1: When the two intervals do not intersect
     * Case 2: When the two intervals intersect
     * Case 3: When the two intervals are the same
     */
    public void boundingUnionTest() {
        //Case 1:
        Interval1D test = new Interval1D(1,10);
        Interval1D i = new Interval1D(1,4);
        Interval1D j = new Interval1D(9,10);
        assertTrue(test.equals(i.boundingUnion(j)));
        assertTrue(test.equals(j.boundingUnion(i)));
        
        //Case 2:
        Interval1D k = new Interval1D(2,10);
        assertTrue(test.equals(k.boundingUnion(i)));
        assertTrue(test.equals(i.boundingUnion(k)));
        
        //Case 3:
        assertTrue(i.equals(i.boundingUnion(i)));
    }
    
    /**
     * Test: isUnionable()
     * Case 1: Normal case
     * Case 2: When two intervals intersect
     * Case 3: When an interval is contained in an other
     * Case 4: When two intervals are not unionable
     * Case 5: When two intervals are the same
     */
    @Test
    public void isUnionableTest() {
        //Case 1: 
        Interval1D i = new Interval1D(1,5);
        Interval1D j = new Interval1D(6,7);
        assertTrue(i.isUnionableWith(j));
        assertTrue(j.isUnionableWith(i));
        
        //Case 2:
        Interval1D k = new Interval1D(3,9);
        assertTrue(i.isUnionableWith(k));
        assertTrue(k.isUnionableWith(i));
        
        //Case 3:
        Interval1D t = new Interval1D(3,5);
        assertTrue(i.isUnionableWith(t));
        assertTrue(t.isUnionableWith(i));
        
        //Case 4:
        Interval1D p = new Interval1D(8,10);
        assertFalse(i.isUnionableWith(p));
        assertFalse(p.isUnionableWith(i));
        
        //Case 5:
        assertTrue(i.isUnionableWith(i));
    }
    
    /**
     * Test: union()
     * Case 1: Normal case
     * Case 2: When two intervals are not unionable (verifies if the error has been thrown or not)
     * Case 3: Union of the same interval
     */
    @Test(expected = IllegalArgumentException.class)
    public void unionTest() {
        //Case 1:
        Interval1D test = new Interval1D(1,10);
        Interval1D i = new Interval1D(1,5);
        Interval1D j = new Interval1D(6,10);
        assertEquals(test,i.union(j));
        assertEquals(test,j.union(i));
        
        //Case 2:
        Interval1D n = new Interval1D(1,1);
        assertEquals(n,n.union(n));
        
        //Case 3:
        assertTrue(i.equals(i.union(i)));
        //assertEquals(i,i.union(i));
        
        //Case 4:
        Interval1D k = new Interval1D(1,4);
        Interval1D l = new Interval1D(1,5);
        assertEquals(l,k.union(l));
        assertEquals(l,l.union(k));
        
        //Case 5 (with exception):
        Interval1D a = new Interval1D(1,4);
        Interval1D b = new Interval1D(6,7);
        a.union(b);
    }
    
    /**
     * Test: equals()
     * Case 1: Normal case
     * Case 2: Disjoint intervals
     * Case 3: Intervals which intersect one another
     * Case 4: Interval which is contained in another
     */
    @Test
    public void equalsTest() {
        //Case 1:
        Interval1D test = new Interval1D(2,2);
        Interval1D i = new Interval1D(2,2);
        assertTrue(test.equals(i));
        
        //Case 2:
        Interval1D k = new Interval1D(3,5);
        assertFalse(test.equals(k));
        
        //Case 3:
        Interval1D l = new Interval1D(2,7);
        assertFalse(test.equals(l));
        
        //Case 4:
        assertFalse(k.equals(l));
        
    }
    
    /**
     * Test: hashcode();
     */
    
    /**
     * Test: toString()
     * Verifies if the output is correct
     */
    @Test
    public void toStringTest() {
        Interval1D i = new Interval1D(2,2);
        Interval1D k = new Interval1D(2,6);
        assertEquals("[2..2]" , i.toString());
        assertEquals("[2..6]" , k.toString());

    }

}