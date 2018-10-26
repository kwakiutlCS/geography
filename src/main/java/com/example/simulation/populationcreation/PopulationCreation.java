package com.example.simulation.populationcreation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.geometry.GeoUtils;
import com.example.geometry.Point;

public class PopulationCreation {

	public static List<Point> execute(List<String> squares, int samples) {
		List<Point> points = new ArrayList<>();
		for (int i = 0; i < samples; i++) {
			points.add(getRandomPosInSquare(getRandomSquare(squares)));
		}
		
		return points;
	}
	
	public static String getRandomSquare(List<String> squares) {
		int high = squares.size()-1;
		double maxValue = Double.valueOf(squares.get(high).split(",")[3]);
		
		Random rand = new Random();
		double value = rand.nextDouble()*maxValue; 
		
		String square = getRandomSquare(squares, 0, high, value);
		//System.out.println(square+" - "+value);
		return square;
	}
	
	public static String getRandomSquare(List<String> squares, int low, int high, double value) {
		if (high == low) return squares.get(low).split(",")[0];
		
		int middle = (high-low)/2 + low;
		if (Double.valueOf(squares.get(middle).split(",")[3]) > value) {
			return getRandomSquare(squares, low, middle, value);
		} else if (middle == low) {
			return squares.get(high).split(",")[0];
		} else {
			return getRandomSquare(squares, middle, high, value);
		}
	}
	
	public static Point getRandomPosInSquare(String geohash) {
		double[] square = GeoUtils.decode_bbox(geohash);
		
		double latDelta = square[1]-square[0];
		double lonDelta = square[3]-square[2];
		
		Random rand = new Random();
		
		Point p = new Point(rand.nextDouble()*lonDelta + square[2],
				rand.nextDouble()*latDelta + square[0]);
		
		return p;
	}
}
