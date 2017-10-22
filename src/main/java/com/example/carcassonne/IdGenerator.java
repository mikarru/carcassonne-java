package com.example.carcassonne;

public class IdGenerator {
    private int id = 0;

    public int newId() {
	return id++;
    }
}
