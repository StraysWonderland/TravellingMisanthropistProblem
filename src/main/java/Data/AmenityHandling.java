package Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmenityHandling {

    private static List<String> desiredAmenitiesValues = Arrays.asList(
            "pub",
            "bar",
            "biergarten",
            "cafe",
            "brothel",
            "gambling",
            "nightclub",
            "stripclub",
            "swingerclub",
            "restaurant"
            );

    public static Set<String> desiredAmenities = new HashSet<>(desiredAmenitiesValues);

    public static boolean isAmenity(String currentType) {
        if (currentType == null)
            return false;

        if (!desiredAmenitiesValues.contains(currentType))
            return false;

        return true;
    }
}