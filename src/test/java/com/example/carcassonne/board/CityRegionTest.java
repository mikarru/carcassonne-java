package com.example.carcassonne.board;

import com.example.carcassonne.Tiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class CityRegionTest {
    @Test
    public void cupCityTest() {
        Tile tile0 = Tiles.newFromName(0, "E").setX(0).setY(0).setRotation(2);
        Tile tile1 = Tiles.newFromName(1, "E").setX(0).setY(-1).setRotation(0);
        CityRegion region = new CityRegion(0, null);
        region.addSegment(tile0.getCitySegments()[0]);
        assertFalse(region.isCompleted());
        assertEquals(1, region.calculateScore());
        region.addSegment(tile1.getCitySegments()[0]);
        assertTrue(region.isCompleted());
        assertEquals(4, region.calculateScore());
    }

    @Test
    public void diamondCityTest() {
        Tile tile0 = Tiles.newFromName(0, "N").setX(0).setY(0).setRotation(0);
        Tile tile1 = Tiles.newFromName(1, "N").setX(-1).setY(0).setRotation(1);
        Tile tile2 = Tiles.newFromName(2, "N").setX(-1).setY(1).setRotation(2);
        Tile tile3 = Tiles.newFromName(3, "P").setX(0).setY(1).setRotation(3);
        CityRegion region = new CityRegion(0, null);
        region.addSegment(tile0.getCitySegments()[0]);
        region.addSegment(tile1.getCitySegments()[0]);
        region.addSegment(tile2.getCitySegments()[0]);
        assertFalse(region.isCompleted());
        assertEquals(3, region.calculateScore());
        region.addSegment(tile3.getCitySegments()[0]);
        assertTrue(region.isCompleted());
        assertEquals(8, region.calculateScore());
    }

    @Test
    public void diamondCityWithOnePennantTest() {
        Tile tile0 = Tiles.newFromName(0, "N").setX(0).setY(0).setRotation(0);
        Tile tile1 = Tiles.newFromName(1, "N").setX(-1).setY(0).setRotation(1);
        Tile tile2 = Tiles.newFromName(2, "M").setX(-1).setY(1).setRotation(2);
        Tile tile3 = Tiles.newFromName(3, "P").setX(0).setY(1).setRotation(3);
        CityRegion region = new CityRegion(0, null);
        region.addSegment(tile0.getCitySegments()[0]);
        region.addSegment(tile1.getCitySegments()[0]);
        region.addSegment(tile2.getCitySegments()[0]);
        assertFalse(region.isCompleted());
        assertEquals(4, region.calculateScore());
        region.addSegment(tile3.getCitySegments()[0]);
        assertTrue(region.isCompleted());
        assertEquals(10, region.calculateScore());
    }

    @Test
    public void diamondCityIncludingITileTest() {
        Tile tile0 = Tiles.newFromName(0, "N").setX(0).setY(0).setRotation(0);
        Tile tile1 = Tiles.newFromName(1, "N").setX(-1).setY(0).setRotation(1);
        Tile tile2 = Tiles.newFromName(2, "I").setX(-1).setY(1).setRotation(0);
        Tile tile3 = Tiles.newFromName(3, "P").setX(0).setY(1).setRotation(3);
        CityRegion region = new CityRegion(0, null);
        region.addSegment(tile0.getCitySegments()[0]);
        region.addSegment(tile1.getCitySegments()[0]);
        region.addSegment(tile3.getCitySegments()[0]);
        assertFalse(region.isCompleted());
        assertEquals(3, region.calculateScore());
        region.addSegment(tile2.getCitySegments()[0]);
        region.addSegment(tile2.getCitySegments()[1]);
        assertTrue(region.isCompleted());
        assertEquals(8, region.calculateScore());
    }
}
