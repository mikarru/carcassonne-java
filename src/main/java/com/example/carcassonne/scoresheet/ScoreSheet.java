package com.example.carcassonne.scoresheet;

import static com.example.carcassonne.board.Segment.SegmentType;


public class ScoreSheet {
    public static enum ActionType {
	FIRST_PLACE, PLAYER_PLACE, SKIP;

        public static ActionType fromString(String s) {
            if (s.equals("firstPlace")) {
                return FIRST_PLACE;
            } else if (s.equals("playerPlace")) {
                return PLAYER_PLACE;
            } else if (s.equals("skip")) {
                return SKIP;
            } else {
                throw new IllegalArgumentException();
            }
        }
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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("TilePlacement[x=").append(x).append(", y=").append(y)
                .append(", rotation=").append(rotation).append("]");
            return sb.toString();
        }
    }

    public static class MeeplePlacement {
	public SegmentType segmentType;
	public int segmentIndex;
	
	public MeeplePlacement(SegmentType type, int index) {
	    this.segmentType = type;
	    this.segmentIndex = segmentIndex;
	}

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MeeplePlacement[type=").append(segmentType)
                .append(", index=").append(segmentIndex).append("]");
            return sb.toString();
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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Placement[\n")
                .append("  action=").append(action).append(",\n")
                .append("  tileName=").append(tileName).append(",\n");
            if (playerName != null) {
                sb.append("  player=").append(playerName).append(",\n");
            }
            if (tilePlacement != null) {
                sb.append("  tilePlacement=").append(tilePlacement).append(",\n");
            }
            if (meeplePlacement != null) {
                sb.append("  meeplePlacement=").append(meeplePlacement).append(",\n");
            }
            sb.append("]");
            return sb.toString();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Placement p : placements) {
            sb.append(p).append("\n");
        }
        return sb.toString();
    }
}
