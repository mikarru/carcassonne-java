package com.example.carcassonne.scoresheet;


public class InvalidScoreSheetFormatException extends Exception {
    public InvalidScoreSheetFormatException(String message) {
        super(message);
    }

    public InvalidScoreSheetFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
