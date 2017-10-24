package com.example.carcassonne;

import java.util.HashSet;
import java.util.Set;


public class TilePositionManager {
    private int size;
    private int shift;
    private Tile[] placement;
    private Set<BoardPosition> possiblePositions = new HashSet<>();
    
    public TilePositionManager(int tileN) {
        this.size = tileN * 2 - 1;
        this.shift = tileN - 1;
        this.placement = new Tile[size * size];
    }

    public void placeTile(Tile tile, int x, int y) {
        setTile(tile, x, y);
        possiblePositions.remove(new BoardPosition(x, y));
        checkAndAddPossiblePosition(x, y + 1);
        checkAndAddPossiblePosition(x, y - 1);
        checkAndAddPossiblePosition(x + 1, y);
        checkAndAddPossiblePosition(x - 1, y);
    }

    private void checkAndAddPossiblePosition(int x, int y) {
        if (!isTilePlaced(x, y)) {
            possiblePositions.add(new BoardPosition(x, y));
        }
    }

    public boolean isTilePlaced(int x, int y) {
        int index = convertToIndex(x, y);
        return placement[index] != null;
    }

    public Tile getPlacedTile(int x, int y) {
        int index = convertToIndex(x, y);
        return placement[index];
    }

    public Set<BoardPosition> getPossiblePositions() {
        return possiblePositions;
    }

    private void setTile(Tile tile, int x, int y) {
        int index = convertToIndex(x, y);
        placement[index] = tile;
    }
    
    private int convertToIndex(int x, int y) {
        int xShift = x + shift;
        int yShift = y + shift;
        return xShift + size * yShift;
    }
}
