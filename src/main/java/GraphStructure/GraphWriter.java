package GraphStructure;

import java.io.*;
import java.util.ArrayList;

public class GraphWriter {

    public static void WriteToLineFile(int[][] edges, double[][] nodes, String nodesPath, String edgesPath) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(nodesPath, "UTF-8");
        writer.println(nodes[0].length);
        for (int i = 0; i < nodes[0].length; i++) {
            writer.print((int) nodes[0][i] + " ");
            writer.print(nodes[1][i] + " ");
            writer.println(nodes[2][i]);
        }
        writer.close();

        writer = new PrintWriter(edgesPath, "UTF-8");
        writer.println(edges.length);
        for (int[] edge : edges) {
            writer.print(edge[1] + " ");
            writer.print(edge[2] + " ");
            writer.println(edge[3]);
        }
        writer.close();
    }

    private void writeAmenities(ArrayList<String[]> amenities, ArrayList<Double[]> amenityLatLon, String amenityPath) throws IOException {

        FileWriter fileWriter = new FileWriter(amenityPath);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.write(String.valueOf(amenities.size()) + "\n");

        for (int i = 0; i < amenities.size(); i++) {
            printWriter.write(String.valueOf(amenityLatLon.get(i)[0]) + " "
                    + String.valueOf(amenityLatLon.get(i)[1]) + " "
                    + String.valueOf(amenities.get(i)[0]) + " "
                    + String.valueOf(amenities.get(i)[1]) + "\n"
            );
        }
        printWriter.close();
    }

    public static void serializeGraph(int[][] edges, double[][] nodes, String edgePath, String nodesPath) {
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
