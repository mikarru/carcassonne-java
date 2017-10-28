package com.example.carcassonne;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


public class CloisterRegionTest {
    private static final int X = 0;
    private static final int Y = 0;
    private static final int R = 0;

    @Test
    public void basicTest() {
        Board b = new Board(15);
        Region region = new CloisterRegion(10, b);
        Segment s = Segment.newCloister(0);
        Tile t = new Tile(0, "Cloister").setX(X).setY(Y).setRotation(R);
        s.setTile(t);
        region.addSegment(s);
        TilePositionManager manager = region.getBoard().getTilePositionManager();
        manager.placeTile(t, X, Y);
        Tile tile = new Tile(1, "dummy");
        for (int dx = -1; dx <= 1; dx++) {
            manager.placeTile(tile, X + dx, Y + 1);
        }
        assertFalse(region.isCompleted());
        assertEquals(4, region.calculateScore());
        for (int dy = -1; dy < 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx != 0 || dy != 0) {
                    manager.placeTile(tile, X + dx, Y + dy);
                }
            }
        }
        assertEquals(9, region.calculateScore());
        assertTrue(region.isCompleted());
    }
}
