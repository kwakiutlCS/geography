package com.example.simulation.alertcontroller;

import com.example.geometry.Polygon;

public class Alert {
	
	private static int nextId = 1;
	public final long id;
	public final Polygon polygon;
	
	public Alert(Polygon polygon) {
		this.id = nextId++;
		this.polygon = polygon;
	}
}
