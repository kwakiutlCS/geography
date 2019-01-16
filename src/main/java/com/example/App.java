package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.geometry.GeoUtils;
import com.example.geometry.Point;
import com.example.geometry.Polygon;
import com.example.geometry.Polygon.PolygonBuilder;
import com.example.simulation.populationcreation.PopulationCreation;
import com.example.simulation.populationmovement.PopulationMovement;

public class App {
	
    private static final int TRESHOLD3 = 15000;
    private static final int TRESHOLD4 = 500;
    
    private static Map<String, List<Integer>> geohashes5 = new HashMap<>();
    private static Map<String, List<Integer>> geohashes4 = new HashMap<>();
    private static Map<String, List<Integer>> geohashes3 = new HashMap<>();
    private static Map<String, List<Integer>> enlarged5 = new HashMap<>();
	
	public static void main( String[] args ) throws IOException {

    	List<String> lines = Files.lines(Paths.get("data/tiles-weights.csv"))
					.collect(Collectors.toList());
		
    	List<Point> positions = PopulationCreation.execute(lines, 100);
    	
    	Set<String> squares = lines.stream().map(l -> l.split(",")[0]).collect(Collectors.toSet());
    	
    	
		
		try (Stream<Path> paths = Files.walk(Paths.get("foreca/filter"))) {
		    paths
		        .filter(Files::isRegularFile)
		        .filter(p -> p.getFileName().toString().split("\\.")[0].equals("us"))
		        .forEach(p -> {
		        	try {
						PopulationMovement.execute(positions, squares, p);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        });
		} 
		
		
//		try (Stream<Path> paths = Files.walk(Paths.get("foreca/filter"))) {
//		    paths
//		        .filter(Files::isRegularFile)
//		        .filter(p -> p.getFileName().toString().equals("pl.csv"))
//		        .forEach(f -> {
//					try {
//						calculate(f);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				});
//		} 
//		
//		for (String key : geohashes5.keySet()) {
//			System.out.println("\n\n");
//			System.out.println(key);
//			System.out.println(geohashes5.get(key));
//			System.out.println(enlarged5.get(key));
//			System.out.println(geohashes4.get(key));
//			System.out.println(geohashes3.get(key));
//            
//		}
    }
	
	private static void calculate(Path path) throws IOException {
		
		String country = path.getFileName().toString().split("\\.")[0];
		System.out.println();
		System.out.println(path);
		if (!geohashes5.containsKey(country)) {
		    geohashes5.put(country, new ArrayList<>());
		    geohashes4.put(country, new ArrayList<>());
		    geohashes3.put(country, new ArrayList<>());
            enlarged5.put(country, new ArrayList<>());
		}
		
		List<Integer> counter5 = geohashes5.get(country);
		List<Integer> counter4 = geohashes4.get(country);
		List<Integer> counter3 = geohashes3.get(country);
        List<Integer> counterLarge = enlarged5.get(country);
		
		List<Polygon> polygons = Files.lines(path).map(line -> {
			String[] items = line.trim().split(",");
			PolygonBuilder pb = PolygonBuilder.create();
			for (String item : items) {
				String[] coords = item.split(" ");
				pb.add(Double.valueOf(coords[0]), Double.valueOf(coords[1]));
			}
			
			return pb.close();
		}).collect(Collectors.toList());
		
		
		computeDay(polygons, counter5, counterLarge, counter4, counter3, country);
	}
	
	
	private static void computeDay(List<Polygon> polygons, List<Integer> cl5, List<Integer> clL, List<Integer> cl4, List<Integer> cl3, String country) {
	    int c5 = 0, c4 = 0, c3 = 0, cL = 0;
	    
	    for (Polygon polygon : polygons) {
	        Set<String> hashes = GeoUtils.geoHashesForPolygon(5, polygon.getCoordinates(), true);
	        
	        if (hashes.size() < TRESHOLD4) {
	            c5 += hashes.size();
	            
	            cL += hashes.stream().flatMap(h -> GeoUtils.geoHashGrid(2, h).stream()).collect(Collectors.toSet()).size();
	        } else if (hashes.size() < TRESHOLD3) {
	            c4 += GeoUtils.geoHashesForPolygon(4, polygon.getCoordinates(), true).size();
	        } else {
	            c3 += GeoUtils.geoHashesForPolygon(3, polygon.getCoordinates(), true).size();
	        }
	    }
	    
	    System.out.println(country +" "+c5);
	    System.out.println(country +" "+cL);
	    cl5.add(c5);
	    clL.add(cL);
	    cl4.add(c4);
	    cl3.add(c3);
	}
}
