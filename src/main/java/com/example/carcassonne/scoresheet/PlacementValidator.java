package com.example.carcassonne.scoresheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.carcassonne.Board;
import com.example.carcassonne.GameContext;
import com.example.carcassonne.IdGenerator;
import com.example.carcassonne.Segment;
import com.example.carcassonne.SequentialTileGenerator;
import com.example.carcassonne.Tile;
import com.example.carcassonne.TileGenerator;
import com.example.carcassonne.Tiles;
import static com.example.carcassonne.scoresheet.ScoreSheet.ActionType;
import static com.example.carcassonne.scoresheet.ScoreSheet.MeeplePlacement;
import static com.example.carcassonne.scoresheet.ScoreSheet.Placement;
import static com.example.carcassonne.scoresheet.ScoreSheet.Player;
import static com.example.carcassonne.scoresheet.ScoreSheet.TilePlacement;


public class PlacementValidator {
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
            Placement placement = placements[i++];
            if (placement.action != ActionType.FIRST_PLACE) {
                return new Result(false, "firstPlace action is missing");
            }
            int rotation = (placement.tilePlacement != null) ? placement.tilePlacement.rotation : 0;
            board.setFirstTile(firstTile, rotation, context);
        }
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
                }
                context.endTurn();
                break;
            default:
                throw new RuntimeException("Never reach here");
            }
            i++;
        }
        System.out.println(context);
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
        System.out.println(r);
    }
}
