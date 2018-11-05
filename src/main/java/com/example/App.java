package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.geometry.Point;
import com.example.simulation.populationcreation.PopulationCreation;
import com.example.simulation.populationmovement.PopulationMovement;

public class App {
	
	public static void main( String[] args ) throws IOException {

    	List<String> lines = Files.lines(Paths.get("data/tiles-weights.csv"))
					.collect(Collectors.toList());
		
    	List<Point> positions = PopulationCreation.execute(lines, 10000);
    	
    	Set<String> squares = lines.stream().map(l -> l.split(",")[0]).collect(Collectors.toSet());
    	
    	PopulationMovement.execute(positions, squares);
    }
}
