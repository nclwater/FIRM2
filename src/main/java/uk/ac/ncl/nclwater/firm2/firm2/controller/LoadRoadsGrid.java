package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

public class LoadRoadsGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadRoadsGrid.class);

    /**
     * Read the roads.json configuration from file and populate the road grid
     */
    public static void loadRoads(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                 Properties properties, Graph graph, HashMap<String, ArrayList<Point>> roadHashMap) {
//         		;; manually fix up the bridge over the river.
//         		;; XXX this should be done from a config file.
//         		ask roads with [road-oid = "4000000012487984"] [set road-elevation 10]
//
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("roads-data");
            logger.debug("Reading: {}", filename);
            Roads roads = gson.fromJson(new FileReader(filename), Roads.class);
            roads.getRoads().forEach(bngroad -> {
                System.out.println(("0: " + bngroad.getRoadIDs()[0] + ", 1: " + bngroad.getRoadIDs()[1]) +
                        ", 2: " + bngroad.getRoadIDs()[2]);
                int nodeInc = 0;
                int edgeInc = 0;
                PointDouble road = bngroad.getPolylineCoordinates().get(1);
                String prevID = bngroad.getRoadIDs()[1];
                if (graph.getNode(prevID) == null) {
                    graph.addNode(prevID);
                    graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().getFirst().getX(),
                            bngroad.getPolylineCoordinates().getFirst().getY(), 0);
                }
                System.out.print(prevID + ", ");
                // for each road add all the xy co-ordinates in the file as a node
                // use the road ID plus a number as the ID of the node
                // add and edge between the previous node and the current node and
                // use the road ID plus a number as the ID of the edge
                for (int roadsection = 1; roadsection < bngroad.getPolylineCoordinates().size() - 2; roadsection++) {
                    String nodeID = bngroad.getRoadIDs()[0] + "." + nodeInc++;
                    System.out.print(nodeID + ", ");
                    graph.addNode(nodeID);
                    graph.getNode(nodeID).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(roadsection).getX(),
                            bngroad.getPolylineCoordinates().get(roadsection).getY(), 0);
                    graph.addEdge(bngroad.getRoadIDs()[0] + edgeInc++, graph.getNode(prevID),
                            graph.getNode(nodeID));
                    prevID = nodeID;
                }
                int last = bngroad.getPolylineCoordinates().size() - 1;
                if (graph.getNode(bngroad.getRoadIDs()[2]) == null) {
                    graph.addNode(bngroad.getRoadIDs()[2]);
                    graph.getNode(bngroad.getRoadIDs()[2]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(last).getX(),
                            bngroad.getPolylineCoordinates().get(last).getY(), 0);
                }
                graph.addEdge(bngroad.getRoadIDs()[0] + "." + edgeInc, graph.getNode(prevID),
                        graph.getNode(bngroad.getRoadIDs()[2]));
                System.out.println(bngroad.getRoadIDs()[2]);
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadBNGRoads() {

    }


}
