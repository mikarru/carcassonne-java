package com.example.carcassonne.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.carcassonne.Consts;
import com.example.carcassonne.GameContext;
import com.example.carcassonne.board.Segment.SegmentType;


public abstract class Region {
    protected Board board;
    protected int id;
    protected List<Segment> segments;
    protected List<Segment> onMeepleSegments = new ArrayList<>();
    protected boolean merged = false;
    protected boolean scoreTransfer = false;

    public Region(int id, Board board) {
        this(id, board, new ArrayList<Segment>());
    }

    private Region(int id, Board board, List<Segment> segments) {
        this.id = id;
        this.board = board;
        this.segments = segments;
    }

    public Board getBoard() {
        return board;
    }

    public int getId() {
        return id;
    }
    
    public void addSegment(Segment segment) {
        segments.add(segment);
        segment.setRegion(this);
    }

    List<Segment> getSegments() {
        return segments;
    }

    public void notifyMeeplePlacement(Segment segment) {
        assert segment.meepleIsPlaced();
        assert segment.getRegion() == this;
        onMeepleSegments.add(segment);
    }
    
    List<Segment> getOnMeepleSegments() {
        return onMeepleSegments;
    }

    public boolean meepleIsPlaced() {
        return onMeepleSegments.size() != 0;
    }

    public boolean mergeRegion(Region region) {
        if (this != region && !region.isMerged()) {
            for (Segment yourSegment : region.getSegments()) {
                yourSegment.setRegion(this);
                segments.add(yourSegment);
            }
            onMeepleSegments.addAll(region.getOnMeepleSegments());
            region.merged();
            return true;
        }
        return false;
    }

    public boolean isMerged() {
        return merged;
    }

    public void merged() {
        this.merged = true;
    }

    public void transferScore(GameContext context, boolean returnMeeple) {
        int score = calculateScore();
        for (Integer meepleColor : getWinningMeeples()) {
            context.addScore(meepleColor, getRegionType(), score);
        }
        if (returnMeeple) {
            returnMeeples(context);
        }
        scoreTransfer = true;
    }

    public boolean scoreIsTransfered() {
        return scoreTransfer;
    }

    public void returnMeeples(GameContext context) {
        for (Segment s : onMeepleSegments) {
            if (s.meepleIsPlaced()) {
                context.returnMeeple(s.getPlacedMeeple(), 1);
            }
        }
    }

    public List<Integer> getWinningMeeples() {
        int[] counts = new int[Consts.MAX_MEEPLE_COLOR];
        for (Segment s : onMeepleSegments) {
            counts[s.getPlacedMeeple()]++;
        }
        int max = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > max) {
                max = counts[i];
            }
        }
        if (max == 0) {
            return Collections.<Integer>emptyList();
        }
        List<Integer> meeples = new ArrayList<>(counts.length);
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] == max) {
                meeples.add(i);
            }
        }
        return meeples;
    }
    
    public abstract boolean isCompleted();
    public abstract int calculateScore();
    public abstract SegmentType getRegionType();
}
