package Util;

public  class Distance {

    /**
     * @param latNode1, latNode2
     *            Latitude of  each node.
     * @param lngNode1, lngNode2
     *            Longitude of each node.
     * @return Approximation of the distance between two points.
     *
     *         approximate distance between two points using euclidean distance.
     *         Error of approximation negligible for short distances
     */
    public static double calculateDistance(double latNode1, double lngNode1, double latNode2, double lngNode2) {
        double x = latNode1 - latNode2;
        double y = (lngNode1 - lngNode2) * Math.cos(latNode2);
        return Math.sqrt(x * x + y * y) * 110.25;
    }
}
