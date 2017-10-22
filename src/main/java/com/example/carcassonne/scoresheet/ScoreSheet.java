package com.example.carcassonne.scoresheet;

import static com.example.carcassonne.Segment.SegmentType;


public class ScoreSheet {
    public static enum ActionType {
	FIRST_PLACE, PLAYER_PLACE, SKIP
    }


    public static class TilePlacement {
	public int x;
	public int y;
	public int rotation;
	
	public TilePlacement(int x, int y, int rotation) {
	    this.x = x;
	    this.y = y;
	    this.rotation = rotation;
	}
    }

    public static class MeeplePlacement {
	public SegmentType segmentType;
	public int segmentIndex;
	
	public MeeplePlacement(SegmentType type, int index) {
	    this.segmentType = type;
	    this.segmentIndex = segmentIndex;
	}
    }

    public static class Placement {
	public ActionType action;
	public String tileName;
	public String playerName;
	public TilePlacement tilePlacement;
	public MeeplePlacement meeplePlacement;

	public Placement(ActionType action, String tileName, String playerName,
                TilePlacement tilePlacement, MeeplePlacement meeplePlacement) {
	    this.action = action;
	    this.tileName = tileName;
	    this.playerName = playerName;
	    this.tilePlacement = tilePlacement;
	    this.meeplePlacement = meeplePlacement;
	}
    }

    public static class Player {
	public String name;
	public String color;
	public Player(String name, String color) {
	    this.name = name;
	    this.color = color;
	}
    }

    private Placement[] placements;
    private Player[] players;

    public ScoreSheet(Placement[] placements, Player[] players) {
	this.placements = placements;
	this.players = players;
    }

    public Placement[] getPlacements() {
	return placements;
    }

    public Player[] getPlayers() {
	return players;
    }
}
