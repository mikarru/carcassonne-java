package com.example.carcassonne;


public class Pair<A, B> {
    private final A a;
    private final B b;
    
    public Pair(A a, B b) {
	this.a = a;
	this.b = b;
    }

    public A getFirst() {
	return a;
    }

    public B getSecond() {
	return b;
    }

    @Override
    public int hashCode() {
	return getFirst().hashCode() * 31 + getSecond().hashCode();
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o instanceof Pair) {
	    Pair that = (Pair) o;
	    return this.getFirst().equals(that.getFirst()) &&
		this.getSecond().equals(that.getSecond());
	}
	return false;
    }
}
