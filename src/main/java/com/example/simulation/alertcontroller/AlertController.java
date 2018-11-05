package com.example.simulation.alertcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.geometry.GeoUtils;
import com.example.geometry.Polygon;
import com.example.simulation.datastores.DataStore;
import com.example.simulation.populationmovement.MovementConfig;

public class AlertController {

	private List<DataStore> stores;
	private List<Alert> alerts = new ArrayList<>();
	private MovementConfig config;
	
	private Map<Long, Set<String>> hashes = new HashMap<>();
	private Map<Long, Set<String>> enlargedHashes = new HashMap<>();
	
	public AlertController(List<DataStore> stores, MovementConfig config) {
		this.stores = stores;
		this.config = config;
	}
	
	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}
	
	public void executeStep() {
		stores.forEach(s -> {
			alerts.forEach(a -> {
				s.publish(a.id, getHashes(a.polygon, a.id, s.enlarged()));
			});
		});
	}
	
	private Set<String> getHashes(Polygon polygon, long id, boolean enlarged) {
		if (!enlarged) {
			if (!hashes.containsKey(id)) {
				hashes.put(id, GeoUtils.geoHashesForPolygon(5, polygon.getCoordinates(), true));
			}
			
			return hashes.get(id);
		} else {
			if (!enlargedHashes.containsKey(id)) {
				enlargedHashes.put(id, GeoUtils.geoHashesForPolygon(5, polygon.getCoordinates(), true).stream()
						.flatMap(s -> GeoUtils.geoHashGrid(2, s).stream()).collect(Collectors.toSet()));
			}
			
			return enlargedHashes.get(id);
		}
	}
}
