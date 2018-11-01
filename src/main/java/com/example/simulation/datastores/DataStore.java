package com.example.simulation.datastores;

public interface DataStore {

	void subscribe(String subscription, String id);

	void unsubscribe(String subscription, String id);

	long getSubscriptionCounter();

	long getUnsubscriptionCounter();
}
