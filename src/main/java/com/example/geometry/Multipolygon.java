package com.example.geometry;

import java.util.ArrayList;
import java.util.List;

public class Multipolygon {

    private List<Polygon> polygons;

    public Multipolygon(List<Polygon> polygons) {
        this.polygons = new ArrayList<>(polygons);
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}
