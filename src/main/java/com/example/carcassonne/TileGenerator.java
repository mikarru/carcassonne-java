package com.example.carcassonne;


public interface TileGenerator {
    boolean hasNextTile();
    Tile nextTile();
    int totalTileCount();
    int remainingTileCount();
    int generatedTileCount();
}
