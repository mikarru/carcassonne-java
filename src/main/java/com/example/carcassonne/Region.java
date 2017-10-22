package com.example.carcassonne;

import java.util.ArrayList;
import java.util.List;


public abstract class Region {
    protected Board board;
    protected int id;
    protected List<Segment> segments;
    protected List<Segment> onMeepleSegments;
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

    public List<Segment> getSegments() {
	return segments;
    }

    public void placeMeeple(Segment targetSegment, int meepleColor) {
	for (Segment segment : segments) {
	    if (segment.isSame(targetSegment)) {
		segment.placeMeeple(meepleColor);
		onMeepleSegments.add(segment);
		return;
	    }
	}
	throw new IllegalArgumentException("targetSegment is not contained on this Region");
    }
    
    public List<Segment> getOnMeepleSegment() {
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
	    context.addScore(meepleColor, score);
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

    // TODO
    public List<Integer> getWinningMeeples() {
	return null;
    }
    
    public abstract boolean isCompleted();
    public abstract int calculateScore();
}
