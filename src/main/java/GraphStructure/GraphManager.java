package GraphStructure;

import Data.FilePaths;
import Data.Highway;
import Data.HighwayType;
import Util.Distance;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.pbf.seq.PbfIterator;

import java.io.*;
import java.util.*;

public class GraphManager {
    private final String pbfPath = FilePaths.pbfBW;
    private final String binaryPath = FilePaths.binBW;

    private double[][] nodes;
    private int[][] edges;
    private int[] offset;

    private List<double[]> edgeList = new ArrayList<>();
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
                if (Highway.isHighway(OsmModelUtil.getTagsAsMap(currentEdge).get("highway"))) {
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
        List<double[]> nodeList = new ArrayList<>(nodeLookup.values());
        Collections.sort(nodeList, (a, b) -> (Double.compare(a[0], b[0])));
        for (int i = 0; i < nodeList.size(); i++) {
            nodes[0][i] = nodeList.get(i)[1];   // latitude
            nodes[1][i] = nodeList.get(i)[2];   // longitude

            nodeLookup.put((long) nodeList.get(i)[0],
                    new double[]{
                            (double) i, // serves as localId
                            (double) 0,
                            (double) 0
                    });
        }
        nodeList.clear();
    }

    private void retrieveEdgesBetweenNodes() {
        iterator = new PbfIterator(stream, false);
        for (EntityContainer container : iterator) {
            if (container.getType() == EntityType.Way) {
                OsmWay currentWay = (OsmWay) container.getEntity();
                if (Highway.isHighway(OsmModelUtil.getTagsAsMap(currentWay).get("highway"))) {
                    for (int i = 0; i < currentWay.getNumberOfNodes(); i++) {
                        convertToEdgeStructure(currentWay);
                    }
                }
            }
        }
    }

    private void convertToEdgeStructure(OsmWay way) {
        for (int i = 0; i < way.getNumberOfNodes() - 1; i++) {
            double[] node1 = nodeLookup.get(i);
            double[] node2 = nodeLookup.get(i + 1);

            edges[i][0] = (int) node1[0];
            edges[i][1] = (int) node2[0];
            edges[i][2] = (int) Distance.calculateDistance(node1[1], node1[2], node2[1], node2[2]);
        }
    }

    private void sortEdges() {
        java.util.Arrays.sort(edges, (a, b) -> (Integer.compare(a[0], b[0])));
    }


}