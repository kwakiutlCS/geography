package com.example.simulation.datastores;

import java.util.Map;

public class StrategyComparator {

	private DataStore base;
	private DataStore challenger;
	
	private long baseTotalAlerts = 0;
	private long baseUniqueAlerts = 0;
	private long challengerTotalAlerts = 0;
	private long challengerUniqueAlerts = 0;
	
	private long baseOnlyAlerts = 0;
	private long challengerOnlyAlerts = 0;
	
	public StrategyComparator(DataStore base, DataStore challenger) {
		this.base = base;
		this.challenger = challenger;
	}
	
	public void analyse() {
		for (String vehicleId : base.getLastAlertData().keySet()) {
			Map<Long, Long> baseData = base.getLastAlertData().get(vehicleId);
			Map<Long, Long> challengerData = challenger.getLastAlertData().get(vehicleId);
			
			for (Long alertId : baseData.keySet()) {
				long baseCounter = baseData.get(alertId);
				long challengerCounter= challengerData.get(alertId);
				
				baseTotalAlerts += baseCounter;
				challengerTotalAlerts += challengerCounter;
				baseUniqueAlerts += baseCounter > 0 ? 1 : 0;
				challengerUniqueAlerts += challengerCounter > 0 ? 1 : 0;
				baseOnlyAlerts += challengerCounter == 0 && baseCounter > 0 ? 1 : 0;
				challengerOnlyAlerts += challengerCounter > 0 && baseCounter == 0 ? 1 : 0;
			}
		}
	}

	public long getBaseTotalAlerts() {
		return baseTotalAlerts;
	}

	public long getBaseUniqueAlerts() {
		return baseUniqueAlerts;
	}

	public long getChallengerTotalAlerts() {
		return challengerTotalAlerts;
	}

	public long getChallengerUniqueAlerts() {
		return challengerUniqueAlerts;
	}

	public long getBaseOnlyAlerts() {
		return baseOnlyAlerts;
	}

	public long getChallengerOnlyAlerts() {
		return challengerOnlyAlerts;
	}
}
