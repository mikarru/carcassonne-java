package com.example.carcassonne.board;

import com.example.carcassonne.Tiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class RoadRegionTest {
    @Test
    public void loopTest() {
        Tile tile0 = Tiles.newFromName(0, "V").setX(0).setY(0).setRotation(0);
        Tile tile1 = Tiles.newFromName(1, "V").setX(0).setY(-1).setRotation(1);
        Tile tile2 = Tiles.newFromName(2, "V").setX(-1).setY(-1).setRotation(2);
        Tile tile3 = Tiles.newFromName(3, "V").setX(-1).setY(0).setRotation(3);
        RoadRegion region = new RoadRegion(0, null);
        region.addSegment(tile0.getRoadSegments()[0]);
        region.addSegment(tile1.getRoadSegments()[0]);
        region.addSegment(tile2.getRoadSegments()[0]);
        assertFalse(region.isCompleted());
        assertEquals(3, region.calculateScore());
        region.addSegment(tile3.getRoadSegments()[0]);
        assertTrue(region.isCompleted());
        assertEquals(4, region.calculateScore());
    }

    @Test
    public void loopWithCrossRoadTest() {
        Tile tile0 = Tiles.newFromName(0, "W").setX(0).setY(0).setRotation(0);
        Tile tile1 = Tiles.newFromName(1, "V").setX(0).setY(-1).setRotation(1);
        Tile tile2 = Tiles.newFromName(2, "V").setX(-1).setY(-1).setRotation(2);
        Tile tile3 = Tiles.newFromName(3, "V").setX(-1).setY(0).setRotation(3);
        RoadRegion region = new RoadRegion(0, null);
        region.addSegment(tile0.getRoadSegments()[1]);
        region.addSegment(tile1.getRoadSegments()[0]);
        region.addSegment(tile2.getRoadSegments()[0]);
        assertFalse(region.isCompleted());
        assertEquals(3, region.calculateScore());
        region.addSegment(tile3.getRoadSegments()[0]);
        region.addSegment(tile0.getRoadSegments()[2]);
        assertTrue(region.isCompleted());
        assertEquals(4, region.calculateScore());
    }

    @Test
    public void nonLoopTest() {
        Tile tile0 = Tiles.newFromName(0, "A").setX(0).setY(0).setRotation(0);
        Tile tile1 = Tiles.newFromName(1, "U").setX(0).setY(-1).setRotation(0);
        Tile tile2 = Tiles.newFromName(2, "X").setX(0).setY(-2).setRotation(0);
        RoadRegion region = new RoadRegion(0, null);
        region.addSegment(tile0.getRoadSegments()[0]);
        region.addSegment(tile1.getRoadSegments()[0]);
        assertFalse(region.isCompleted());
        assertEquals(2, region.calculateScore());
        region.addSegment(tile2.getRoadSegments()[0]);
        assertTrue(region.isCompleted());
        assertEquals(3, region.calculateScore());
    }
}
