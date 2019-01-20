package strayswonderland.TravelingMisanthropistProblem.Pathing;

import java.util.ArrayList;
import java.util.Arrays;

public class TravelingSalesmanProblem {

    private ArrayList<Integer> tspSolution = new ArrayList<>();
    private int subPathCost[][], chosenNode[][], expo, numberOfNodes, edgeWeights[][];

    public ArrayList<Integer> computeTSP(int[][] inputArray) {
        edgeWeights = inputArray;
        numberOfNodes = inputArray.length;
        expo = (int) Math.pow(2, numberOfNodes);
        subPathCost = new int[numberOfNodes][expo];
        chosenNode = new int[numberOfNodes][expo];

        for (int i = 0; i < numberOfNodes; i++) {
            Arrays.fill(subPathCost[i], -1);
            Arrays.fill(chosenNode[i], -1);
        }

        for (int i = 0; i < numberOfNodes; i++)
            subPathCost[i][0] = inputArray[i][0];

        TravelingSalesmanProblemCalculation(0, expo - 2);

        tspSolution.add(0);
        pathCalculation(0, expo - 2);
        tspSolution.add(0);

        return tspSolution;
    }

    private int TravelingSalesmanProblemCalculation(int start, int currentSet) {
        int cost = -1;
        int tempCost, newSet, nodeMask;

        if (subPathCost[start][currentSet] != -1)
            return subPathCost[start][currentSet];

        for (int node = 0; node < numberOfNodes; node++) {
            nodeMask = expo - 1 - (int) Math.pow(2, node);
            newSet = currentSet & nodeMask;
            if (newSet != currentSet) {
                tempCost = edgeWeights[start][node] + TravelingSalesmanProblemCalculation(node, newSet);
                if (cost == -1 || cost > tempCost) {
                    cost = tempCost;
                    chosenNode[start][currentSet] = node;
                }
            }
        }

        subPathCost[start][currentSet] = cost;
        return cost;

    }

    private void pathCalculation(int start, int currentSet) {
        if (chosenNode[start][currentSet] == -1)
            return;

        int node = chosenNode[start][currentSet];
        int nodeMask = expo - 1 - (int) Math.pow(2, node);
        int newSet = currentSet & nodeMask;

        tspSolution.add(node);
        pathCalculation(node, newSet);
    }
}
