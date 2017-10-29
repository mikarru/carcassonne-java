package com.example.carcassonne.board;


public class Tile {
    private int id;
    private String name;

    private int x;
    private int y;
    private int rotation;

    private BorderType[] borderTypes;
    private Segment[] citySegments;
    private Segment[] roadSegments;
    private Segment[] fieldSegments;
    private Segment cloisterSegment;
    int[] cities;
    int[] roads;
    int[] fields;

    public Tile(int id, String name, BorderType[] borderTypes,
            Segment[] citySegments, Segment[] roadSegments,
            Segment[] fieldSegments, Segment cloisterSegment,
                int[] cities, int[] roads, int[] fields) {
        this.id = id;
        this.name = name;
        this.borderTypes = borderTypes;
        this.citySegments = citySegments;
        this.roadSegments = roadSegments;
        this.fieldSegments = fieldSegments;
        this.cloisterSegment = cloisterSegment;
        this.cities = cities;
        this.roads = roads;
        this.fields = fields;
        for (Segment s : citySegments) {
            s.setTile(this);
        }
        for (Segment s : roadSegments) {
            s.setTile(this);
        }
        for (Segment s : fieldSegments) {
            s.setTile(this);
        }
        if (cloisterSegment != null) {
            cloisterSegment.setTile(this);
        }
    }

    // for testing only
    Tile(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Tile setX(int x) {
        this.x = x;
        return this;
    }

    public Tile setY(int y) {
        this.y = y;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Tile setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }

    public Segment getCitySegmentOfDirection(int direction) {
        int index = cities[direction];
        return (index != -1) ? citySegments[index] : null;
    }

    public Segment getRoadSegmentOfDirection(int direction) {
        int index = roads[direction];
        return (index != -1) ? roadSegments[index] : null;
    }

    public Segment getFieldSegmentOfDirection(int direction) {
        int index = fields[direction];
        return (index != -1) ? fieldSegments[index] : null;
    }

    Segment[] getCitySegments() {
        return citySegments;
    }

    Segment[] getRoadSegments() {
        return roadSegments;
    }

    Segment[] getFieldSegments() {
        return fieldSegments;
    }

    Segment getCloisterSegment() {
        return cloisterSegment;
    }

    public int getRotation() {
        return rotation;
    }

    public BorderType getBorderType(int d) {
        return borderTypes[d];
    }

    // topTileをこのタイルの上に回転rした状態で置くことができるかどうか
    public boolean canTopAdjacentWith(Tile topTile, int r) {
        return canAdjacentWith(0, topTile, r);
    }

    // rightTileをこのタイルの右に回転rした状態で置くことができるかどうか
    public boolean canRightAdjacentWith(Tile rightTile, int r) {
        return canAdjacentWith(1, rightTile, r);
    }

    // bottomTileをこのタイルの下に回転rした状態で置くことができるかどうか
    public boolean canBottomAdjacentWith(Tile bottomTile, int r) {
        return canAdjacentWith(2, bottomTile, r);
    }

    // leftTileをこのタイルの左に回転rした状態で置くことができるかどうか
    public boolean canLeftAdjacentWith(Tile leftTile, int r) {
        return canAdjacentWith(3, leftTile, r);
    }

    private boolean canAdjacentWith(int d, Tile tile, int r) {
        BorderType myBorderType = getBorderType((d + 4 - rotation) % 4);
        BorderType yourBorderType = tile.getBorderType((d + 2 + 4 - r) % 4);
        return myBorderType == yourBorderType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tile[")
            .append("id=").append(id).append(", ")
            .append("name=").append(name).append(", ")
            .append("x=").append(x).append(", ")
            .append("y=").append(y).append(", ")
            .append("rotation=").append(rotation)
            .append("]");
        return sb.toString();
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder("Tile[")
            .append("id=").append(id).append(", ")
            .append("name=").append(name).append(", ")
            .append("x=").append(x).append(", ")
            .append("y=").append(y).append(", ")
            .append("rotation=").append(rotation).append(", ")
            .append("borderTypes=")
            .append(borderTypes[0]).append(":")
            .append(borderTypes[1]).append(":")
            .append(borderTypes[2]).append(":")
            .append(borderTypes[3])
            .append("]");
        return sb.toString();
    }
}
