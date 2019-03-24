package strayswonderland.TravelingMisanthropistProblem.Pathing;

import strayswonderland.TravelingMisanthropistProblem.GraphStructure.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Pathing {

    public static String getShortestPath(int start, int dest, Graph graph) {


        if (start == dest)
            return "STOP: Start equals Destination";

        int startNode[] = {start, 0};
        int[] dist = new int[graph.edgeSource.length];
        int[] prev = new int[graph.edgeSource.length];

        Queue<int[]> priorityNodes = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        priorityNodes.add(startNode);

        while (!priorityNodes.isEmpty()) {
            int[] currentNode = priorityNodes.poll();
            assert currentNode != null;
            if (currentNode[0] == dest) {
                break;
            }
            int offset = graph.offsets[currentNode[0]];
            if (graph.offsets[currentNode[0]] != -1) {
                while (graph.edgeSource[offset] == currentNode[0]) {
                    int newDist = dist[graph.edgeSource[offset]] + graph.edgeDistance[offset];
                    if (dist[graph.edgeTarget[offset]] == 0 || newDist < dist[graph.edgeTarget[offset]]) {
                        dist[graph.edgeTarget[offset]] = newDist;
                        prev[graph.edgeTarget[offset]] = graph.edgeSource[offset];
                        int newNode[] = {graph.edgeTarget[offset], newDist};
                        priorityNodes.add(newNode);
                    }
                    offset++;
                    if (offset >= graph.edgeSource.length) {
                        break;
                    }
                }
            }
        }
        if (prev[dest] == 0)
            return "Could not find a path from " + start + " to  " + dest;

        return getPathing(start, dest, prev, graph.nodes);

    }

    private static String getPathing(int start, int target, int[] prev, double[][] nodes) {
        ArrayList<Integer> solutionPath = new ArrayList<>();
        int prevNode = prev[target];
        solutionPath.add(prevNode);
        while (prevNode != start) {
            prevNode = prev[prevNode];
            solutionPath.add(prevNode);
        }
        StringBuilder solution = new StringBuilder(nodes[0][target] + "_" + nodes[1][target]);
        for (Integer aSolutionPath : solutionPath) {
            solution.insert(0, nodes[0][aSolutionPath] + "_" + nodes[1][aSolutionPath] + ",");
        }
        solution.append(target);
        return solution.toString();
    }


}
