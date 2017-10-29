package com.example.carcassonne.board;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


public class TilePositionManagerTest {
    private TilePositionManager manager;

    @Before
    public void setup() {
        manager = new TilePositionManager(10);
    }

    @Test
    public void basicTest() {
        Tile tile = new Tile(0, "tile");
        manager.placeTile(tile, 0, 0);
        assertTrue(manager.isTilePlaced(0, 0));
        assertFalse(manager.isTilePlaced(0, 1));
        assertTrue(tile == manager.getPlacedTile(0, 0));
        Set<BoardPosition> positions = manager.getPossiblePositions();
        assertEquals(4, positions.size());
    }
}
