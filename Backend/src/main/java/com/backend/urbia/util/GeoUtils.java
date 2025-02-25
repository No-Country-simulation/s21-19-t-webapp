package com.backend.urbia.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class GeoUtils {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point createPoint(double lng, double lat) {
        return geometryFactory.createPoint(new Coordinate(lng, lat));
    }

    public static String convertPointToGeoJSON(Point point) {
        try {
            Map<String, Object> geoJson = new HashMap<>();
            geoJson.put("type", "Point");
            geoJson.put("coordinates", new double[]{point.getX(), point.getY()});
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(geoJson);
        } catch (Exception e) {
            return "{}";
        }
    }
}
