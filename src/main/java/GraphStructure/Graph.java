package GraphStructure;

import Data.FilePaths;
import Util.Distance;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    private HashMap<String, LinkedList<Integer>> nodeGrid;
    int nodeCount = 0;
    int edgeCount = 0;
    int amenityCount = 0;
    final String edgesPath = FilePaths.binBWEdges;
    String nodesPath = FilePaths.binBWNodes;
    private int[][] edges;
    private double[][] nodes;
    private int[] offsets;

    private void createNodeGrid() {
        nodeGrid = new HashMap<>();
        for (int i = 0; i < nodes[0].length; i++) {
            String gridKey = (double) Math.round(nodes[0][i] * 10) / 10 + "-"
                    + (double) Math.round(nodes[1][i] * 10) / 10;
            if (nodeGrid.containsKey(gridKey)) {
                nodeGrid.get(gridKey).add(i);
            } else {
                LinkedList<Integer> nodesInCell = new LinkedList<>();
                nodesInCell.add(i);
                nodeGrid.put(gridKey, nodesInCell);
            }
        }
    }


    public int getNearestNode(double[] latLng) {
        int nearestNodeIndex = -1;
        double shortestDist = Double.MAX_VALUE;
        String gridKey = (double) Math.round(latLng[0] * 10) / 10 + "-" + (double) Math.round(latLng[1] * 10) / 10;
        LinkedList<Integer> gridCell;

        if (nodeGrid.containsKey(gridKey)) {
            gridCell = nodeGrid.get(gridKey);
        } else {
            return -1;
        }

        for (int nodeID : gridCell) {
            double dist = Distance.euclideanDistance(nodes[0][nodeID], nodes[1][nodeID], latLng[0], latLng[1]);
            if (dist < shortestDist) {
                shortestDist = dist;
                nearestNodeIndex = nodeID;
            }
        }
        return nearestNodeIndex;
    }

    public void loadMapData() {
        System.out.println("Loading map data...");
        try {
            loadNodes();
            loadEdges();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Graph file not found, exiting!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not read graph file, exiting!");
            System.exit(2);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println(
                    "Number of edges or nodes is not consistent with the specification at the beginning of the graph file, exiting!");
            System.exit(3);

        }
    }

    private void loadEdges() throws IOException {
        String line;
        BufferedReader bf = new BufferedReader(new FileReader(edgesPath));
        // Read number of edges and create an array with the same size to store them.
        line = bf.readLine();
        int edgesCount = Integer.parseInt(line);
        edges = new int[3][edgesCount];
        int counterEdges = 0;
        // Read all edges from file.
        // Create offset array and init with -1 (no outgoing edge)
        offsets = new int[nodes[0].length];
        Arrays.fill(offsets, -1);
        int counterNodes = -1;
        line = bf.readLine();
        while (counterEdges < edgesCount) {
            String edgeData[] = line.split(" ");
            // Store start-node, dest-node and weighting in 'edges' array.
            edges[0][counterEdges] = Integer.parseInt(edgeData[0]);
            edges[1][counterEdges] = Integer.parseInt(edgeData[1]);
            edges[2][counterEdges] = Integer.parseInt(edgeData[2]);
            /*
             * Check if current edge starts from new vertex and if so, store offset of that
             * new vertex in the offset-array. 'counternodes' stores the  ID of the current start node.
             * So if the next edge starts from a new node, 'counternodes < edges[counterEdges][0]' is true (since edges are sorted by
             * their starting node) */
            if (counterNodes < edges[0][counterEdges]) {
                counterNodes = edges[0][counterEdges];
                offsets[counterNodes] = counterEdges;
            }
            counterEdges++;
            line = bf.readLine();
        }
        bf.close();
    }

    private void loadNodes() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(nodesPath));
        // Read number of nodes and create an array with the same size to store them.
        String line = bf.readLine();
        int nodesCount = Integer.parseInt(line);
        nodes = new double[2][nodesCount];
        line = bf.readLine();
        int counterNodes = 0;
        // Read all nodes from file. Store geographic coordinates in 'nodes' array
        while (counterNodes < nodesCount) {
            String vertexData[] = line.split(" ");
            nodes[0][counterNodes] = Double.parseDouble(vertexData[1]);
            nodes[1][counterNodes] = Double.parseDouble(vertexData[2]);
            counterNodes++;
            line = bf.readLine();
        }
        bf.close();
    }

    public void graphFromBinaries() {
        try {
            // Read edges
            FileInputStream fis = new FileInputStream(FilePaths.binBWEdges);
            ObjectInputStream ois = new ObjectInputStream(fis);
            edges = (int[][]) ois.readObject();

            // Read nodes
            fis = new FileInputStream(FilePaths.binBWNodes);
            ois = new ObjectInputStream(fis);
            nodes = (double[][]) ois.readObject();

            edgeCount = edges[0].length;
            nodeCount = nodes[0].length;

            //fis = new FileInputStream(path + "offsets");
            //ois = new ObjectInputStream(fis);
            //offsets = (int[]) ois.readObject();

            calculateOffsets();

            createNodeGrid();

            fis.close();
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void calculateOffsets() {
        offsets = new int[nodes[0].length];
        Arrays.fill(offsets, -1);
        int counterNodes = -1;

        for (int currentEdgeCount = 0; currentEdgeCount < edgeCount; currentEdgeCount++) {
            int targetVertex = edges[0][currentEdgeCount];
            if (counterNodes < targetVertex) {
                counterNodes = targetVertex;
                offsets[counterNodes] = currentEdgeCount;
            }
        }
    }

    public int[][] getEdges() {
        return edges;
    }

    public double[][] getNodes() {
        return nodes;
    }

    public int[] getOffsets() {
        return offsets;
    }
}
