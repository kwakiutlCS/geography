package com.example.simulation.populationmovement;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.geometry.GeoUtils;
import com.example.geometry.Point;
import com.example.simulation.datastores.DataStore;

public class VehicleController {

	private String id = UUID.randomUUID().toString();
	
	private DataStore store;
	
	private int threshold;
	private int subscription;
	
	private Set<String> subscriptions = new HashSet<>();
	
	private double maxLat;
	private double minLat;
	private double maxLon;
	private double minLon;	
	
	
	public VehicleController(DataStore store, int threshold, int subscription) {
		this.store = store;
		this.threshold = threshold;
		this.subscription = subscription;
	}
	
	public void moveTo(Point destination) {
		if (destination.getCoordinates()[0] > maxLon || destination.getCoordinates()[0] < minLon ||
			destination.getCoordinates()[1] > maxLat || destination.getCoordinates()[1] < minLat) {
			
			String currentGeohash = GeoUtils.encode(destination);
			
			calculateNewThreshold(currentGeohash);
			
			calculateNewSubscriptions(currentGeohash);
		}
	}
	
	private void calculateNewThreshold(String geohash) {
		String neGeohash = GeoUtils.geoHashLine(threshold, geohash, GeoUtils::northeast)
				.stream()
				.reduce(geohash, (a, b) -> b);
		
		String swGeohash = GeoUtils.geoHashLine(threshold, geohash, GeoUtils::southwest)
				.stream()
				.reduce(geohash, (a, b) -> b);

		double[] neBB = GeoUtils.decode_bbox(neGeohash);
		double[] swBB = GeoUtils.decode_bbox(swGeohash);
		
		maxLon = neBB[3];
		minLon = swBB[2];
		maxLat = neBB[1];
		minLat = swBB[0];
	}
	
	private void calculateNewSubscriptions(String geohash) {
		Set<String> newSubscriptions = GeoUtils.geoHashGrid(subscription, geohash);
		
		subscriptions.forEach(s -> {
			if (!newSubscriptions.contains(s)) {
				store.unsubscribe(s, id);
			}
		});
		
		newSubscriptions.forEach(s -> {
			if (!subscriptions.contains(s)) {
				store.subscribe(s, id);
			}
		});
		
		subscriptions = newSubscriptions;
	}

	public void start(Point position) {
		String currentGeohash = GeoUtils.encode(position);
		calculateNewThreshold(currentGeohash);
		calculateNewSubscriptions(currentGeohash);
	}
	
	public void stop() {
		subscriptions.forEach(s -> store.unsubscribe(s, id));
		subscriptions.clear();
	}
}
