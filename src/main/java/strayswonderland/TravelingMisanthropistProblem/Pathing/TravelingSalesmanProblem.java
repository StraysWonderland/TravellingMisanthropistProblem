package strayswonderland.TravelingMisanthropistProblem.Pathing;

import java.util.ArrayList;
import java.util.Arrays;

public class TravelingSalesmanProblem {

    private int pathCosts[][];
    private int chosenNode[][];
    private int exponent;
    private int numberOfNodes;
    private int edgeWeights[][];
    private ArrayList<Integer> tspSolution = new ArrayList<>();

    public ArrayList<Integer> computeTSP(int[][] inputArray) {
        edgeWeights = inputArray;
        numberOfNodes = inputArray.length;
        exponent = (int) Math.pow(2, numberOfNodes);
        pathCosts = new int[numberOfNodes][exponent];
        chosenNode = new int[numberOfNodes][exponent];

        for (int i = 0; i < numberOfNodes; i++) {
            Arrays.fill(pathCosts[i], -1);
            Arrays.fill(chosenNode[i], -1);
        }

        for (int i = 0; i < numberOfNodes; i++)
            pathCosts[i][0] = inputArray[i][0];

        TravelingSalesmanProblemCalculation(0, exponent - 2);

        tspSolution.add(0);
        recursivePathCalculation(0, exponent - 2);
        tspSolution.add(0);

        return tspSolution;
    }

    private int TravelingSalesmanProblemCalculation(int start, int currentSet) {
        int totalCost = -1;
        int currentCost;
        int nodeSet;
        int nodeMask;

        if (pathCosts[start][currentSet] != -1)
            return pathCosts[start][currentSet];

        for (int node = 0; node < numberOfNodes; node++) {
            nodeMask = exponent - 1 - (int) Math.pow(2, node);
            nodeSet = currentSet & nodeMask;
            if (nodeSet != currentSet) {
                currentCost = edgeWeights[start][node] + TravelingSalesmanProblemCalculation(node, nodeSet);
                if (totalCost == -1 || totalCost > currentCost) {
                    totalCost = currentCost;
                    chosenNode[start][currentSet] = node;
                }
            }
        }
        pathCosts[start][currentSet] = totalCost;
        return totalCost;
    }

    private void recursivePathCalculation(int start, int currentSet) {
        if (chosenNode[start][currentSet] == -1)
            return;

        int currentNode = chosenNode[start][currentSet];
        int nodeMask = exponent - 1 - (int) Math.pow(2, currentNode);
        int newSet = currentSet & nodeMask;

        tspSolution.add(currentNode);
        recursivePathCalculation(currentNode, newSet);
    }
}
