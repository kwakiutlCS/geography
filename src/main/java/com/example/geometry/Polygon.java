package com.example.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Polygon {

    private List<Point> coordinates;

    private Polygon(List<Point> coordinates) {
        this.coordinates = coordinates;
    }

    public double[][] getCoordinates() {
        return coordinates.stream()
                     .map(Point::getCoordinates)
                     .collect(Collectors.toList())
                     .toArray(new double[][]{});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[[");
        coordinates.forEach(p -> sb.append("[")
                                .append(p.getCoordinates()[0])
                                .append(", ")
                                .append(p.getCoordinates()[1])
                                .append("], "));
        sb.delete(sb.length()-2, sb.length());
        sb.append("]]");
        return sb.toString();
    }

    public static class PolygonBuilder {
        private List<Point> coordinates;

        public static PolygonBuilder create() {
            PolygonBuilder builder = new PolygonBuilder();
            builder.coordinates = new ArrayList<>();
            return builder;
        }

        public PolygonBuilder add(double longitude, double latitude) {
            coordinates.add(new Point(longitude, latitude));
            return this;
        }

        public Polygon close() {
            coordinates.add(coordinates.get(0));
            return new Polygon(coordinates);
        }
    }
}
