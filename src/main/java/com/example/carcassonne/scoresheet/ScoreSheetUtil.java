package com.example.carcassonne.scoresheet;

import static com.example.carcassonne.Segment.SegmentType;
import static com.example.carcassonne.scoresheet.ScoreSheet.ActionType;
import static com.example.carcassonne.scoresheet.ScoreSheet.MeeplePlacement;
import static com.example.carcassonne.scoresheet.ScoreSheet.Placement;
import static com.example.carcassonne.scoresheet.ScoreSheet.Player;
import static com.example.carcassonne.scoresheet.ScoreSheet.TilePlacement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScoreSheetUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static ScoreSheet readFromFile(String path)
        throws IOException, InvalidScoreSheetFormatException {
        File file = new File(path);
        JsonNode rootNode = OBJECT_MAPPER.readTree(file);
        return constractScoreSheet(rootNode);
    }

    public static ScoreSheet constractScoreSheet(JsonNode rootNode)
        throws InvalidScoreSheetFormatException {
        JsonNode infoNode = getNode(rootNode, "info");
        Player[] players = constractPlayers(infoNode);
        JsonNode placementsNode = getNode(rootNode, "placements");
        Placement[] placements = constractPlacements(placementsNode);
        return new ScoreSheet(placements, players);
    }

    private static Player[] constractPlayers(JsonNode infoNode)
        throws InvalidScoreSheetFormatException {
        JsonNode playersNode = getNode(infoNode, "players");
        if (!playersNode.isArray()) {
            throw new InvalidScoreSheetFormatException("value of players field is not array");
        }
        List<Player> players = new ArrayList<>();
        Iterator<JsonNode> iter = playersNode.elements();
        while (iter.hasNext()) {
            JsonNode playerNode = iter.next();
            players.add(new Player(getAsString(playerNode, "name"), getAsString(playerNode, "color")));
        }
        return players.toArray(new Player[players.size()]);
    }

    private static Placement[] constractPlacements(JsonNode placementsNode)
        throws InvalidScoreSheetFormatException {
        if (!placementsNode.isArray()) {
            throw new InvalidScoreSheetFormatException("placementsNode is not array");
        }
        List<Placement> placements = new ArrayList<>();
        Iterator<JsonNode> iter = placementsNode.elements();
        while (iter.hasNext()) {
            JsonNode placementNode = iter.next();
            placements.add(constractPlacement(placementNode));
        }
        return placements.toArray(new Placement[placements.size()]);
    }

    private static Placement constractPlacement(JsonNode placementNode)
        throws InvalidScoreSheetFormatException {
        String action = getAsString(placementNode, "action");
        ActionType actionType = ScoreSheet.ActionType.fromString(action);
        String tileName = getAsString(placementNode, "tile");
        String playerName = null;
        JsonNode playerNode = placementNode.get("player");
        if (playerNode != null) {
            playerName = playerNode.asText();
        }
        JsonNode tilePlacementNode = placementNode.get("tilePlacement");
        TilePlacement tilePlacement = null;
        if (tilePlacementNode != null) {
            tilePlacement = constractTilePlacement(tilePlacementNode);
        }
        JsonNode meeplePlacementNode = placementNode.get("meeplePlacement");
        MeeplePlacement meeplePlacement = null;
        if (meeplePlacementNode != null) {
            meeplePlacement = constractMeeplePlacement(meeplePlacementNode);
        }
        return new Placement(actionType, tileName, playerName, tilePlacement, meeplePlacement);
    }

    private static TilePlacement constractTilePlacement(JsonNode tilePlacementNode) {
        return new TilePlacement(
            getAsInt(tilePlacementNode, "x"),
            getAsInt(tilePlacementNode, "y"),
            getAsInt(tilePlacementNode, "rotation")
        );
    }

    private static MeeplePlacement constractMeeplePlacement(JsonNode meeplePlacementNode)
        throws InvalidScoreSheetFormatException {
        return new MeeplePlacement(
            SegmentType.fromString(getAsString(meeplePlacementNode, "segmentType")),
            getNode(meeplePlacementNode, "segmentIndex").asInt()
        );
    }

    private static JsonNode getNode(JsonNode node, String fieldName)
        throws InvalidScoreSheetFormatException {
        JsonNode child = node.get(fieldName);
        if (child == null) {
            throw new InvalidScoreSheetFormatException("Missing field '" + fieldName + "'");
        }
        return child;
    }

    private static String getAsString(JsonNode node, String fieldName)
        throws InvalidScoreSheetFormatException {
        JsonNode child = getNode(node, fieldName);
        if (!child.isTextual()) {
            throw new InvalidScoreSheetFormatException("value of '" + fieldName + "' is not string");
        }
        return child.asText();
    }

    private static int getAsInt(JsonNode node, String fieldName) {
        return getAsInt(node, fieldName, 0);
    }

    private static int getAsInt(JsonNode node, String fieldName, int defaultVal) {
        JsonNode child = node.get(fieldName);
        if (child == null) {
            return defaultVal;
        }
        return child.asInt();
    }
}
