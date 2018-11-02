package com.example.simulation.populationmovement;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.example.geometry.GeoUtils;
import com.example.geometry.Movement;
import com.example.geometry.Point;

public class Vehicle {

	private Point position;
	private List<VehicleController> controllers;	
	private MovementConfig config;
	
	private Deque<Movement> executedMovements = new LinkedList<>();
	private double totalDistance;
	private double remainingDistance;
	private double stepDistance;
		
	public Vehicle(Point position, List<VehicleController> controllers, MovementConfig config) {
		this.position = position;
		this.controllers = controllers;
		this.config = config;
		
		init();
		startControllers();
	}
	
	public void executeStep() {
		if (remainingDistance > 0) {
			Point initialPosition = position;

			Movement movement;
			
			if (executedMovements.isEmpty()) {
				movement = Movement.generateRandomMovement(stepDistance);
			} else {
				movement = executedMovements.peek()
						.scale(1-config.randomWalk)
						.plus(Movement.generateRandomMovement(stepDistance).scale(config.randomWalk))
						.scaleTo(stepDistance);
			}

			execute(movement);
			remainingDistance -= GeoUtils.distance(initialPosition, position);
			if (remainingDistance <= 0) {
				stopControllers();
			}
		}
	}
	
	private void execute(Movement movement) {
		executedMovements.push(movement);
		
		position = position.execute(movement);
		controllers.stream().forEach(vc -> vc.moveTo(position));
		
		System.out.println("Car moved - "+position);
	}
	
	private void init() {
		System.out.println("Car started - "+position);
		
		Random rand = new Random();
		
		this.totalDistance = rand.nextGaussian()*config.distDev + config.distMean;
		this.remainingDistance = totalDistance;
		
		double speed = rand.nextGaussian()*config.speedDev + config.speedMean;
		this.stepDistance = speed * config.step;
	}
	
	public void startControllers() {
		controllers.forEach(c -> c.start(position));
	}
	
	public void stopControllers() {
		controllers.forEach(VehicleController::stop);
		
		System.out.println("car stopped");
	}
	
	public Point getPosition() {
		return position;
	}
}
