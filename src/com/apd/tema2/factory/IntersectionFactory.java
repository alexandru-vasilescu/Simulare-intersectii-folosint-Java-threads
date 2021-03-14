package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static Map<String, Intersection> cache = new HashMap<>();

    static {
    	// Am adaugat o instantiare de clasa pentru fiecare tip de intersectie dupa nume
        cache.put("simpleIntersection", new SimpleIntersection());
        cache.put("carRoundabout", new CarRoundabout());
        cache.put("railroad", new Railroad());
        cache.put("priority", new PriorityIntersection());
        cache.put("maintenance", new Maintenance());
        cache.put("crosswalk", new Crosswalk());
    }

    public static Intersection getIntersection(String handlerType) {
        return cache.get(handlerType);
    }

}
