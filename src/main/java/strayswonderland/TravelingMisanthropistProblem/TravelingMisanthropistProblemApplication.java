package strayswonderland.TravelingMisanthropistProblem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import strayswonderland.TravelingMisanthropistProblem.GraphStructure.Graph;
import strayswonderland.TravelingMisanthropistProblem.Pathing.Pathing;
import strayswonderland.TravelingMisanthropistProblem.Pathing.TravelingSalesmanProblem;
import strayswonderland.TravelingMisanthropistProblem.Util.Distance;

import java.util.ArrayList;

@SpringBootApplication
@RestController
public class TravelingMisanthropistProblemApplication {
    private static Graph graph;

    public static void main(String[] args) {
        System.out.println(" PLEASE WAIT: LOADING MAP DATA FROM FILE. APPLICATION WILL RUN SHORTLY");
        graph = new Graph();
        graph.loadMapData();
        SpringApplication.run(TravelingMisanthropistProblemApplication.class, args);
    }


    @RequestMapping("/getClosestNodeToMarker/{markerNode}")
    public String getClosestNodeToMarker(@PathVariable double[] markerNode) {
        int closestNodeIndex = graph.getNearestNode(markerNode);
        double[] nodeCoords = graph.getNodeCoodinates(closestNodeIndex);

        return "" + nodeCoords[0] + "-" + nodeCoords[1];
    }

    @RequestMapping("/shortestPathFromTo/{start}/{target}")
    public String getShortestPathFromTo(@PathVariable double[] start, @PathVariable double[] target) {
        int startIndex = graph.getNearestNode(start);
        int targetIndex = graph.getNearestNode(target);

        return Pathing.getShortestPath(startIndex, targetIndex, graph);
    }


    @RequestMapping("/generateRoundtrip/{targetNodesLat}/{targetNodesLon}")
    public String generateRoundtripForGivenMarkers(@PathVariable double[] targetNodesLat, @PathVariable double[] targetNodesLon) {
        StringBuilder tspSolution = new StringBuilder();
        double[][] nodesToVisit = new double[targetNodesLat.length][2];
        for (int i = 0; i < targetNodesLat.length; i++) {
            nodesToVisit[i][0] = targetNodesLat[i];
            nodesToVisit[i][1] = targetNodesLon[i];
        }

        int numberOfNodesToVisit = nodesToVisit.length;
        int[][] costsBetweenNodes = generateCostsBetweenAllTargetNodes(numberOfNodesToVisit, nodesToVisit);
        ArrayList<Integer> result = new TravelingSalesmanProblem().computeTSP(costsBetweenNodes);

        int[] neighbourNodes = new int[numberOfNodesToVisit];
        for (int i = 0; i < numberOfNodesToVisit; i++) {
            neighbourNodes[i] = graph.getNearestNode(nodesToVisit[i]);
        }

        for (int i = 0; i < result.size() - 1; i++) {
            int startNode = neighbourNodes[result.get(i)];
            int targetNode = neighbourNodes[result.get(i + 1)];
            tspSolution.append(Pathing.getShortestPath(startNode, targetNode, graph)).append(",");
        }

        tspSolution = new StringBuilder(tspSolution.substring(0, tspSolution.length() - 1));
        return tspSolution.toString();
    }

    private int[][] generateCostsBetweenAllTargetNodes(int nodeCount, double[][] targetNodes) {
        int[][] costs = new int[nodeCount][nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (i == j) {
                    costs[i][j] = 0;
                } else {
                    costs[i][j] = (int) Distance.euclideanDistance(targetNodes[i][0], targetNodes[i][1], targetNodes[j][0],
                            targetNodes[j][1]);
                }
            }
        }
        return costs;
    }

}

