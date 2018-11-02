package com.example.simulation.populationmovement;

public class MovementConfig {

	public final double distMean;
	public final double distDev;
	public final double speedMean;
	public final double speedDev;
	public final double randomWalk;
	public final double step;
	
	public MovementConfig(double distMean, double distDev, double speedMean,
			double speedDev, double randomWalk, double step) {
		this.distMean = distMean;
		this.distDev = distDev;
		this.randomWalk = randomWalk;
		this.step = step;
		this.speedMean = speedMean;
		this.speedDev = speedDev;
	}
}
