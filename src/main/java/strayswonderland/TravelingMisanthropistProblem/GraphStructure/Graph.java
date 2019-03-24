package strayswonderland.TravelingMisanthropistProblem.GraphStructure;


import strayswonderland.TravelingMisanthropistProblem.Data.FilePaths;
import strayswonderland.TravelingMisanthropistProblem.Util.Distance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    private HashMap<String, LinkedList<Integer>> nodeGrid;
    private HashMap<String, LinkedList<Integer>> amenityGrid;

    final String edgesPath = FilePaths.binBWEdges;
    String nodesPath = FilePaths.binBWNodes;

   public int[] edgeSource;
   public int[] edgeTarget;
   public int[] edgeDistance;

    public double[][] nodes;
    public int[] offsets;

    int amenityCount = 0;
    private double[][] amenities;

    private void createNodeGrid() {
        nodeGrid = new HashMap<>();
        createGrid(nodes, nodeGrid);
    }

    private void createAmenityGrid() {
        amenityGrid = new HashMap<>();
        createGrid(amenities, amenityGrid);
    }

    private void createGrid(double[][] targetNodes, HashMap<String, LinkedList<Integer>> targetGrid) {
        for (int i = 0; i < targetNodes[0].length; i++) {
            String gridKey = (double) Math.round(targetNodes[0][i] * 10) / 10 + "-"
                    + (double) Math.round(targetNodes[1][i] * 10) / 10;
            if (targetGrid.containsKey(gridKey)) {
                targetGrid.get(gridKey).add(i);
            } else {
                LinkedList<Integer> nodesInCurrentCell = new LinkedList<>();
                nodesInCurrentCell.add(i);
                targetGrid.put(gridKey, nodesInCurrentCell);
            }
        }
    }

    public int getNearestNode(double[] latLng) {
        int nearestNodeIndex = -1;
        double shortestDist = Double.MAX_VALUE;
        String gridKey = (double) Math.round(latLng[0] * 10) / 10 + "-" + (double) Math.round(latLng[1] * 10) / 10;
        LinkedList<Integer> gridCell;

        if (!nodeGrid.containsKey(gridKey))
            return -1;

        gridCell = nodeGrid.get(gridKey);

        for (int nodeID : gridCell) {
            double dist = Distance.euclideanDistance(nodes[0][nodeID], nodes[1][nodeID], latLng[0], latLng[1]);
            if (dist < shortestDist) {
                shortestDist = dist;
                nearestNodeIndex = nodeID;
            }
        }
        return nearestNodeIndex;
    }

    public double[] getNodeCoords(int index) {
        return new double[]{nodes[0][index], nodes[1][index]};
    }

    public void loadMapData() {
        try {
            loadNodes();
            loadEdges();
            createNodeGrid();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("REQUIRED .NODES AND .EDGES FILES COULD NOT BE FOUND: MAKE SURE TO RUN THE PARSER FIRST");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadNodes() throws IOException {
        int nodeCounter = 0;

        BufferedReader bf = new BufferedReader(new FileReader(nodesPath));

        String line = bf.readLine();
        int numberOfNodes = Integer.parseInt(line);
        nodes = new double[2][numberOfNodes];

        line = bf.readLine();
        while (nodeCounter < numberOfNodes) {
            String vertexData[] = line.split(" ");
            nodes[0][nodeCounter] = Double.parseDouble(vertexData[1]);
            nodes[1][nodeCounter] = Double.parseDouble(vertexData[2]);
            nodeCounter++;
            line = bf.readLine();
        }
        bf.close();
    }

    private void loadEdges() throws IOException {
        String currentLine;
        int edgeCounter = 0;
        int nodeCounter = -1;

        offsets = new int[nodes[0].length];
        Arrays.fill(offsets, -1);

        BufferedReader bf = new BufferedReader(new FileReader(edgesPath));

        currentLine = bf.readLine();
        int numberOfEdges = Integer.parseInt(currentLine);
        edgeSource = new int[numberOfEdges];
        edgeTarget = new int[numberOfEdges];
        edgeDistance = new int[numberOfEdges];


        currentLine = bf.readLine();
        while (edgeCounter < numberOfEdges) {
            String edgeData[] = currentLine.split(" ");
            edgeSource[edgeCounter] = Integer.parseInt(edgeData[0]);
            edgeTarget[edgeCounter] = Integer.parseInt(edgeData[1]);
            edgeDistance[edgeCounter] = Integer.parseInt(edgeData[2]);
            if (nodeCounter < edgeSource[edgeCounter]) {
                nodeCounter = edgeSource[edgeCounter];
                offsets[nodeCounter] = edgeCounter;
            }
            edgeCounter++;
            currentLine = bf.readLine();
        }
        bf.close();
    }


    public int[] getEdgeSource() {
        return edgeSource;
    }
    public int[] getEdgeTarget() {
        return edgeTarget;
    }
    public int[] getEdgeDistance() {
        return edgeDistance;
    }

    public double[][] getNodes() {
        return nodes;
    }

    public int[] getOffsets() {
        return offsets;
    }
}
