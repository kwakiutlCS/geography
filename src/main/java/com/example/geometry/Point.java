package com.example.geometry;

public class Point {

    private double longitude;
    private double latitude;

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double[] getCoordinates() {
        return new double[]{longitude, latitude};
    }
    
    public Point execute(Movement movement) {
    	return new Point(longitude+movement.getLongitudeDelta(), latitude+movement.getLatitudeDelta());
    }
    
    public String toString() {
    	return "{\"properties\":{}, \"geometry\":{\"type\": \"Point\", \"coordinates\": ["+
    				longitude +", "+ latitude + "]}},";
    }
}
