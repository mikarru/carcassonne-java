package com.example.carcassonne;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class GameContext {
    private static class Container {
	public int score;
	public int holdingMeepleCount;
	public int returnedMeepleCount;
	public int onBoardMeepleCount;
    }

    private int initialHoldingMeepleCount;
    private Map<Integer, Container> map = new HashMap<>();

    public GameContext(int initialHoldingMeepleCount) {
	this.initialHoldingMeepleCount = initialHoldingMeepleCount;
    }
    
    public GameContext registerMeeple(int meepleColor) {
	if (map.containsKey(meepleColor)) {
	    throw new IllegalArgumentException();
	}
	Container c = new Container();
	c.holdingMeepleCount = initialHoldingMeepleCount;
	map.put(meepleColor, c);
	return this;
    }

    public void endTurn() {
	for (Container c : map.values()) {
	    c.holdingMeepleCount += c.returnedMeepleCount;
	    c.returnedMeepleCount = 0;
	}
    }

    public void returnMeeple(int meepleColor, int n) {
	Container c = map.get(meepleColor);
	assert c.onBoardMeepleCount >= n;
	c.returnedMeepleCount += n;
	c.onBoardMeepleCount -= n;
    }

    public void placeMeeple(int meepleColor) {
	Container c = map.get(meepleColor);
	if (c.holdingMeepleCount < 1) {
	    throw new IllegalArgumentException();
	}
	c.holdingMeepleCount -= 1;
	c.onBoardMeepleCount += 1;
    }

    public void addScore(int meepleColor, int score) {
	Container c = map.get(meepleColor);
	c.score += score;
    }

    public int getScore(int meepleColor) {
	return map.get(meepleColor).score;
    }

    public int getHoldingMeepleCount(int meepleColor) {
	return map.get(meepleColor).holdingMeepleCount;
    }

    public int getReturnedMeepleCount(int meepleColor) {
	return map.get(meepleColor).returnedMeepleCount;
    }

    public int getOnBoardMeepleCount(int meepleColor) {
	return map.get(meepleColor).onBoardMeepleCount;
    }

    public Set<Integer> getRegisteredMeeples() {
	return map.keySet();
    }
}
