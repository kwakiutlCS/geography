package com.example.geometry;

import com.jillesvangurp.geo.GeoHashUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeoUtils extends GeoHashUtils {

    public static String north(String geohash) {
        return GeoHashUtils.south(geohash);
    }

    public static String south(String geohash) {
        return GeoHashUtils.north(geohash);
    }
    
    public static String northeast(String geohash) {
    	return north(east(geohash));
    }
    
    public static String southwest(String geohash) {
    	return south(west(geohash));
    }
    
    public static String encode(Point point) {
    	return GeoHashUtils.encode(point.getCoordinates()[1], point.getCoordinates()[0], 5);
    }

    public static Set<String> geoHashesForPolygon(int length, double[][] points, boolean uniform) {
        Set<String> subhashes = GeoHashUtils.geoHashesForPolygon(length, points);

        return uniform ? subhashes.stream()
                                  .flatMap(g -> GeoUtils.decompose(g, length).stream())
                                  .collect(Collectors.toSet()) :
                        subhashes;
    }

    public static Set<String> geoHashGrid(int size, String center) {
        return geoHashBiDirectionalLine(size, center, GeoUtils::north, GeoUtils::south)
                .stream()
                .flatMap(g -> geoHashBiDirectionalLine(size, g, GeoUtils::west, GeoUtils::east).stream())
                .collect(Collectors.toSet());
    }

    public static Polygon toPolygon(String geohash) {
        double[] bb = GeoHashUtils.decode_bbox(geohash);

        return Polygon.PolygonBuilder.create()
                .add(bb[2], bb[1])
                .add(bb[2], bb[0])
                .add(bb[3], bb[0])
                .add(bb[3], bb[1])
                .close();
    }

    public static double distance(Point p1, Point p2) {
        int radius = 6371;
        double lat1 = Math.toRadians(p1.getCoordinates()[1]);
        double lat2 = Math.toRadians(p2.getCoordinates()[1]);
        double lon1 = Math.toRadians(p1.getCoordinates()[0]);
        double lon2 = Math.toRadians(p2.getCoordinates()[0]);

        double delta1 = lat2-lat1;
        double delta2 = lon2-lon1;

        double a = Math.sin(delta1/2) * Math.sin(delta1/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(delta2/2) * Math.sin(delta2/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return radius*c;
    }

    public static List<String> geoHashLine(int length, String start, Function<String, String> direction) {
        return geoHashLine(length, start, direction, new ArrayList<>());
    }

    private static Set<String> geoHashBiDirectionalLine(int length, String start, Function<String, String> direction1, Function<String, String> direction2) {
        Set<String> line = new HashSet<>();
        line.add(start);
        line.addAll(geoHashLine(length, start, direction1));
        line.addAll(geoHashLine(length, start, direction2));

        return line;
    }

    private static List<String> geoHashLine(int length, String start, Function<String, String> direction, List<String> acc) {
        if (length <= 0) return acc;
        String newGeohash = direction.apply(start);
        acc.add(newGeohash);
        return geoHashLine(length-1, newGeohash, direction, acc);
    }

    private static Set<String> decompose(String geohash, final int length) {
        if (geohash.length() == length) {
            return new HashSet<>(Arrays.asList(geohash));
        } else if (geohash.length() < length) {
            return Arrays.asList(GeoUtils.subHashes(geohash))
                    .stream()
                    .flatMap(g -> decompose(g, length).stream())
                    .collect(Collectors.toSet());
        }

        return new HashSet<>();
    }
}
