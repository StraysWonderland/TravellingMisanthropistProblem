package strayswonderland.TravelingMisanthropistProblem.GraphStructure;


import strayswonderland.TravelingMisanthropistProblem.Data.FilePaths;
import strayswonderland.TravelingMisanthropistProblem.Util.Distance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Graph {

    private HashMap<Integer, int[]> nodeGrid;

    final String edgesPath = FilePaths.binBWEdges;
    String nodesPath = FilePaths.binBWNodes;

    public int[] edgeSource;
    public int[] edgeTarget;
    public int[] edgeDistance;

    public double[][] nodes;
    public int[] offsets;

    public void loadMapData() {
        try {
            loadNodes();
            System.out.println("finished reading nodes");
            loadEdges();
            System.out.println("finished reading edges");
            createNodeGrid();
            System.out.println("created NodeGrid");
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

    public double[] getNodeCoodinates(int index) {
        return new double[]{nodes[0][index], nodes[1][index]};
    }


    private void createNodeGrid() {
        nodeGrid = new HashMap<>();
        HashMap<Integer, Integer> gridCell = new HashMap<>();
        for (int i = 0; i < nodes[0].length; i++) {
            int gridKey = generateKey(i);
            gridCell.computeIfAbsent(gridKey, (x -> 1));
            gridCell.computeIfPresent(gridKey, (k, x) -> (x + 1));
        }
        for (int i = 0; i < nodes[0].length; i++) {
            int nodeKey = generateKey(i);
            if (!nodeGrid.containsKey(nodeKey)) {
                nodeGrid.computeIfAbsent(nodeKey, (x -> new int[gridCell.get(nodeKey)]))[1] = i;
                nodeGrid.get(nodeKey)[0] += 1;
            } else {
                nodeGrid.get(nodeKey)[nodeGrid.get(nodeKey)[0]] = i;
                nodeGrid.get(nodeKey)[0] += 1;
            }
        }
    }

    private int generateKey(int current) {
        int latitude = (int) nodes[0][current];
        int longitude = (int) nodes[1][current];
        String s = String.valueOf(latitude) + String.valueOf(longitude);
        return Integer.valueOf(s);
    }

    private int generateKey(int lat, int lng) {
        String s = String.valueOf(lat) + String.valueOf(lng);
        return Integer.valueOf(s);
    }

    public int getNearestNode(double[] markerCoordinates) {
        int latitude = (int) markerCoordinates[0];
        int longitued = (int) markerCoordinates[1];
        int gridKey = generateKey(latitude, longitued);
        int nearestNodeIndex = Integer.MAX_VALUE;
        if (nodeGrid.containsKey(gridKey)) {
            double[] distanceNeighboursFromSource = new double[nodeGrid.get(gridKey).length - 1];
            nearestNodeIndex = 0;
            double nearestNeighbourCurrent = Double.MAX_VALUE;

            for (int j = 0; j < distanceNeighboursFromSource.length; j++) {
                distanceNeighboursFromSource[j] = Distance.euclideanDistance(markerCoordinates[0],
                        markerCoordinates[1],
                        nodes[0][nodeGrid.get(gridKey)[j + 1]],
                        nodes[1][nodeGrid.get(gridKey)[j + 1]]);

                if (distanceNeighboursFromSource[j] < nearestNeighbourCurrent) {
                    nearestNeighbourCurrent = distanceNeighboursFromSource[j];
                    nearestNodeIndex = (nodeGrid.get(gridKey)[j + 1]);
                }
            }
        }
        return nearestNodeIndex;
    }
}
