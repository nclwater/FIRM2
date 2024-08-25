package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoads;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

public class LoadRoadsGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadRoadsGrid.class);

    /**
     * Read the roads.json configuration from file and populate the road grid
     */
    public static void loadRoads(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                 Properties properties, Graph graph) {
//         		;; manually fix up the bridge over the river.
//         		;; XXX this should be done from a config file.
//         		ask roads with [road-oid = "4000000012487984"] [set road-elevation 10]
//
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("ROADS_DATA");
            logger.debug("Reading: {}", filename);
            BNGRoads roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            roads.getRoads().forEach(bngroad -> {
                int nodeInc = 0;
                int edgeInc = 0;
                PointDouble road = bngroad.getPolylineCoordinates().get(1);
                String prevID = bngroad.getRoadIDs()[1];
                if (graph.getNode(prevID) == null) {
                    graph.addNode(prevID);
                    graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(0).getX(),
                            bngroad.getPolylineCoordinates().get(0).getY(), 0);
                }
                // for each road add all the xy co-ordinates in the file as a node
                // use the road ID plus a number as the ID of the node
                // add and edge between the previous node and the current node and
                // use the road ID plus a number as the ID of the edge
                for (int roadsection = 1; roadsection < bngroad.getPolylineCoordinates().size() - 2; roadsection++) {
                    String nodeID = bngroad.getRoadIDs()[0] + "." + nodeInc++;
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
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method is used to load roads from json into a GraphStream network
     */
    public static BNGRoads gsLoadRoads(Graph graph, HashMap<String, BNGRoad> roadsMap, Properties properties) {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        BNGRoads roads = null;
        try {
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("ROADS_DATA");
            logger.debug("Read roads from {}", filename);
            roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            roads.getRoads().forEach(bngroad -> {
                int nodeInc = 0;
                int edgeInc = 0;
                PointDouble road = bngroad.getPolylineCoordinates().get(1);
                String prevID = bngroad.getRoadIDs()[1];
                if (graph.getNode(prevID) == null) {
                    graph.addNode(prevID);
                    logger.trace("Add start node {}", prevID);
                    // This node (prevID) belongs to this road (bngroad)
                    roadsMap.put(prevID, bngroad);
                    graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(0).getX(),
                            bngroad.getPolylineCoordinates().get(0).getY(), 0);
                }
                // for each road add all the xy co-ordinates in the file as a node
                // use the road ID plus a number as the ID of the node
                // add and edge between the previous node and the current node and
                // use the road ID plus a number as the ID of the edge
                for (int roadsection = 1; roadsection < bngroad.getPolylineCoordinates().size() - 2; roadsection++) {
                    String nodeID = bngroad.getRoadIDs()[0] + "." + nodeInc++;
                    if (graph.getNode(nodeID) == null) {
                        graph.addNode(nodeID);
                        logger.trace("Add intermediate node {}", nodeID);
                        // This node (nodeID) belongs to this road (bngroad)
                        roadsMap.put(nodeID, bngroad);
                        graph.getNode(nodeID).setAttribute("xyz",
                                bngroad.getPolylineCoordinates().get(roadsection).getX(),
                                bngroad.getPolylineCoordinates().get(roadsection).getY(), 0);
                        String edgeID = bngroad.getRoadIDs()[0] + edgeInc++;
                        graph.addEdge(edgeID, graph.getNode(prevID),
                                graph.getNode(nodeID), true);
                        prevID = nodeID;
                    }
                }
                int last = bngroad.getPolylineCoordinates().size() - 1;
                if (graph.getNode(bngroad.getRoadIDs()[2]) == null) {
                    graph.addNode(bngroad.getRoadIDs()[2]);
                    logger.trace("Add end node {}", bngroad.getRoadIDs()[2]);
                    graph.getNode(bngroad.getRoadIDs()[2]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(last).getX(),
                            bngroad.getPolylineCoordinates().get(last).getY(), 0);
                }
                String edgeID = bngroad.getRoadIDs()[0] + "." + edgeInc;
                graph.addEdge(edgeID, graph.getNode(prevID),
                        graph.getNode(bngroad.getRoadIDs()[2]), true);

            });
        } catch (FileNotFoundException e) {
            logger.debug("File not found.");
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            logger.debug(e.getMessage());
        }
        return roads;

    }



}
