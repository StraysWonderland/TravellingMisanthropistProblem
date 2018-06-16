package GraphStructure;

import Data.FilePaths;

import java.io.*;

public class Graph {
    private int[][] edges;
    private double[][] nodes;
    private int[] offsets;

    int nodeCount = 0;
    int edgeCount = 0;
    int amenityCount = 0;

    public void graphFromBinaries() throws IOException, ClassNotFoundException {
        try {
            // Read edges
            FileInputStream fis = new FileInputStream(FilePaths.binBWEdges );
            ObjectInputStream ois = new ObjectInputStream(fis);
            edges = (int[][]) ois.readObject();

            // Read nodes
            fis = new FileInputStream(FilePaths.binBWNodes);
            ois = new ObjectInputStream(fis);
            nodes = (double[][])ois.readObject();

            //fis = new FileInputStream(path + "offsets");
            //ois = new ObjectInputStream(fis);
            //offsets = (int[]) ois.readObject();

            edgeCount = edges[0].length;
            nodeCount = nodes[0].length;

            // generateNodeGrid();

            fis.close();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
