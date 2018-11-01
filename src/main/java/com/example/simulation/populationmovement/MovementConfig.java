package com.example.simulation.populationmovement;

public class MovementConfig {

	private int maxDistance;
	private int minDistance;
	private int thresholdDistance;
	private double maxSpeed;
	private double minSpeed;
	private double randomWalk;
	private double step;
	
	public MovementConfig(int maxDistance, int minDistance, int thresholdDistance, double maxSpeed,
			double minSpeed, double randomWalk, double step) {
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.randomWalk = randomWalk;
		this.step = step;
		this.maxDistance = maxDistance;
		this.minDistance = minDistance;
		this.thresholdDistance = thresholdDistance;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public double getMinSpeed() {
		return minSpeed;
	}

	public double getRandomWalk() {
		return randomWalk;
	}

	public double getStep() {
		return step;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public int getMinDistance() {
		return minDistance;
	}

	public int getThresholdDistance() {
		return thresholdDistance;
	}
}
