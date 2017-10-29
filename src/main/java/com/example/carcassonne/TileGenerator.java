package com.example.carcassonne;

import com.example.carcassonne.board.Tile;


public interface TileGenerator {
    boolean hasNextTile();
    Tile nextTile();
    int totalTileCount();
    int remainingTileCount();
    int generatedTileCount();
}
