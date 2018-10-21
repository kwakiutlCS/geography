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
}
