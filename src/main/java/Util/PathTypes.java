package Util;

import DTO.Highway;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;

import java.util.Map;

public class PathTypes {

    public static boolean isOneWay(OsmWay osmWay) {
        Map<String, String> tags = OsmModelUtil.getTagsAsMap(osmWay);
        return tags.containsKey("oneway") && tags.get("oneway").equals("yes") || tags.containsValue("motorway") || tags.containsValue("motorway_link");
    }

    public static float[] getMaxSpeed(OsmWay osmWay) {
        Map<String, String> tags = OsmModelUtil.getTagsAsMap(osmWay);
        String highway = tags.get("highway");
        float type = Highway.valueOf(highway).getType();
        float maxSpeed = Highway.valueOf(highway).getMaxSpeed();
        return new float[]{type, maxSpeed};
    }


}
