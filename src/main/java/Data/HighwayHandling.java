package Data;

import DTO.Highway;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HighwayHandling {

    private static final List<String> desiredTypesValues = Arrays.asList(
            "motorway",
            "trunk",
            "primary",
            "secondary",
            "tertiary",
            "unclassified",
            "residential",
            "service",
            "living_street",
            "pedestrian",
            "track",
            "footway",
            "bridleway",
            "steps",
            "path",
            "cycleway",
            "unclassified",
            "motorway_link",
            "primary_link",
            "secondary_link",
            "tertiary_link",
            "trunk_link",
            "primary_link",
            "road");

    public static Set<String> desiredHighwayTypes = new HashSet<>(desiredTypesValues);

    public static boolean isHighway(String currentType) {
        if (currentType == null)
            return false;

        return Arrays.stream(Highway.values()).anyMatch(x -> x.getName().equals(currentType));
    }
}
