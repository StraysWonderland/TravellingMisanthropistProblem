package GraphStructure;

import Data.FilePaths;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    private int[][] edges;
    private double[][] nodes;
    private int[] offsets;

    int nodeCount = 0;
    int edgeCount = 0;
    int amenityCount = 0;

    public HashMap<String, LinkedList<Integer>> nodeGrid;

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

    public void graphFromBinaries() throws ClassNotFoundException {
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

        } catch (IOException e) {
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
