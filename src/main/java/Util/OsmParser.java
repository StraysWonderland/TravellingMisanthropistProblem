package Util;

import Data.FilePaths;

import java.io.*;
import java.util.Arrays;

public class OsmParser {
    // TODO: change to germany when needed
    private final String pbfPath = FilePaths.pbfBW;

    private int[][] edges;
    private double[][] vertices;
    private int[] offsets;


    private void readFromByteFile(String graphPath) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(graphPath);
            ObjectInputStream o = new ObjectInputStream(fis);
            edges = (int[][]) o.readObject();
            vertices = (double[][]) o.readObject();
            offsets = (int[]) o.readObject();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File could not be found!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File could not be read!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("File contains wrong object type!");
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println("Closing of InputStream failed!");
            }
            System.exit(5);
        }
    }

    private void exportToByteFile(String exportPath) {
        OutputStream fos = null;

        try {
            fos = new FileOutputStream(exportPath);
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(edges);
            o.writeObject(vertices);
            o.writeObject(offsets);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not export graph file!");
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not close output stream!");
            }
        }
    }

    public void readFromTextFile() throws IOException, NullPointerException {
        BufferedReader bf = new BufferedReader(new FileReader(pbfPath));
        int verticesCount;
        int edgesCount;
        // Read number of vertices and create an array with the same size to store them.
        String line = bf.readLine();
        verticesCount = Integer.parseInt(line);
        vertices = new double[verticesCount][2];
        // Read number of edges and create an array with the same size to store them.
        line = bf.readLine();
        edgesCount = Integer.parseInt(line);
        edges = new int[edgesCount][3];
        // Create offset array and init with -1 (no outgoing edge)
        offsets = new int[verticesCount];
        Arrays.fill(offsets, -1);
        line = bf.readLine();
        int counterVertices = 0;
        int counterEdges = 0;
        // Read all vertices from file. Store geographic coordinates in 'vertices' array
        while (counterVertices < verticesCount) {
            String vertexData[] = line.split(" ");
            vertices[counterVertices][0] = Double.parseDouble(vertexData[2]);
            vertices[counterVertices][1] = Double.parseDouble(vertexData[3]);
            counterVertices++;
            line = bf.readLine();
        }
        // Read all edges from file.
        counterVertices = -1;
        while (counterEdges < edgesCount) {
            String edgeData[] = line.split(" ");
            // Store start-node, dest-node and weighting in 'edges' array.
            edges[counterEdges][0] = Integer.parseInt(edgeData[0]);
            edges[counterEdges][1] = Integer.parseInt(edgeData[1]);
            edges[counterEdges][2] = Integer.parseInt(edgeData[2]);
            /*
             * Checks if the current edge starts from a new vertex and if so, stores the offset of
             * that new vertex in the 'offsets' array. 'counterVertices' stores the ID of the
             * current start node. So if the next edge starts from a new node, 'counterVertices <
             * edges[counterEdges][0]' is true (since edges are sorted by their starting node).
             */
            if (counterVertices < edges[counterEdges][0]) {
                counterVertices = edges[counterEdges][0];
                offsets[counterVertices] = counterEdges;
            }
            counterEdges++;
            line = bf.readLine();
        }
        bf.close();
    }
}
