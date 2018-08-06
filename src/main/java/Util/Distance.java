package Util;

public class Distance {

    /**
     * @param latNode1, latNode2
     *                  Latitude of  each node.
     * @param lngNode1, lngNode2
     *                  Longitude of each node.
     * @return Approximation of the distance between two points.
     * <p>
     * approximate distance between two points using euclidean distance.
     * Error of approximation negligible for short distances
     */
    public static double euclideanDistance(double latNode1, double lngNode1, double latNode2, double lngNode2) {
        double x = latNode1 - latNode2;
        double y = (lngNode1 - lngNode2) * Math.cos(latNode2);
        return Math.sqrt(x * x + y * y) * 110.25;
    }


    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        return 6372.8 *2 * Math.asin(Math.sqrt(a));
    }

}
