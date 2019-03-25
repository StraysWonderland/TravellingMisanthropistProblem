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
            generateNodeGrid();
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

  /*  private void createNodeGrid() {
        nodeGrid = new HashMap<>();
        for (int i = 0; i < nodes[0].length; i++) {
            String gridKey = generateKey(nodes[0][i], nodes[1][i]);
            if (nodeGrid.containsKey(gridKey)) {
                nodeGrid.get(gridKey).add(i);
            } else {
                System.out.println("new grid-key added");
                LinkedList<Integer> nodesInCurrentCell = new LinkedList<>();
                nodesInCurrentCell.add(i);
                nodeGrid.put(gridKey, nodesInCurrentCell);
            }
        }
    }*/

   /* public int getNearestNode(double[] latLng) {
        String gridKey = generateKey(latLng[0], latLng[1]);
        if (!nodeGrid.containsKey(gridKey))
            return -1;

        List<Integer> gridCell = nodeGrid.get(gridKey);
        int nearestNodeIndex = -1;
        double shortestDist = Double.MAX_VALUE;

        for (int nodeID : gridCell) {
            double dist = Distance.euclideanDistance(nodes[0][nodeID], nodes[1][nodeID], latLng[0], latLng[1]);
            if (dist < shortestDist) {
                shortestDist = dist;
                nearestNodeIndex = nodeID;
            }
        }
        return nearestNodeIndex;
    }*/

    public double[] getNodeCoords(int index) {
        return new double[]{nodes[0][index], nodes[1][index]};
    }

    private String generateKey(double lat, double lng) {
        StringBuilder gridKey = new StringBuilder();
        gridKey.append((double) Math.round(lat * 10) / 10);
        gridKey.append("-");
        gridKey.append((double) Math.round(lng * 10) / 10);

        return gridKey.toString();
    }


    private void generateNodeGrid() {
        long startTime = System.currentTimeMillis();
        nodeGrid = new HashMap<>();
        // Initialize nodeGridDimensions, which contains dimensions for each key in nodeGrid to set its array size
        HashMap<Integer, Integer> nodeGridDimensions = new HashMap<>();
        for (int i = 0; i < nodes[0].length; i++) {
            int nodeKey = encodeCoordinates(i);
            nodeGridDimensions.computeIfAbsent(nodeKey, (x -> 1));
            nodeGridDimensions.computeIfPresent(nodeKey, (k, x) -> (x + 1));
        }
        // The current highest non-empty position of the array is stored at the first cell of the array itself
        for (int i = 0; i < nodes[0].length; i++) {
            int nodeKey = encodeCoordinates(i);
            if (!nodeGrid.containsKey(nodeKey)) {
                nodeGrid.computeIfAbsent(nodeKey, (x -> new int[nodeGridDimensions.get(nodeKey)]))[1] = i;
                nodeGrid.get(nodeKey)[0] += 1;
            } else {
                nodeGrid.get(nodeKey)[nodeGrid.get(nodeKey)[0]] = i;
                nodeGrid.get(nodeKey)[0] += 1;
            }
        }
        System.out.println("Grid generated in: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private int encodeCoordinates(int current) {
        int latitude = (int) nodes[0][current];
        int longitude = (int) nodes[1][current];
        String s = String.valueOf(latitude) + String.valueOf(longitude);
        return Integer.valueOf(s);
    }

    public int getNearestNode(double[] markerCoordinates) {
        int latSource = (int) markerCoordinates[0];
        int lonSource = (int) markerCoordinates[1];
        String s = "" + latSource + lonSource;
        int nodeKey = Integer.valueOf(s);

        //initialize distance-array with length - 1 because the gridArray's first element is not a node but an information
        //that was needed to fill it in the graph.class.
        int nearestNeighbourId = Integer.MAX_VALUE;
        if (nodeGrid.containsKey(nodeKey)) {
            double[] distanceNeighboursFromSource = new double[nodeGrid.get(nodeKey).length - 1];
            nearestNeighbourId = 0;
            double nearestNeighbourCurrent = Double.MAX_VALUE;

            for (int j = 0; j < distanceNeighboursFromSource.length; j++) {
                distanceNeighboursFromSource[j] = Distance.euclideanDistance(markerCoordinates[0],
                        markerCoordinates[1],
                        nodes[0][nodeGrid.get(nodeKey)[j + 1]],
                        nodes[1][nodeGrid.get(nodeKey)[j + 1]]);

                if (distanceNeighboursFromSource[j] < nearestNeighbourCurrent) {
                    nearestNeighbourCurrent = distanceNeighboursFromSource[j];
                    nearestNeighbourId = (nodeGrid.get(nodeKey)[j + 1]);
                }
            }
        }
        return nearestNeighbourId;
    }
}
