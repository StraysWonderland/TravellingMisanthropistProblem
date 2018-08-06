package GraphStructure;

import java.io.*;

public class GraphWriter {

    public static void WriteToLineFile(int[][] edges, double[][] nodes, String nodesPath, String edgesPath) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(nodesPath,
                "UTF-8");
        writer.println(nodes[0].length);
        for (int i = 0; i < nodes[0].length; i++) {
            writer.print((int) nodes[0][i] + " ");
            writer.print(nodes[1][i] + " ");
            writer.println(nodes[2][i]);
        }
        writer.close();
        writer = new PrintWriter(edgesPath,
                "UTF-8");
        writer.println(edges.length);
        for (int i = 0; i < edges.length; i++) {
            writer.print(edges[i][1] + " ");
            writer.print(edges[i][2] + " ");
            writer.println(edges[i][3]);
        }
        writer.close();
    }

    public static void serializeGraph(int[][] edges, double[][] nodes,String edgePath, String nodesPath) {
        try {
            FileOutputStream fos = new FileOutputStream(edgePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(edges);
            oos.close();

            fos = new FileOutputStream(nodesPath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(nodes);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
