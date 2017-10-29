package com.example.carcassonne.board;

import com.example.carcassonne.board.Segment.SegmentType;

import java.util.List;


public class FieldRegion extends Region {
    public FieldRegion(int id, Board board) {
        super(id, board);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public int calculateScore() {
        int score = 0;
        for (CityRegion cityRegion : board.getCityRegions()) {
            if (cityRegion.isMerged()) {
                continue;
            }
            if (cityRegion.isCompleted() && isAdjacentWith(cityRegion)) {
                score += 3;
            }
        }
        return score;   
    }

    private boolean isAdjacentWith(CityRegion cityRegion) {
        List<Segment> fieldSegments = getSegments();
        List<Segment> citySegments = cityRegion.getSegments();
        for (Segment fieldSegment : fieldSegments) {
            Tile myTile = fieldSegment.getTile();
            for (Segment citySegment : citySegments) {
                Tile yourTile = citySegment.getTile();
                if (myTile.getX() == yourTile.getX() && myTile.getY() == yourTile.getY()) {
                    if (myTile.isTwoSegmentAdjacent(fieldSegment.getIndex(), citySegment.getIndex())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public SegmentType getRegionType() {
        return SegmentType.FIELD;
    }
}
