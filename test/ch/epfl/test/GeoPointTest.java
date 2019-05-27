package ch.epfl.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;

public class GeoPointTest {

    @Test
    public void distanceToTest() {
        GeoPoint a=new GeoPoint(Math.toRadians(7.17697), Math.toRadians(46.33283));
        GeoPoint b=new GeoPoint(Math.toRadians(9.16302), Math.toRadians(47.24135));
        assertEquals(179.45e3, a.distanceTo(b), 3000);
    }
    @Test
    public void trivialDistanceToTest() {
        GeoPoint a=new GeoPoint(Math.toRadians(7.17697), Math.toRadians(46.33283));
        assertEquals(0, a.distanceTo(a), 0);
    }
    @Test
    public void toStringTest() {
        GeoPoint a=new GeoPoint(Math.toRadians(7.17697), Math.toRadians(46.33283));
        assertEquals("(7.1770,46.3328)", a.toString());
    }
    @Test
    public void azimuthToTest() {
        GeoPoint a=new GeoPoint(Math.toRadians(7.95661), Math.toRadians(47.12175));
        GeoPoint b=new GeoPoint(Math.toRadians(8.93852), Math.toRadians(47.31195));
        assertEquals(75.04, Math.toDegrees(a.azimuthTo(b)), 3);
    }
}
