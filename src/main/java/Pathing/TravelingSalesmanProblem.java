package Pathing;

import java.util.ArrayList;
import java.util.Arrays;

public class TravelingSalesmanProblem {

    private ArrayList<Integer> tspSolution = new ArrayList<>();
    private int subPathCost[][], pickedNode[][], npow, numberNodes, edgeCost[][];

    public ArrayList<Integer> computeTSP(int[][] inputArray) {
        long start = System.currentTimeMillis();
        edgeCost = inputArray;

        numberNodes = inputArray.length;
        npow = (int) Math.pow(2, numberNodes);
        subPathCost = new int[numberNodes][npow];
        pickedNode = new int[numberNodes][npow];

        // init. with invalid value to check when to break out of recursion.
        for (int i = 0; i < numberNodes; i++) {
            Arrays.fill(subPathCost[i], -1);
            Arrays.fill(pickedNode[i], -1);
        }

        // init. first row of the table with costs from each edge i to start node.
        for (int i = 0; i < numberNodes; i++)
            subPathCost[i][0] = inputArray[i][0];

        tspRecursive(0, npow - 2);

        tspSolution.add(0);
        getPathRecursive(0, npow - 2);
        tspSolution.add(0);

        long end = System.currentTimeMillis();
        long time = (end - start) / 1000;
        System.out.println("TSP computation time: " + time + "s");

        return tspSolution;
    }

    private int tspRecursive(int start, int currentSet) {
        int cost = -1;
        int tempCost, newSet, nodeMask;

        if (subPathCost[start][currentSet] != -1) {
            return subPathCost[start][currentSet];
        } else {
            for (int node = 0; node < numberNodes; node++) {
                nodeMask = npow - 1 - (int) Math.pow(2, node);
                newSet = currentSet & nodeMask;
                if (newSet != currentSet) {
                    tempCost = edgeCost[start][node] + tspRecursive(node, newSet);
                    if (cost == -1 || cost > tempCost) {
                        cost = tempCost;
                        pickedNode[start][currentSet] = node;
                    }
                }
            }
            subPathCost[start][currentSet] = cost;
            return cost;
        }
    }

    private void getPathRecursive(int start, int currentSet) {
        if (pickedNode[start][currentSet] == -1)
            return;

        int node = pickedNode[start][currentSet];
        int nodeMask = npow - 1 - (int) Math.pow(2, node);
        int newSet = currentSet & nodeMask;

        tspSolution.add(node);
        getPathRecursive(node, newSet);
    }
}
