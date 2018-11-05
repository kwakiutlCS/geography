package com.example.simulation.datastores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AlternativeStore implements DataStore {

	private long subscriptionCounter = 0;
	private long unsubscriptionCounter = 0;
	private long alertsReceived = 0;
	private long alertsSended = 0;
	
	private Map<String, Set<String>> subscriptions = new HashMap<>();
	
	private Map<String, Map<Long, Long>> lastAlertData = new HashMap<>();
	
	@Override
	public void subscribe(String subscription, String id) {
		subscriptionCounter++;
		
		Set<String> sub = subscriptions.get(id);
		if (sub == null) {
			sub = new HashSet<String>();
			subscriptions.put(id, sub);
		}
		
		sub.add(subscription);
	}

	@Override
	public void unsubscribe(String subscription, String id) {
		unsubscriptionCounter++;
		
		Set<String> sub = subscriptions.get(id);
		if (sub != null) {
			sub.remove(subscription);
		}
	}

	@Override
	public long getSubscriptionCounter() {
		return subscriptionCounter;
	}

	@Override
	public long getUnsubscriptionCounter() {
		return unsubscriptionCounter;
	}
	
	@Override
	public long getAlertsCounter() {
		return alertsReceived;
	}
	
	@Override
	public long getAlertsSended() {
		return alertsSended;
	}

	@Override
	public void publish(long id, Set<String> geohashes) {
		alertsReceived += geohashes.size();
		
		for (String vehicleId : subscriptions.keySet()) {
			if (!lastAlertData.containsKey(vehicleId)) {
				lastAlertData.put(vehicleId, new HashMap<>());
			}
			Map<Long, Long> vehicleData = lastAlertData.get(vehicleId);
			
			long counter = 0;
			for (String subscription : subscriptions.get(vehicleId)) {
				if (geohashes.contains(subscription)) {
					counter++;
				}
			}
			
			alertsSended += counter;
			vehicleData.put(id, counter);
		}
	}
	
	@Override
	public Map<String, Map<Long, Long>> getLastAlertData() {
		return lastAlertData;
	}
	
	@Override
	public void clearLastAlertData() {
		lastAlertData.clear();
	}

	@Override
	public boolean enlarged() {
		return true;
	}
}
