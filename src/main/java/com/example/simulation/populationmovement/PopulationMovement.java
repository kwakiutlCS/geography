package com.example.simulation.populationmovement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.geometry.Point;
import com.example.geometry.Polygon.PolygonBuilder;
import com.example.simulation.alertcontroller.Alert;
import com.example.simulation.alertcontroller.AlertController;
import com.example.simulation.datastores.AlternativeStore;
import com.example.simulation.datastores.DataStore;
import com.example.simulation.datastores.DefaultStore;
import com.example.simulation.datastores.StrategyComparator;

public class PopulationMovement {

	public static void execute(List<Point> population, Set<String> squares, Path filename) throws IOException {
		
		MovementConfig config = new MovementConfig(30, 3, 60, 5, 0.2, 1/30.);
		
//		DataStore store0 = new DefaultStore();
//		DataStore store1 = new DefaultStore();
//		DataStore store2 = new DefaultStore();
		DataStore store3 = new DefaultStore();
		DataStore store4 = new DefaultStore(true);
		
		StrategyComparator sc = new StrategyComparator(store3, store4);
		
		List<Alert> alerts = Files.lines(filename)
				.map(line -> {
					String[] items = line.trim().split(",");
					PolygonBuilder pb = PolygonBuilder.create();
					for (String item : items) {
						String[] coords = item.split(" ");
						pb.add(Double.valueOf(coords[0]), Double.valueOf(coords[1]));
					}
					
					return pb.close();
				})
				.map(Alert::new)
				.collect(Collectors.toList());
		
		AlertController alertController = new AlertController(Arrays.asList(store3, store4), config);
		alertController.setAlerts(alerts);
		
		List<Vehicle> vehicles = population.stream().map(p -> {
			List<VehicleController> controllers = Arrays.asList(
//				new VehicleController(store0, 0, 0),
//				new VehicleController(store1, 0, 1),
//				new VehicleController(store2, 1, 1),
				new VehicleController(store3, 1, 2),
				new VehicleController(store4, 1, 0)
			);
			
			return new Vehicle(p, controllers, config, squares);
			
		}).collect(Collectors.toList());
		
		double t = 0;
		double limit = 24;
		double lastAlert = 0;
		
		while (t < limit) {
			vehicles.forEach(Vehicle::executeStep);
			
			t += config.step;
			lastAlert += config.step;
			
			if (lastAlert > 0.1667) {
				lastAlert = 0;
				alertController.executeStep();
				sc.analyse();
			}
			
		}
		
//		vehicles.forEach(Vehicle::startControllers);
//		while (t < limit) {
//			vehicles.forEach(Vehicle::revertStep);
//			
//			t += config.step;
//			lastAlert += config.step;
//			
//			if (lastAlert > 0.1667) {
//				lastAlert = 0;
//				alertController.executeStep();
//				sc.analyse();
//			}
//		}
		
		System.out.println("\n\n"+filename);
		System.out.println("SUBSCRIPTIONS");
//		System.out.println(store0.getSubscriptionCounter()+ " - " +store0.getUnsubscriptionCounter());
//		System.out.println(store1.getSubscriptionCounter()+ " - " +store1.getUnsubscriptionCounter());
//		System.out.println(store2.getSubscriptionCounter()+ " - " +store2.getUnsubscriptionCounter());
		System.out.println(store3.getSubscriptionCounter()+ " - " +store3.getUnsubscriptionCounter());
		System.out.println(store4.getSubscriptionCounter()+ " - " +store4.getUnsubscriptionCounter());
		
		System.out.println("\nALERTS");
		
//		System.out.println(store0.getAlertsCounter()+ " - "+store0.getAlertsSended());
//		System.out.println(store1.getAlertsCounter()+ " - "+store1.getAlertsSended());
//		System.out.println(store2.getAlertsCounter()+ " - "+store2.getAlertsSended());
		System.out.println(store3.getAlertsCounter()+ " - "+store3.getAlertsSended());
		System.out.println(store4.getAlertsCounter()+ " - "+store4.getAlertsSended());
		
		System.out.println("\nUNIQUE vs TOTAL");
		System.out.println(sc.getBaseUniqueAlerts()+" - "+sc.getBaseTotalAlerts()+ " - "+sc.getBaseOnlyAlerts());
		System.out.println(sc.getChallengerUniqueAlerts()+ " - "+sc.getChallengerTotalAlerts() + " - "+sc.getChallengerOnlyAlerts());
	}
}
