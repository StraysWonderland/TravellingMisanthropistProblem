import GraphStructure.Graph;
import GraphStructure.GraphParserPBF;
import Pathing.Pathing;
import Pathing.TravelingSalesmanProblem;
import Util.Distance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.ArrayList;

@Controller
@EnableAutoConfiguration
public class Main {

    private static final String template = "Hello, %s!";
    private static Graph graph;

    @RequestMapping("/greeting")
    @ResponseBody
    public String greeting(@RequestParam(value = "name", defaultValue = "Fuckface") String name) {
        return String.format(template, name);
    }

    // start by parsing the graph; then launch springboot
    public static void main(String[] args) {
        GraphParserPBF graphParserPBF = new GraphParserPBF();
        try {
            graphParserPBF.retrieveAmenityPOIs();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // graphParserPBF.parseFromPbf();

        graph = new Graph();
        graph.loadMapData();

        SpringApplication.run(Main.class, args);
    }

    @RequestMapping("/calc/{targetNodesLat}/{targetNodesLon:.+}")
    public String calculateRoundTrip(@PathVariable double[] targetNodesLat, @PathVariable double[] targetNodesLon) {
        double[][] visitNodes = new double[targetNodesLat.length][2];
        for (int i = 0; i < targetNodesLat.length; i++) {
            visitNodes[i][0] = targetNodesLat[i];
            visitNodes[i][1] = targetNodesLon[i];
        }

        int nodeCount = visitNodes.length;
        int[][] costs = calculateCosts(nodeCount, visitNodes);
        ArrayList<Integer> result = new TravelingSalesmanProblem().computeTSP(costs);

        int[] nodes = new int[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = graph.getNearestNode(visitNodes[i]);
        }

        StringBuilder solution = new StringBuilder();
        for (int i = 0; i < result.size() - 1; i++) {
            int startNode = nodes[result.get(i)];
            int targetNode = nodes[result.get(i + 1)];
            solution.append(Pathing.getShortestPath(startNode, targetNode, graph)).append(",");
        }
        solution = new StringBuilder(solution.substring(0, solution.length() - 1));

        return solution.toString();
    }

    private int[][] calculateCosts(int nodeCount, double[][] targetNodes) {
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