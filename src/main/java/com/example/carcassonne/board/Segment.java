package com.example.carcassonne.board;


public class Segment {
    public static enum SegmentType {
        CITY, ROAD, FIELD, CLOISTER;

        public static SegmentType fromString(String s) {
            switch (s) {
            case "city":
                return CITY;
            case "road":
                return ROAD;
            case "field":
                return FIELD;
            case "cloister":
                return CLOISTER;
            }
            throw new IllegalArgumentException();
        }
    }

    public static Segment newCity(int index, boolean pennant) {
        return new Segment(index, SegmentType.CITY, pennant);
    }

    public static Segment newRoad(int index) {
        return new Segment(index, SegmentType.ROAD);
    }

    public static Segment newField(int index) {
        return new Segment(index, SegmentType.FIELD);
    }

    public static Segment newCloister(int index) {
        return new Segment(index, SegmentType.CLOISTER);
    }

    private Tile tile;
    private int index;
    private Region region;
    private SegmentType type;
    private boolean pennant;
    
    static int NOT_PLACED = -1;
    private int placedMeepleColor = NOT_PLACED;

    public Segment(int index, SegmentType type, boolean pennant) {
        this.index = index;
        this.type = type;
        this.pennant = pennant;
    }

    public Segment(int index, SegmentType type) {
        this(index, type, false);
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getIndex() {
        return index;
    }

    public SegmentType getSegmentType() {
        return type;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean hasPennant() {
        return pennant;
    }
    
    public boolean meepleIsPlaced() {
        return placedMeepleColor != NOT_PLACED;
    }

    public void placeMeeple(int meepleColor) {
        this.placedMeepleColor = meepleColor;
        region.notifyMeeplePlacement(this);
    }

    public int getPlacedMeeple() {
        return placedMeepleColor;
    }

    public boolean isAdjacentTo(int direction) {
        switch (type) {
        case CITY:
            return tile.cities[direction] == index;
        case ROAD:
            return tile.roads[direction] == index;
        case FIELD:
            return tile.fields[direction] == index;
        case CLOISTER:
            return false;
        default:
            throw new RuntimeException("Never reach here");
        }
    }

    public boolean isSame(Segment that) {
        return getTile().getId() == that.getTile().getId() &&
            getIndex() == that.getIndex() &&
            getSegmentType() == that.getSegmentType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
            .append("Segment[")
            .append("type=").append(type).append(", ")
            .append("index=").append(index).append(", ")
            .append("pennant=").append(pennant).append(", ")
            .append("meeple=").append(placedMeepleColor).append(", ")
            .append("tile=").append(tile)
            .append("]");
        return sb.toString();
    }
}
