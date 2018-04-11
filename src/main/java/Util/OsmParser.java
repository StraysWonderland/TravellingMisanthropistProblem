package Util;

import de.topobyte.osm4j.core.access.OsmIterator;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.pbf.seq.PbfIterator;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class OsmParser {
    private final String pbfPath = "baden-wuerttemberg-latest.osm.pbf";
    private final String binaryPath = "baden-wuerttemberg-map";

    private int[][] edges;
    private double[][] vertices;
    private int[] offsets;


    private void readFromByteFile(String graphPath) throws FileNotFoundException {
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
// region unused
    /*
    public void parseFromPBF() throws FileNotFoundException {
        InputStream stream = null;

        try {
            int edgeCount = 0;
            int nodeCount = -1;

            stream = new FileInputStream(pbfPath);
            OsmIterator iterator = new PbfIterator(stream, true);
            for (EntityContainer container : iterator) {
                if (container.getType() == EntityType.Way) {
                    OsmWay currentEdge = (OsmWay) container.getEntity();
                    for (int i = 0; i < currentEdge.getNumberOfNodes() - 1; i++) {
                        edges[0][edgeCount] = currentEdge.getNodeId(i);
                        edges[1][edgeCount] = currentEdge.getNodeId(i + 1);
                        edges[2][edgeCount] = 60;

                        System.out.println(String.format("%-15s %-15s %-15s",
                                edges[0][edgeCount],
                                edges[1][edgeCount],
                                edges[2][edgeCount]
                        ));
                        edgeCount++;
                    }

                }
                numEdges = edgeCount;
            }

            iterator = new PbfIterator(stream, true);
            nodeCount = 0;
            for (EntityContainer container : iterator) {
                if (container.getType() == EntityType.Node) {
                    OsmNode currentNode = (OsmNode) container.getEntity();
                    if (nodeLookup.containsKey(currentNode.getId())) {
                        nodes[0][nodeCount] = currentNode.getId();
                    }
                    nodeCount++;
                }
            }
            numNodes = nodeCount;
            numEdges = edgeCount;
        } catch (
                FileNotFoundException e)

        {
            e.printStackTrace();
            System.out.println("File could not be found!");
        } finally

        {
            try {
                stream.close();
                System.out.println(String.format("nodes: %-15s edges: %-15s",
                        numNodes,
                        numEdges
                ));
            } catch (IOException e) {
                System.out.println("Closing of InputStream failed!");
            }
        }
    }

            for (EntityContainer container : iterator) {
                switch (container.getType()) {
                    default:
                    case Node:

                        OsmNode node = (OsmNode) container.getEntity();
                        Map<String, String> tags = OsmModelUtil.getTagsAsMap(node);

                        // Check if this is a pub
                        String amenity = tags.get("amenity");
                        boolean isPub = amenity != null
                                && amenity.equals("pub");
                        if (isPub) {
                            System.out.println(String.format("%-15s %-40s %-15f %-15f",
                                    node.getId(),
                                    tags.get("name"),
                                    node.getLatitude(),
                                    node.getLongitude()
                            ));
                        }
            }





     // edges[2][edgeCount] = OsmModelUtil.getTagsAsMap(currentEdge).get("maxspeed") != null ? Integer.parseInt(OsmModelUtil.getTagsAsMap(currentEdge).get("maxspeed").replaceAll("[^\\w\\s\\.]", "").) : 30;

*/
//endregion