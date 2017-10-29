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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder()
                .append("score=").append(score).append(", ")
                .append("holdingMeepleCount=").append(holdingMeepleCount).append(", ")
                .append("returnedMeepleCount=").append(returnedMeepleCount).append(", ")
                .append("onBoardMeepleCount=").append(onBoardMeepleCount);
            return sb.toString();
        }
    }

    private int initialHoldingMeepleCount;
    private Map<Integer, Container> map = new HashMap<>();

    public GameContext(int initialHoldingMeepleCount) {
        this.initialHoldingMeepleCount = initialHoldingMeepleCount;
    }
    
    public GameContext registerPlayer(int meepleColor) {
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

    public Set<Integer> getRegisteredPlayers() {
        return map.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GameContext[");
        for (Map.Entry<Integer, Container> e : map.entrySet()) {
            int color = e.getKey();
            Container c = e.getValue();
            sb.append(color).append(": {").append(c).append("}, ");
        }
        return sb.toString();
    }

    // for debugging
    public void checkMeepleCounts() {
        for (Map.Entry<Integer, Container> e : map.entrySet()) {
            int color = e.getKey();
            Container c = e.getValue();
            int totalMeepleCount = c.holdingMeepleCount + c.returnedMeepleCount + c.onBoardMeepleCount;
            if (totalMeepleCount > initialHoldingMeepleCount) {
                String message = "Meeple(color=" + color + ") increased from " +
                    initialHoldingMeepleCount + " to " + totalMeepleCount;
                throw new AssertionError(message + "\n" + c);
            } else if (totalMeepleCount < initialHoldingMeepleCount) {
                String message = "Meeple(color=" + color + ") decreased from " +
                    initialHoldingMeepleCount + " to " + totalMeepleCount;
                throw new AssertionError(message + "\n" + c);
            }
            if (c.holdingMeepleCount < 0) {
                String message = "holdingMeepleCount of meeple(color=" + color + ") " +
                    "is less than 0(" + c.holdingMeepleCount + ")";
                throw new AssertionError(message + "\n" + c);
            }
            if (c.returnedMeepleCount < 0) {
                String message = "returnedMeepleCount of meeple(color=" + color + ") " +
                    "is less than 0(" + c.returnedMeepleCount + ")";
                throw new AssertionError(message + "\n" + c);
            }
            if (c.onBoardMeepleCount < 0) {
                String message = "onBoardMeepleCount of meeple(color=" + color + ") " +
                    "is less than 0(" + c.onBoardMeepleCount + ")";
                throw new AssertionError(message + "\n" + c);
            }
        }
    }
}
