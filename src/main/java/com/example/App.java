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
	
	private static Map<String, List<Integer>> geohashes = new HashMap<>();
	private static Map<String, List<Integer>> enlarged = new HashMap<>();
	
	public static void main( String[] args ) throws IOException {

    	List<String> lines = Files.lines(Paths.get("data/tiles-weights.csv"))
					.collect(Collectors.toList());
		
    	List<Point> positions = PopulationCreation.execute(lines, 100);
    	
    	Set<String> squares = lines.stream().map(l -> l.split(",")[0]).collect(Collectors.toSet());
    	
    	
		
		try (Stream<Path> paths = Files.walk(Paths.get("foreca/filter"))) {
		    paths
		        .filter(Files::isRegularFile)
		        .filter(p -> p.getFileName().toString().split("\\.")[0].equals("ca"))
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
//		for (String key : geohashes.keySet()) {
//			System.out.println("\n\n");
//			System.out.println(key);
//			System.out.println(geohashes.get(key));
//			System.out.println(enlarged.get(key));
//		}
    }
	
	private static void calculate(Path path) throws IOException {
		
		String country = path.getFileName().toString().split("\\.")[0];
		System.out.println();
		System.out.println(path);
		if (!geohashes.containsKey(country)) {
			geohashes.put(country, new ArrayList<>());
			enlarged.put(country, new ArrayList<>());
		}
		
		List<Integer> counterNormal = geohashes.get(country);
		List<Integer> counterLarge = enlarged.get(country);
		
		Stream<Polygon> polygons = Files.lines(path).map(line -> {
			String[] items = line.trim().split(",");
			PolygonBuilder pb = PolygonBuilder.create();
			for (String item : items) {
				String[] coords = item.split(" ");
				pb.add(Double.valueOf(coords[0]), Double.valueOf(coords[1]));
			}
			
			return pb.close();
		});
		
		
		Set<Set<String>> normalHashes = 
				polygons.map(
						p -> GeoUtils.geoHashesForPolygon(5, p.getCoordinates(), true)).collect(Collectors.toSet());
		
		int normalSize = normalHashes.stream()
		        .mapToInt(Set::size)
		        .sum();
		
		System.out.println(country +" "+normalSize);
		int largeCounter = normalHashes
				.stream()
				.map(set -> set.stream()
							   .flatMap(h -> GeoUtils.geoHashGrid(2, h).stream()).collect(Collectors.toSet()))
				.mapToInt(Set::size)
				.sum();                        
		
		System.out.println(country +" "+largeCounter);
		counterNormal.add(normalSize);
		counterLarge.add(largeCounter);
	}
}
