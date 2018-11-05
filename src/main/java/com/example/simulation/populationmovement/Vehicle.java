package com.example.simulation.populationmovement;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.example.geometry.GeoUtils;
import com.example.geometry.Movement;
import com.example.geometry.Point;

public class Vehicle {

	public final String id = UUID.randomUUID().toString();
	
	private Point position;
	private List<VehicleController> controllers;	
	private MovementConfig config;
	private Set<String> domain;
	
	private Deque<Movement> executedMovements = new LinkedList<>();
	private double totalDistance;
	private double remainingDistance;
	private double stepDistance;
		
	public Vehicle(Point position, List<VehicleController> controllers, MovementConfig config, Set<String> domain) {
		this.position = position;
		this.controllers = controllers;
		this.config = config;
		this.domain = domain;
		
		init();
		startControllers();
	}
	
	public void executeStep() {
		if (remainingDistance > 0) {
			Point newPosition = generateNewPosition();
			remainingDistance -= GeoUtils.distance(newPosition, position);
			position = newPosition;
			
			controllers.stream().forEach(vc -> vc.moveTo(position, id));
			
			//System.out.println("Car moved - "+position);
			
			if (remainingDistance <= 0) {
				stopControllers();
			}
		}
	}
	
	public void revertStep() {
		if (!executedMovements.isEmpty()) {
			position = position.execute(executedMovements.pop().scale(-1));
			controllers.stream().forEach(vc -> vc.moveTo(position, id));
			
			//System.out.println("Car moved - "+position);
			
			if (executedMovements.isEmpty()) {
				stopControllers();
			}
		}
	}
	
	private Point generateNewPosition() {
		Movement movement = null;
		double randomWalkEnhancer = 1.1;
		Point candidatePosition = null;
		int iteration = 0;
		
		while (candidatePosition == null || !domain.contains(GeoUtils.encode(candidatePosition))) {
			
			if (executedMovements.isEmpty()) {
				movement = Movement.generateRandomMovement(stepDistance);
			} else {
				double randomWalkModifier = Math.min(1, config.randomWalk*Math.pow(randomWalkEnhancer, iteration));
				movement = executedMovements.peek()
						.scale(1-randomWalkModifier)
						.plus(Movement.generateRandomMovement(stepDistance).scale(randomWalkModifier))
						.scaleTo(stepDistance);
			}
			
			iteration++;
			candidatePosition = position.execute(movement);
		}
		
		executedMovements.push(movement);
		return candidatePosition;
	}
	
	private void init() {
		//System.out.println("Car started - "+position);
		
		Random rand = new Random();
		
		this.totalDistance = rand.nextGaussian()*config.distDev + config.distMean;
		this.remainingDistance = totalDistance;
		
		double speed = Math.max(10, rand.nextGaussian()*config.speedDev + config.speedMean);
		this.stepDistance = speed * config.step;
	}
	
	public void startControllers() {
		controllers.forEach(c -> c.start(position, id));
	}
	
	public void stopControllers() {
		controllers.forEach(c -> c.stop(id));
		
		//System.out.println("car stopped");
	}
	
	public Point getPosition() {
		return position;
	}
}
