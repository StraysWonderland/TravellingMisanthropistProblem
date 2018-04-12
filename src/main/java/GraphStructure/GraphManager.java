package GraphStructure;

import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.pbf.seq.PbfIterator;

import java.io.*;
import java.util.*;

public class GraphManager {
    private final String pbfPath = "bw.osm.pbf";
    private final String binaryPath = "baden-wuerttemberg-map";

    private double[][] nodes;
    private int[][] edges;
    private int[] offset;

    private List<double[]> edgeList = new ArrayList<>();
    private List<double[]> nodeList = new ArrayList<>();
    private List<String> barsList = new ArrayList<>();
    HashMap<Long, double[]> nodeLookup;

    // nodes currently : 2969443
    private int numNodes = 39472043;
    private int numEdges = 52325469;

    PbfIterator iterator;
    InputStream stream;

    public GraphManager() {
        edges = new int[3][numEdges];
        nodes = new double[3][numNodes];
        offset = new int[numNodes];
        Arrays.fill(offset, -1);
        nodeLookup = new HashMap<>();
    }

    public void parseFromPbf() {
        try {
            stream = new FileInputStream(pbfPath);
            retrieveRelevantNodes();
            System.out.println("Looked up relevant nodes");

            stream = new FileInputStream(pbfPath);
            retrieveDataForNodes();
            System.out.println("Added geo coordinates");

            stream = new FileInputStream(pbfPath);
            localiseAndSortNodes();
            System.out.println("localised nodes");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File could not be found!");
        } finally {
            try {
                stream.close();
                System.out.println(String.format("nodes: %-15s edges: %-15s",
                        numNodes,
                        numEdges
                ));
            } catch (IOException e) {
                System.out.println("Closing of InputStream failed!");
            }
        }
    }

    public void retrieveRelevantNodes() {
        iterator = new PbfIterator(stream, false);
        for (EntityContainer container : iterator) {
            if (container.getType() == EntityType.Way) {
                OsmWay currentEdge = (OsmWay) container.getEntity();
                if (isHighway(currentEdge)) {
                    for (int i = 0; i < currentEdge.getNumberOfNodes(); i++) {
                        nodeLookup.put(currentEdge.getNodeId(i), new double[]{});
                        System.out.println("relevant node added: " + currentEdge.getNodeId(i));
                    }
                }
            }
        }
    }

    private void retrieveDataForNodes() {
        iterator = new PbfIterator(stream, false);
        for (EntityContainer container : iterator) {
            if (container.getType() == EntityType.Node) {
                OsmNode osmNode = (OsmNode) container.getEntity();
                if (nodeLookup.containsKey(osmNode.getId())) {
                    double[] nodeData = new double[]{
                            osmNode.getId(),
                            osmNode.getLatitude(),
                            osmNode.getLongitude(),
                    };
                    nodeLookup.put(osmNode.getId(), nodeData);
                    System.out.println("data for node " + osmNode.getId() + ": "
                            + "lat: " + nodeData[1]
                            + "lon: " + nodeData[2]);
                }
            }
        }
    }

    private void localiseAndSortNodes() {
        nodeList = new ArrayList<>(nodeLookup.values());
        int localId = 0;
        for (Map.Entry<Long, double[]> entry : nodeLookup.entrySet()) {
            nodes[0][localId] = entry.getValue()[1];
            nodes[1][localId] = entry.getValue()[2];

            nodeLookup.put(entry.getKey(), new double[]{
                    (double) localId, // serves as the localId
                    0,
                    0
            });
            localId++;
        }

        //region Beautyful but unusable lambda </3
        /*
        nodeLookup.forEach((key, valueArray) -> {
            nodes[localIdCount][0] = valueArray[1];
            nodes[localIdCount][0] = valueArray[2];
            nodeLookup.put(key, new double[]{(double) localIdCount, 0, 0});
            localIdCount++;
        });
        */
        //endregion

    }

    private void retrieveEdgesBetweenNodes() {
        iterator = new PbfIterator(stream, false);
        for (EntityContainer container : iterator) {
            if (container.getType() == EntityType.Way) {
                OsmWay currentWay = (OsmWay) container.getEntity();
                if (isHighway(currentWay)) {
                    for (int i = 0; i < currentWay.getNumberOfNodes(); i++) {
                        convertToEdgeStructure(currentWay);
                    }
                }
            }
        }
    }

    private void convertToEdgeStructure(OsmWay way) {
        for (int i = 0; i < way.getNumberOfNodes() - 1; i++) {
            edges[i][0] = (int) nodeLookup.get(way.getNodeId(i))[0];
            edges[i][1] = (int) nodeLookup.get(way.getNodeId(i + 1))[0];
            // TODO calculate distance
        }
    }

    private void sortEdges() {
        java.util.Arrays.sort(edges, (a, b) -> (Integer.compare(a[0], b[0])));
    }

    private boolean isHighway(OsmWay way) {
        List<String> desiredHighwayTypes = Arrays.asList(
                "motorway",
                "trunk",
                "primary",
                "secondary",
                "residential",
                "service");
        String currentType = OsmModelUtil.getTagsAsMap(way).get("highway");
        if (currentType == null)
            return false;
        // Ignore nodes without such a tag
        if (!desiredHighwayTypes.contains(currentType))
            return false;

        return true;
    }

}