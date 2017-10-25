package com.example.carcassonne;


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
        this.citySegments = citySegments;
        this.roadSegments = roadSegments;
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

    public Segment[] getCitySegments() {
        return citySegments;
    }

    public Segment[] getRoadSegments() {
        return roadSegments;
    }

    public Segment[] getFieldSegments() {
        return fieldSegments;
    }

    public Segment getCloisterSegment() {
        return cloisterSegment;
    }

    public int getRotation() {
        return rotation;
    }

    public BorderType getBorderType(int d) {
        return borderTypes[d];
    }

    // topTileをこのタイルの上に回転rした状態でで置くことができるかどうか
    public boolean canTopAdjacentWith(Tile topTile, int r) {
        // TODO
        return false;
    }

    // bottomTileをこのタイルの下に回転rした状態で置くことができるかどうか
    public boolean canBottomAdjacentWith(Tile bottomTile, int r) {
        // TODO
        return false;
    }

    // rightTileをこのタイルの右に回転rした状態で置くことができるかどうか
    public boolean canRightAdjacentWith(Tile rightTile, int r) {
        // TODO
        return false;
    }

    // leftTileをこのタイルの左に回転rした状態で置くことができるかどうか
    public boolean canLeftAdjacentWith(Tile leftTile, int r) {
        // TODO
        return false;
    }
}
