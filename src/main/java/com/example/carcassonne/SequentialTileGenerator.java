package com.example.carcassonne;

import java.util.Iterator;
import java.util.List;


public class SequentialTileGenerator implements TileGenerator {
    private List<Tile> tiles;
    private Iterator<Tile> iter;
    private int generated = 0;

    public SequentialTileGenerator(List<Tile> tiles) {
        this.tiles = tiles;
        this.iter = tiles.iterator();
    }

    @Override
    public boolean hasNextTile() {
        return iter.hasNext();
    }

    @Override
    public Tile nextTile() {
        generated++;
        return iter.next();
    }

    @Override
    public int totalTileCount() {
        return tiles.size();
    }

    @Override
    public int remainingTileCount() {
        return tiles.size() - generated;
    }

    @Override
    public int generatedTileCount() {
        return generated;
    }
}
