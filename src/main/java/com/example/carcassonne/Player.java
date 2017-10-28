package com.example.carcassonne;

import java.util.Comparator;


public abstract class Player {
    public static final Comparator<Player> SCORE_COMPARATOR = new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                int score1 = player1.getScore();
                int score2 = player2.getScore();
                if (score1 > score2) {
                    return -1;
                } else if (score1 < score2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

    protected String name;
    protected int color;
    protected int score;

    Player(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
