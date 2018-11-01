package com.example.simulation.populationmovement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.geometry.Point;
import com.example.simulation.datastores.DataStore;
import com.example.simulation.datastores.DefaultStore;

public class PopulationMovement {

	public static void execute(List<Point> population) {
		
		MovementConfig config = new MovementConfig(90, 10, 40, 80, 40, 0.3, 1/30.);
		
		DataStore store0 = new DefaultStore();
		DataStore store1 = new DefaultStore();
		DataStore store2 = new DefaultStore();
		
		List<Vehicle> vehicles = population.stream().map(p -> {
			List<VehicleController> controllers = Arrays.asList(
				new VehicleController(store0, 0, 0),
				new VehicleController(store1, 0, 1),
				new VehicleController(store2, 1, 2)
			);
			
			return new Vehicle(p, controllers, config);
		}).collect(Collectors.toList());
		
		double t = 0;
		double limit = 16;
		
		while (t < limit) {
			vehicles.forEach(Vehicle::executeStep);
			
			t += config.getStep();
		}
		
		System.out.println(store0.getSubscriptionCounter()+ " - " +store0.getUnsubscriptionCounter());
		System.out.println(store1.getSubscriptionCounter()+ " - " +store1.getUnsubscriptionCounter());
		System.out.println(store2.getSubscriptionCounter()+ " - " +store2.getUnsubscriptionCounter());
	}
}