package GraphStructure;

import Data.FilePaths;

import java.io.*;
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
            FileInputStream fis = new FileInputStream(FilePaths.binBWEdges );
            ObjectInputStream ois = new ObjectInputStream(fis);
            edges = (int[][]) ois.readObject();

            // Read nodes
            fis = new FileInputStream(FilePaths.binBWNodes);
            ois = new ObjectInputStream(fis);
            nodes = (double[][])ois.readObject();

            //fis = new FileInputStream(path + "offsets");
            //ois = new ObjectInputStream(fis);
            //offsets = (int[]) ois.readObject();

            edgeCount = edges[0].length;
            nodeCount = nodes[0].length;

            createNodeGrid();

            fis.close();
            ois.close();

        } catch (IOException e) {
            e.printStackTrace();
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
