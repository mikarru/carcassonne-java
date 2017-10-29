package com.example.carcassonne.board;


public class CloisterRegion extends Region {
    private boolean completed = false;

    public CloisterRegion(int id, Board board) {
        super(id, board);
    }

    @Override
    public boolean isCompleted() {
        if (completed) {
            return true;
        }
        return calculateScore() == 9;
    }

    @Override
    public int calculateScore() {
        if (completed) {
            return 9;
        }
        TilePositionManager m = board.getTilePositionManager();
        assert segments.size() == 1 : "CloisterRegion has more or less 1 segment";
        Segment segment = segments.get(0);
        int x = segment.getTile().getX();
        int y = segment.getTile().getY();
        int score = 0;
        for (int dx = -1; dx <= 1; dx++) {
            int xx = x + dx;
            for (int dy = -1; dy <= 1; dy++) {
                int yy = y + dy;
                if (m.isTilePlaced(xx, yy)) {
                    score++;
                }
            }
        }
        if (score == 9) {
            completed = true;
        }
        return score;
    }
}
