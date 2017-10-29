package com.example.carcassonne;

import com.example.carcassonne.board.Board;
import com.example.carcassonne.board.Segment;
import com.example.carcassonne.board.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Game {
    private GameContext context = new GameContext(7);
    private TileGenerator tileGenerator;
    private List<Player> players = new ArrayList<>();

    public Game(TileGenerator tileGenerator) {
        this.tileGenerator = tileGenerator;
    }

    public Game registerPlayer(Player player) {
        players.add(player);
        context.registerPlayer(player.getColor());
        return this;
    }
        
    public void start() {
        // setup game
        Board board = new Board(tileGenerator.totalTileCount());
        if (tileGenerator.hasNextTile()) {
            Tile firstTile = tileGenerator.nextTile();
            board.setFirstTile(firstTile, context);
        } else {
            return;
        }

        // start game
        int turn = 0;
        while (tileGenerator.hasNextTile()) {
            Tile tile = tileGenerator.nextTile();
            if (!board.hasPossiblePlacement(tile)) {
                continue;
            }
            Player player = players.get(turn % players.size());
            // TODO
            int x = 0;
            int y = 0;
            int rotation = 0;
            List<Segment> segments = board.placeTile(x, y, rotation, tile, context);
            // TODO
            Segment segment = null;
            board.placeMeeple(segment, player.getColor(), context);
            context.endTurn();
            turn++;
        }

        // end game
        board.transferRemainingScore(context);
        for (Player player : players) {
            int score = context.getScore(player.getColor());
            player.setScore(score);
        }
        List<Player> sortedPlayers = new ArrayList<>(players);
        Collections.sort(sortedPlayers, Player.SCORE_COMPARATOR);
        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            System.out.println("Rank " + (i + 1) + ", Score " + player.getScore() + ", Name " + player.getName());
        }
    }
}
