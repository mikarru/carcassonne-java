package com.example.carcassonne;

import static com.example.carcassonne.BorderType.CITY;
import static com.example.carcassonne.BorderType.FIELD;
import static com.example.carcassonne.BorderType.ROAD;


public class Tiles {
    public static Tile newA(int id) {
        final String name = "A";
        BorderType[] borderTypes = new BorderType[] {
            FIELD, FIELD, ROAD, FIELD
        };
        Segment[] citySegments = new Segment[0];
        Segment[] roadSegments = new Segment[] {Segment.newRoad(0)};
        Segment[] fieldSegments = new Segment[] {Segment.newField(0)};
        Segment cloisterSegment = Segment.newCloister(0);
        int[] cities = new int[] {-1, -1, -1, -1};
        int[] roads = new int[] {-1, -1, 0, -1};
        int[] fields = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
        return new Tile(id, name, borderTypes,
                citySegments, roadSegments, fieldSegments, cloisterSegment,
                cities, roads, fields);
    }

    public static Tile newB(int id) {
        final String name = "B";
        BorderType[] borderTypes = new BorderType[] {
            FIELD, FIELD, FIELD, FIELD
        };
        Segment[] citySegments = new Segment[0];
        Segment[] roadSegments = new Segment[0];
        Segment[] fieldSegments = new Segment[] {Segment.newField(0)};
        Segment cloisterSegment = Segment.newCloister(0);
        int[] cities = new int[] {-1, -1, -1, -1};
        int[] roads = new int[] {-1, -1, -1, -1};
        int[] fields = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
        return new Tile(id, name, borderTypes,
                citySegments, roadSegments, fieldSegments, cloisterSegment,
                cities, roads, fields);
    }

    public static Tile newC(int id) {
        final String name = "C";
        BorderType[] borderTypes = new BorderType[] {
            CITY, CITY, CITY, CITY
        };
        Segment[] citySegments = new Segment[] {Segment.newCity(0, true)};
        Segment[] roadSegments = new Segment[0];
        Segment[] fieldSegments = new Segment[0];
        Segment cloisterSegment = null;
        int[] cities = new int[] {0, 0, 0, 0};
        int[] roads = new int[] {-1, -1, -1, -1};
        int[] fields = new int[] {-1, -1, -1, -1, -1, -1, -1, -1};
        return new Tile(id, name, borderTypes,
                citySegments, roadSegments, fieldSegments, cloisterSegment,
                cities, roads, fields);
    }
    
    public static Tile newD(int id) {
        final String name = "D";
        BorderType[] borderTypes = new BorderType[] {
            ROAD, CITY, ROAD, FIELD
        };
        Segment[] citySegments = new Segment[] {Segment.newCity(0, false)};
        Segment[] roadSegments = new Segment[] {Segment.newRoad(0)};
        Segment[] fieldSegments = new Segment[] {Segment.newField(0), Segment.newField(1)};
        Segment cloisterSegment = null;
        int[] cities = new int[] {-1, 0, -1, -1};
        int[] roads = new int[] {0, -1, 0, -1};
        int[] fields = new int[] {0, 1, -1, -1, 1, 0, 0, 0};
        return new Tile(id, name, borderTypes,
                citySegments, roadSegments, fieldSegments, cloisterSegment,
                cities, roads, fields);
    }

    public static Tile newE(int id) {
        return null;
    }

    public static Tile newF(int id) {
        return null;
    }

    public static Tile newG(int id) {
        return null;
    }

    public static Tile newH(int id) {
        return null;
    }

    public static Tile newI(int id) {
        return null;
    }

    public static Tile newJ(int id) {
        return null;
    }

    public static Tile newK(int id) {
        return null;
    }

    public static Tile newL(int id) {
        return null;
    }

    public static Tile newM(int id) {
        return null;
    }

    public static Tile newN(int id) {
        return null;
    }

    public static Tile newO(int id) {
        return null;
    }

    public static Tile newP(int id) {
        return null;
    }

    public static Tile newQ(int id) {
        return null;
    }

    public static Tile newR(int id) {
        return null;
    }

    public static Tile newS(int id) {
        return null;
    }

    public static Tile newT(int id) {
        return null;
    }
    
    public static Tile newU(int id) {
        return null;
    }
    
    public static Tile newV(int id) {
        return null;
    }
    
    public static Tile newW(int id) {
        return null;
    }

    public static Tile newX(int id) {
        return null;
    }

    public static Tile newFromName(int id, String name) {
        return null;
    }
}
