package com.example.carcassonne;


public class BoardPosition {
    private int x;
    private int y;

    public BoardPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return x + 31 * y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoardPosition) {
            BoardPosition that = (BoardPosition) o;
            return x == that.x && y == that.y;
        }
        return false;
    }
}
