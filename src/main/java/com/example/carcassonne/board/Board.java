package com.example.carcassonne.board;

import com.example.carcassonne.GameContext;
import com.example.carcassonne.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Board {
    private IdGenerator idGenerator = new IdGenerator();
    private TilePositionManager positionManager;
    private List<CityRegion> cityRegions = new ArrayList<>();
    private List<RoadRegion> roadRegions = new ArrayList<>();
    private List<FieldRegion> fieldRegions = new ArrayList<>();
    private List<CloisterRegion> cloisterRegions = new ArrayList<>();


    public Board(int tileN) {
        positionManager = new TilePositionManager(tileN);
    }

    TilePositionManager getTilePositionManager() {
        return positionManager;
    }

    List<CityRegion> getCityRegions() {
        return cityRegions;
    }

    List<RoadRegion> getRoadRegions() {
        return roadRegions;
    }

    List<FieldRegion> getFieldRegions() {
        return fieldRegions;
    }

    List<CloisterRegion> getCloisterRegions() {
        return cloisterRegions;
    }

    public boolean canPlaceTile(int x, int y, int rotation, Tile tile) {
        if (positionManager.isTilePlaced(x, y)) {
            return false;
        }
        if (!positionManager.getPossiblePositions().contains(new BoardPosition(x, y))) {
            return false;
        }
        return isPossibleAdjacence(x, y, rotation, tile);
    }

    public boolean hasPossiblePlacement(Tile tile) {
        Set<BoardPosition> possiblePositions = positionManager.getPossiblePositions();
        for (BoardPosition pos : possiblePositions) {
            for (int rotation = 0; rotation < 4; rotation++) {
                if (isPossibleAdjacence(pos.getX(), pos.getY(), rotation, tile)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPossibleAdjacence(int x, int y, int rotation, Tile tile) {
        Tile topTile = positionManager.getPlacedTile(x, y + 1);
        if (topTile != null && !topTile.canBottomAdjacentWith(tile, rotation)) {
            return false;
        }
        Tile bottomTile = positionManager.getPlacedTile(x, y - 1);
        if (bottomTile != null && !bottomTile.canTopAdjacentWith(tile, rotation)) {
            return false;
        }
        Tile rightTile = positionManager.getPlacedTile(x + 1, y);
        if (rightTile != null && !rightTile.canLeftAdjacentWith(tile, rotation)) {
            return false;
        }
        Tile leftTile = positionManager.getPlacedTile(x - 1, y);
        if (leftTile != null && !leftTile.canRightAdjacentWith(tile, rotation)) {
            return false;
        }
        return true;
    }

    public void setFirstTile(Tile tile, GameContext context) {
        setFirstTile(tile, 0, context);
    }

    public void setFirstTile(Tile tile, int rotation, GameContext context) {
        doPlaceTile(0, 0, rotation, tile, context);
    }
    
    public List<Segment> placeTile(int x, int y, int rotation, Tile tile, GameContext context) {
        if (!canPlaceTile(x, y, rotation, tile)) {
            throw new IllegalArgumentException();
        }
        return doPlaceTile(x, y, rotation, tile, context);
    }

    // TODO: Refactoring
    private List<Segment> doPlaceTile(int x, int y, int rotation, Tile tile, GameContext context) {
        tile.setX(x).setY(y).setRotation(rotation);
        positionManager.placeTile(tile, x, y);
        Tile[] aroundTiles = new Tile[] {
            positionManager.getPlacedTile(x, y + 1), positionManager.getPlacedTile(x + 1, y),
            positionManager.getPlacedTile(x, y - 1), positionManager.getPlacedTile(x - 1, y)
        };
        List<Segment> meeplePlaceCandidates = new ArrayList<>();
        List<Region> adjacentRegions = new ArrayList<>(4);
        Segment[] citySegments = tile.getCitySegments();
        for (Segment citySegment : citySegments) {
            for (int d = 0; d < 4; d++) {
                if (aroundTiles[d] == null) {
                    continue;
                }
                Tile aroundTile = aroundTiles[d];
                int myD = (d + 4 - rotation) & 0x3;
                if (citySegment.isAdjacentTo(myD)) {
                    int yourD = (d + 2 + 4 - aroundTile.getRotation()) & 0x3;
                    Segment yourSegment = aroundTile.getCitySegmentOfDirection(yourD);
                    adjacentRegions.add(yourSegment.getRegion());
                }
            }
            if (adjacentRegions.size() == 0) {
                CityRegion newRegion = new CityRegion(idGenerator.newId(), this);
                newRegion.addSegment(citySegment);
                cityRegions.add(newRegion);
                meeplePlaceCandidates.add(citySegment);
            } else {
                Region region = adjacentRegions.get(0);
                region.addSegment(citySegment);
                for (int i = 1; i < adjacentRegions.size(); i++) {
                    region.mergeRegion(adjacentRegions.get(i));
                }
                if (!region.meepleIsPlaced()) {
                    meeplePlaceCandidates.add(citySegment);
                } else if (region.isCompleted() && !region.scoreIsTransfered()) {
                    region.transferScore(context, true);
                }
            }
            adjacentRegions.clear();
        }
        Segment[] roadSegments = tile.getRoadSegments();
        for (Segment roadSegment : roadSegments) {
            for (int d = 0; d < 4; d++) {
                if (aroundTiles[d] == null) {
                    continue;
                }
                Tile aroundTile = aroundTiles[d];
                int myD = (d + 4 - rotation) & 0x3;
                if (roadSegment.isAdjacentTo(myD)) {
                    int yourD = (d + 2 + 4 - aroundTile.getRotation()) & 0x3;
                    Segment yourSegment = aroundTile.getRoadSegmentOfDirection(yourD);
                    adjacentRegions.add(yourSegment.getRegion());
                }
            }
            if (adjacentRegions.size() == 0) {
                RoadRegion newRegion = new RoadRegion(idGenerator.newId(), this);
                newRegion.addSegment(roadSegment);
                roadRegions.add(newRegion);
                meeplePlaceCandidates.add(roadSegment);
            } else {
                Region region = adjacentRegions.get(0);
                region.addSegment(roadSegment);
                for (int i = 1; i < adjacentRegions.size(); i++) {
                    region.mergeRegion(adjacentRegions.get(i));
                }
                if (!region.meepleIsPlaced()) {
                    meeplePlaceCandidates.add(roadSegment);
                } else if (region.isCompleted() && !region.scoreIsTransfered()) {
                    region.transferScore(context, true);
                }
            }
            adjacentRegions.clear();
        }
        Segment[] fieldSegments = tile.getFieldSegments();
        for (Segment fieldSegment : fieldSegments) {
            for (int d = 0; d < 8; d++) {
                if (aroundTiles[d / 2] == null) {
                    d++;
                    continue;
                }
                Tile aroundTile = aroundTiles[d / 2];
                int myD = (d + 8 - rotation * 2) % 8;
                if (fieldSegment.isAdjacentTo(myD)) {
                    int yourD = (d + (d%2==0 ? 5 : 3) + 8 - aroundTile.getRotation() * 2) % 8;
                    Segment yourSegment = aroundTile.getFieldSegmentOfDirection(yourD);
                    adjacentRegions.add(yourSegment.getRegion());
                }
            }
            if (adjacentRegions.size() == 0) {
                FieldRegion newRegion = new FieldRegion(idGenerator.newId(), this);
                newRegion.addSegment(fieldSegment);
                fieldRegions.add(newRegion);
                meeplePlaceCandidates.add(fieldSegment);
            } else {
                Region region = adjacentRegions.get(0);
                region.addSegment(fieldSegment);
                for (int i = 1; i < adjacentRegions.size(); i++) {
                    region.mergeRegion(adjacentRegions.get(i));
                }
                if (!region.meepleIsPlaced()) {
                    meeplePlaceCandidates.add(fieldSegment);
                }
            }
            adjacentRegions.clear();
        }
        Segment cloisterSegment = tile.getCloisterSegment();
        if (cloisterSegment != null) {
            CloisterRegion newRegion = new CloisterRegion(idGenerator.newId(), this);
            newRegion.addSegment(cloisterSegment);
            cloisterRegions.add(newRegion);
            meeplePlaceCandidates.add(cloisterSegment);
        }
        for (Region region : cloisterRegions) {
            if (region.meepleIsPlaced() && !region.scoreIsTransfered() && region.isCompleted()) {
                region.transferScore(context, true);
            }
        }
        return meeplePlaceCandidates;
    }

    public void placeMeeple(Segment segment, int meepleColor, GameContext context) {
        Region region = segment.getRegion();
        if (region.meepleIsPlaced()) {
            throw new IllegalArgumentException("Meeple is already placed");
        }
        segment.placeMeeple(meepleColor);
        context.placeMeeple(meepleColor);
        if (region.isCompleted()) {
            region.transferScore(context, true);
        }
    }

    public void transferRemainingScore(GameContext context) {
        for (Region region : cityRegions) {
            if (!region.isMerged() && !region.scoreIsTransfered()) {
                region.transferScore(context, true);
            }
        }
        for (Region region : roadRegions) {
            if (!region.isMerged() && !region.scoreIsTransfered()) {
                region.transferScore(context, true);
            }
        }
        for (Region region : fieldRegions) {
            if (!region.isMerged() && !region.scoreIsTransfered()) {
                region.transferScore(context, true);
            }
        }
        for (Region region : cloisterRegions) {
            if (!region.isMerged() && !region.scoreIsTransfered()) {
                region.transferScore(context, true);
            }
        }
    }
}
