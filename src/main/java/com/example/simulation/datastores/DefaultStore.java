package com.example.simulation.datastores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultStore implements DataStore {

	private long subscriptionCounter = 0;
	private long unsubscriptionCounter = 0;
	
	private Map<String, Set<String>> subscriptions = new HashMap<>();
	
	public void subscribe(String subscription, String id) {
		subscriptionCounter++;
		
		Set<String> sub = subscriptions.get(id);
		if (sub == null) {
			sub = new HashSet<String>();
			subscriptions.put(id, sub);
		}
		
		sub.add(subscription);
	}

	public void unsubscribe(String subscription, String id) {
		unsubscriptionCounter++;
		
		Set<String> sub = subscriptions.get(id);
		if (sub != null) {
			sub.remove(id);
		}
	}

	public long getSubscriptionCounter() {
		return subscriptionCounter;
	}

	public long getUnsubscriptionCounter() {
		return unsubscriptionCounter;
	}
}
