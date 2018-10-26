package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.example.simulation.populationcreation.PopulationCreation;

public class App 
{
	
	public static void main( String[] args ) throws IOException {

    	List<String> lines = Files.lines(Paths.get("data/squares.csv"))
					//.limit(100)
					.collect(Collectors.toList());
		
    	PopulationCreation.execute(lines, 1000).forEach(System.out::println);
    }
}
