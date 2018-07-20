package Util;

import DTO.Highway;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;

import java.util.Map;

public class PathTypes {

    public static boolean isOneWayTag(OsmWay osmWay) {
        Map<String, String> tags = OsmModelUtil.getTagsAsMap(osmWay);
        return tags.containsKey("oneway") && tags.get("oneway").equals("yes") || tags.containsValue("motorway") || tags.containsValue("motorway_link");
    }

    public static boolean isOneWay(OsmWay osmWay) {
        boolean oneWay = false;
        for (int j = 0; j < osmWay.getNumberOfTags(); j++) {
            oneWay = (osmWay.getTag(j).getKey().equals("oneway")
                    && osmWay.getTag(j).getValue().equals("yes"))
                    || osmWay.getTag(j).getKey().equals("motorway")
                    || osmWay.getTag(j).getKey().equals("motorway_link");
        }
        return true;
    }

    public static float[] getMaxSpeed(OsmWay osmWay) {
        Map<String, String> tags = OsmModelUtil.getTagsAsMap(osmWay);
        String highway = tags.get("highway");
        float type = Highway.valueOf(highway).getType();
        float maxSpeed = Highway.valueOf(highway).getMaxSpeed();
        return new float[]{type, maxSpeed};
    }


}
