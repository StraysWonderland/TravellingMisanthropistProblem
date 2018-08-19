package Pathing;
import GraphStructure.Graph;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;

public class Pathing {

    public static String getShortestPath(int start, int dest, Graph graph) {
        double[][] nodes = graph.getNodes();
        int[][] edges = graph.getEdges();
        int[] offsets = graph.getOffsets();

        if (start == dest) {
            return "Start == Destination";
        } else {
            Queue<int[]> priorityNodes = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            int[] dist = new int[edges[0].length];
            int[] prev = new int[edges[0].length];
            int startNode[] = { start, 0 };
            priorityNodes.add(startNode);
            while (!priorityNodes.isEmpty()) {
                int[] currentNode = priorityNodes.poll();
                assert currentNode != null;
                if (currentNode[0] == dest) {
                    break;
                }
                int offset = offsets[currentNode[0]];
                if (offsets[currentNode[0]] != -1) {
                    while (edges[0][offset] == currentNode[0]) {
                        int newDist = dist[edges[0][offset]] + edges[2][offset];
                        if (dist[edges[1][offset]] == 0 || newDist < dist[edges[1][offset]]) {
                            dist[edges[1][offset]] = newDist;
                            prev[edges[1][offset]] = edges[0][offset];
                            int newNode[] = { edges[1][offset], newDist };
                            priorityNodes.add(newNode);
                        }
                        offset++;
                        if (offset >= edges[0].length) {
                            break;
                        }
                    }
                }
            }
            if (prev[dest] == 0) {
                return "Path not found: " + start + " to  " + dest;
            }
            return getPathing(start, dest, prev, nodes);
        }
    }

    private static String getPathing(int start, int dest, int[] prev, double[][] nodes) {
        ArrayList<Integer> solutionPath = new ArrayList<>();
        int prevNode = prev[dest];
        solutionPath.add(prevNode);
        while (prevNode != start) {
            prevNode = prev[prevNode];
            solutionPath.add(prevNode);
        }
        StringBuilder solution = new StringBuilder(nodes[0][dest] + "_" + nodes[1][dest]);
        for (Integer aSolutionPath : solutionPath) {
            solution.insert(0, nodes[0][aSolutionPath] + "_" + nodes[1][aSolutionPath] + ",");
        }
        solution.append(dest);
        return solution.toString();
    }


}
