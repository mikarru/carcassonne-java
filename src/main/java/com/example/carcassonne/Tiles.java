package com.example.carcassonne;

import com.example.carcassonne.board.BorderType;
import static com.example.carcassonne.board.BorderType.CITY;
import static com.example.carcassonne.board.BorderType.FIELD;
import static com.example.carcassonne.board.BorderType.ROAD;
import com.example.carcassonne.board.Segment;
import com.example.carcassonne.board.Tile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Tiles {
    static class TileParts {
        public String name;
        public int[] cities;
        public int[] roads;
        public int[] fields;
        public boolean hasCloister;
        public boolean[] pennants;

        public TileParts(String name, int[] cities, int[] roads, int[] fields,
            boolean hasCloister, boolean[] pennants) {
            this.name = name;
            this.cities = cities;
            this.roads = roads;
            this.fields = fields;
            this.hasCloister = hasCloister;
            this.pennants = pennants;
        }
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, TileParts> PARTS_MAP;

    private static final String RESOURCE_NAME = "tile/tiles.json";
    static {
        InputStream in = Tiles.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
        if (in == null) {
            throw new RuntimeException("Can't find tile definition file at " + RESOURCE_NAME);
        }
        try {
            PARTS_MAP = readTileDefinitions(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static Map<String, TileParts> readTileDefinitions(InputStream in) throws IOException {
        Map<String, TileParts> map = new HashMap<>();
        JsonNode rootNode = OBJECT_MAPPER.readTree(in);
        Iterator<JsonNode> iter = rootNode.elements();
        while (iter.hasNext()) {
            JsonNode definition = iter.next();
            String name = definition.get("name").asText();
            int[] cities = getAsIntArray(definition, "cities");
            int[] roads = getAsIntArray(definition, "roads");
            int[] fields = getAsIntArray(definition, "fields");
            boolean hasCloister = definition.get("hasCloister").asBoolean();
            boolean[] pennants = getAsBooleanArray(definition, "pennants");
            map.put(name, new TileParts(name, cities, roads, fields, hasCloister, pennants));
        }
        return map;
    }

    private static int[] getAsIntArray(JsonNode node, String field) {
        JsonNode child = node.get(field);
        List<Integer> list = new ArrayList<>();
        Iterator<JsonNode> iter = child.elements();
        while (iter.hasNext()) {
            JsonNode n = iter.next();
            list.add(n.asInt());
        }
        int[] a = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i);
        }
        return a;
    }

    private static boolean[] getAsBooleanArray(JsonNode node, String field) {
        JsonNode child = node.get(field);
        List<Boolean> list = new ArrayList<>();
        Iterator<JsonNode> iter = child.elements();
        while (iter.hasNext()) {
            JsonNode n = iter.next();
            list.add(n.asBoolean());
        }
        boolean[] a = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i);
        }
        return a;
    }

    public static Tile newFromName(int id, String name) {
        TileParts parts = PARTS_MAP.get(name);
        if (parts == null) {
            throw new IllegalArgumentException("Unknown tile name '" + name + "'");
        }
        return constructTile(id, parts);
    }

    static Tile constructTile(int id, TileParts parts) {
        int citySegmentN = 0;
        for (int city : parts.cities) {
            if (citySegmentN < city + 1) {
                citySegmentN = city + 1;
            }
        }
        Segment[] citySegments = new Segment[citySegmentN];
        for (int i = 0; i < citySegmentN; i++) {
            citySegments[i] = Segment.newCity(i, parts.pennants[i]);
        }

        int roadSegmentN = 0;
        for (int road : parts.roads) {
            if (roadSegmentN < road + 1) {
                roadSegmentN = road + 1;
            }
        }
        Segment[] roadSegments = new Segment[roadSegmentN];
        for (int i = 0; i < roadSegmentN; i++) {
            roadSegments[i] = Segment.newRoad(i);
        }

        int fieldSegmentN = 0;
        for (int field : parts.fields) {
            if (fieldSegmentN < field + 1) {
                fieldSegmentN = field + 1;
            }
        }
        Segment[] fieldSegments = new Segment[fieldSegmentN];
        for (int i = 0; i < fieldSegmentN; i++) {
            fieldSegments[i] = Segment.newField(i);
        }

        Segment cloisterSegment = parts.hasCloister ? Segment.newCloister(0) : null;

        BorderType[] borderTypes = new BorderType[4];
        for (int d = 0; d < 4; d++) {
            if (parts.cities[d] != -1) {
                borderTypes[d] = CITY;
            } else if (parts.roads[d] != -1) {
                borderTypes[d] = ROAD;
            } else {
                borderTypes[d] = FIELD;
            }
        }
        return new Tile(id, parts.name, borderTypes,
                citySegments, roadSegments, fieldSegments, cloisterSegment,
                parts.cities, parts.roads, parts.fields);
    }
}
