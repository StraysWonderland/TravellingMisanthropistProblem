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

        graph = new Graph();

        System.out.println(" PLEASE WAIT: LOADING MAP DATA FROM FILE. APPLICATION WILL RUN SHORTLY");
        graph.loadMapData();

        SpringApplication.run(TravelingMisanthropistProblemApplication.class, args);
    }


    @RequestMapping("/getClosestNodeToMarker/{markerNode}")
    String getClosestNodeToMarker(@PathVariable double[] markerNode) {

        int closestNodeIndex = graph.getNearestNode(markerNode);
        double[] nodeCoords = graph.getNodeCoodinates(closestNodeIndex);

        return "" + nodeCoords[0] + "-" + nodeCoords[1];
    }

    @RequestMapping("/shortestPathFromTo/{start}/{target}")
    String getShortestPathFromTo(@PathVariable double[] start, @PathVariable double[] target) {

        int startIndex = graph.getNearestNode(start);
        int targetIndex = graph.getNearestNode(target);

        double[][] visitNodes = new double[2][2];
        visitNodes[0] = start;
        visitNodes[1] = target;

        String pathString = Pathing.getShortestPath(startIndex, targetIndex, graph);
        return pathString;
    }


    @RequestMapping("/generateRoundtrip/{targetNodesLat}/{targetNodesLon}")
    public String generateRoundtripForGivenMarkers(@PathVariable double[] targetNodesLat, @PathVariable double[] targetNodesLon) {

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

        StringBuilder solution = new StringBuilder();
        for (int i = 0; i < result.size() - 1; i++) {
            int startNode = neighbourNodes[result.get(i)];
            int targetNode = neighbourNodes[result.get(i + 1)];
            solution.append(Pathing.getShortestPath(startNode, targetNode, graph)).append(",");
        }
        solution = new StringBuilder(solution.substring(0, solution.length() - 1));

        return solution.toString();
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

