package strayswonderland.TravelingMisanthropistProblem.Pathing;

import strayswonderland.TravelingMisanthropistProblem.GraphStructure.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Pathing {

    public static String getShortestPath(int start, int target, Graph graph) {
        if (start == target)
            return "STOP: Start equals Destination";

        int[] distance = new int[graph.edgeSource.length];
        int[] prev = new int[graph.edgeSource.length];
        int startNode[] = {start, 0};

        Queue<int[]> priorityNodes = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        priorityNodes.add(startNode);

        while (!priorityNodes.isEmpty()) {
            int[] currentNode = priorityNodes.poll();
            assert currentNode != null;

            if (currentNode[0] == target)
                break;

            int offset = graph.offsets[currentNode[0]];

            if (graph.offsets[currentNode[0]] == -1)
                continue;

            while (graph.edgeSource[offset] == currentNode[0]) {
                int newDist = distance[graph.edgeSource[offset]] + graph.edgeDistance[offset];
                if (distance[graph.edgeTarget[offset]] == 0 || newDist < distance[graph.edgeTarget[offset]]) {
                    distance[graph.edgeTarget[offset]] = newDist;
                    prev[graph.edgeTarget[offset]] = graph.edgeSource[offset];
                    int newNode[] = {graph.edgeTarget[offset], newDist};
                    priorityNodes.add(newNode);
                }
                offset++;

                if (offset >= graph.edgeSource.length)
                    break;
            }
        }

        if (prev[target] == 0)
            return "Could not find a path from " + start + " to  " + target;

        return getPathing(start, target, prev, graph.nodes);
    }

    private static String getPathing(int start, int target, int[] previousNodes, double[][] nodes) {
        ArrayList<Integer> totalPath = new ArrayList<>();
        int previousNod = previousNodes[target];
        totalPath.add(previousNod);

        while (previousNod != start) {
            previousNod = previousNodes[previousNod];
            totalPath.add(previousNod);
        }

        StringBuilder solution = new StringBuilder(nodes[0][target] + "_" + nodes[1][target]);

        for (Integer aSolutionPath : totalPath) {
            solution.insert(0, nodes[0][aSolutionPath] + "_" + nodes[1][aSolutionPath] + ",");
        }

        solution.append(target);
        return solution.toString();
    }

}
