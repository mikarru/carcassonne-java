package com.example.carcassonne.scoresheet;

import com.example.carcassonne.GameContext;
import com.example.carcassonne.SequentialTileGenerator;
import com.example.carcassonne.TileGenerator;
import com.example.carcassonne.Tiles;
import com.example.carcassonne.board.Board;
import com.example.carcassonne.board.Segment;
import com.example.carcassonne.board.Segment.SegmentType;
import com.example.carcassonne.board.Tile;
import static com.example.carcassonne.scoresheet.ScoreSheet.ActionType;
import static com.example.carcassonne.scoresheet.ScoreSheet.MeeplePlacement;
import static com.example.carcassonne.scoresheet.ScoreSheet.Placement;
import static com.example.carcassonne.scoresheet.ScoreSheet.Player;
import static com.example.carcassonne.scoresheet.ScoreSheet.TilePlacement;
import com.example.carcassonne.util.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PlacementValidator {
    private static final Logger LOG = LoggerFactory.getLogger(PlacementValidator.class);

    public static class Result {
        boolean isValid;
        String message;

        public Result(boolean isValid, String message) {
            this.isValid = isValid ;
            this.message = message;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Result[isValid: ").append(isValid)
                .append(", message: ").append(message);
            return sb.toString();
        }
    }

    public Result validate(ScoreSheet scoreSheet) {
        Placement[] placements = scoreSheet.getPlacements();
        TileGenerator tileGenerator = createTileGenerator(placements);
        Board board = new Board(tileGenerator.totalTileCount());
        GameContext context = new GameContext(7);
        Map<String, Integer> playerMap = new HashMap<>();
        for (int i = 0; i < scoreSheet.getPlayers().length; i++) {
            playerMap.put(scoreSheet.getPlayers()[i].name, i);
            context.registerPlayer(i);
        }

        int i = 0;
        if (tileGenerator.hasNextTile()) {
            Tile firstTile = tileGenerator.nextTile();
            Placement placement = placements[i];
            if (placement.action != ActionType.FIRST_PLACE) {
                return new Result(false, "firstPlace action is missing");
            }
            int rotation = (placement.tilePlacement != null) ? placement.tilePlacement.rotation : 0;
            board.setFirstTile(firstTile, rotation, context);
            LOG.info("[turn {}] Placed first tile {} to x=0, y=0 with rotation={}",
                    i, firstTile.getName(), rotation);
        }
        i++;
        while (tileGenerator.hasNextTile()) {
            Tile tile = tileGenerator.nextTile();
            Placement placement = placements[i];
            switch (placement.action) {
            case FIRST_PLACE:
                return new Result(false, "firstPlace action occures twice");
            case SKIP:
                if (board.hasPossiblePlacement(tile)) {
                    String message = "" + i + "th tile is skipped neverthless it can be placed";
                    return new Result(false, message);
                }
                LOG.info("[turn {}] Skipped tile {}", i, tile.getName());
                break;
            case PLAYER_PLACE:
                TilePlacement tileP = placement.tilePlacement;
                if (!board.canPlaceTile(tileP.x, tileP.y, tileP.rotation, tile)) {
                    String message = "" + i + "th tile " + tile.getName() +
                        " can't be placed with x=" + tileP.x +
                        ", y=" + tileP.y + ", rotation=" + tileP.rotation;
                    return new Result(false, message);
                }
                List<Segment> segments = board.placeTile(tileP.x, tileP.y, tileP.rotation, tile, context);
                LOG.info("[turn {}] {} placed tile {} to x={}, y={} with rotation={}",
                        i, placement.playerName, tile.getName(), tileP.x, tileP.y, tileP.rotation);
                MeeplePlacement meepleP = placement.meeplePlacement;
                if (meepleP != null) {
                    int meepleColor = playerMap.get(placement.playerName);
                    if (context.getHoldingMeepleCount(meepleColor) == 0) {
                        String message = "Player '" + placement.playerName +
                            "' tries to place meeple to " + i + "th tile " + tile.getName() +
                            ", neverthless he/she has no meeple";
                        return new Result(false, message);
                    }
                    Segment targetSegment = null;
                    for (Segment segment : segments) {
                        if (segment.getSegmentType() == meepleP.segmentType &&
                            segment.getIndex() == meepleP.segmentIndex) {
                            targetSegment = segment;
                            break;
                        }
                    }
                    if (targetSegment == null) {
                        String message = "Player '" + placement.playerName +
                            "' tries to place meeple to wrong segment of " + i + "th tile";
                        return new Result(false, message);
                    }
                    board.placeMeeple(targetSegment, meepleColor, context);
                    LOG.info("[turn {}] {} placed meeple to {} of tile {} with index={}",
                            i, placement.playerName, meepleP.segmentType, tile.getName(),
                            meepleP.segmentIndex);
                }
                for (Map.Entry<String, Integer> e : playerMap.entrySet()) {
                    String playerName = e.getKey();
                    int color = e.getValue();
                    for (SegmentType type : SegmentType.values()) {
                        int addedScore = context.getAddedScore(color, type);
                        if (addedScore != 0) {
                            LOG.info("[turn {}] {} gained {} point from {}",
                                    i, playerName, addedScore, type);
                        }
                    }
                }
                context.endTurn();
                break;
            default:
                throw new RuntimeException("Never reach here");
            }
            i++;
        }
        LOG.info("All tiles have been drawn");
        board.transferRemainingScore(context);
        for (Map.Entry<String, Integer> e : playerMap.entrySet()) {
            String playerName = e.getKey();
            int color = e.getValue();
            for (SegmentType type : SegmentType.values()) {
                int addedScore = context.getAddedScore(color, type);
                if (addedScore != 0) {
                    LOG.info("{} gained {} point from {}", playerName, addedScore, type);
                }
            }
        }
        context.endGame();
        for (Map.Entry<String, Integer> e : playerMap.entrySet()) {
            String playerName = e.getKey();
            int color = e.getValue();
            int score = context.getTotalScore(color);
            LOG.info("Last score of {}: {}", playerName, score);
        }
        return new Result(true, "ok");
    }

    private TileGenerator createTileGenerator(Placement[] placements) {
        List<Tile> tiles = new ArrayList<>(placements.length);
        IdGenerator idGen = new IdGenerator();
        for (Placement placement : placements) {
            tiles.add(Tiles.newFromName(idGen.newId(), placement.tileName));
        }
        return new SequentialTileGenerator(tiles);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: <json-path>");
            System.exit(1);
        }

        String path = args[0];
        ScoreSheet s = ScoreSheetUtil.readFromFile(path);
        System.out.println(s);
        Result r = new PlacementValidator().validate(s);
        if (r.isValid()) {
            System.out.println("ScoreSheet is valid");
            System.exit(0);
        } else {
            System.out.println("ScoreSheet is invalid: " + r.getMessage());
            System.exit(1);
        }
    }
}
