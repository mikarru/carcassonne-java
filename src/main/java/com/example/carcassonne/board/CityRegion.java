package com.example.carcassonne.board;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CityRegion extends Region {
    private boolean completed = false;

    public CityRegion(int id, Board board) {
        super(id, board);
    }

    @Override
    public boolean isCompleted() {
        if (completed) {
            return true;
        }
        List<Segment> segments = getSegments();
        for (Segment s1 : segments) {
            Tile tile = s1.getTile();
            for (int d = 0; d < 4; d++) {
                int s1D = (d + 4 - tile.getRotation()) % 4;
                if (!s1.isAdjacentTo(s1D)) {
                    continue;
                }
                int adjacentX, adjacentY;
                switch (d) {
                case 0:
                    adjacentX = tile.getX();
                    adjacentY = tile.getY() + 1;
                    break;
                case 1:
                    adjacentX = tile.getX() + 1;
                    adjacentY = tile.getY();
                    break;
                case 2:
                    adjacentX = tile.getX();
                    adjacentY = tile.getY() - 1;
                    break;
                case 3:
                    adjacentX = tile.getX() - 1;
                    adjacentY = tile.getY();
                    break;
                default:
                    throw new RuntimeException("Never reach here");
                }
                boolean foundAdjacentSegment = false;
                for (Segment s2 : segments) {
                    if (s2.getTile().getX() == adjacentX && s2.getTile().getY() == adjacentY) {
                        foundAdjacentSegment = true;
                        continue;
                    }
                }
                if (!foundAdjacentSegment) {
                    return false;
                }
            }
        }
        completed = true;
        return true;
    }

    @Override
    public int calculateScore() {
        List<Segment> segments = getSegments();
        int pennantCount = 0;
        Set<Integer> uniqueTiles = new HashSet<>(segments.size());
        for (Segment s : segments) {
            if (s.hasPennant()) {
                pennantCount++;
            }
            uniqueTiles.add(s.getTile().getId());
        }
        int score = pennantCount + uniqueTiles.size();
        return isCompleted() ? score * 2 : score;
    }
}
