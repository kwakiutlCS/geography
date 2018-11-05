package com.example.simulation.datastores;

import java.util.Map;
import java.util.Set;

public interface DataStore {

	void subscribe(String subscription, String id);

	void unsubscribe(String subscription, String id);

	long getSubscriptionCounter();

	long getUnsubscriptionCounter();
	
	void publish(long id, Set<String> geohashes);

	long getAlertsCounter();

	Map<String, Map<Long, Long>> getLastAlertData();
	
	void clearLastAlertData();

	long getAlertsSended();
	
	boolean enlarged();
}
