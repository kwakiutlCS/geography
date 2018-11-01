package com.example.geometry;

import java.util.Random;

public class Movement {
	
	private double longitudeDelta;
	private double latitudeDelta;
	private int direction;
	
	public Movement(double longitudeDelta, double latitudeDelta) {
		this.longitudeDelta = longitudeDelta;
		this.latitudeDelta = latitudeDelta;
	}
	
	public double getLongitudeDelta() {
		return longitudeDelta;
	}
	
	public double getLatitudeDelta() {
		return latitudeDelta;
	}
	
	public static Movement generateRandomMovement(double distance) {
		Random rand = new Random();
		int direction = rand.nextInt(360);
		
		Movement movement = generateMovement(distance, direction);
		movement.direction = direction;
		
		return movement;
	}
	
	public static Movement generateMovement(double distance, double direction) {
		// this is a rough movement, corresponding to ~1km
		double latRatio = 0.009;
		double lonRatio = 0.0115;
		
		return new Movement(distance*lonRatio*Math.sin(direction), distance*latRatio*Math.cos(direction));
	}

	public Movement scale(double scale) {
		return new Movement(longitudeDelta*scale, latitudeDelta*scale);
	}
	
	public Movement plus(Movement other) {
		return new Movement(longitudeDelta+other.getLongitudeDelta(), latitudeDelta+other.getLatitudeDelta());
	}
	
	public Movement scaleTo(double distance) {
		// this is a rough movement, corresponding to ~1km
		double latRatio = 0.009;
		double lonRatio = 0.0115;
		
		double xDelta = latitudeDelta/latRatio;
		double yDelta = longitudeDelta/lonRatio;
		
		return scale(distance/Math.sqrt(Math.pow(xDelta, 2)+Math.pow(yDelta, 2)));
	}
	
	public int getDirection() {
		return direction;
	}
}
