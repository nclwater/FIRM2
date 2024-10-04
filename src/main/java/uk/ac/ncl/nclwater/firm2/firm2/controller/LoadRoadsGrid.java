package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer;
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

    private static final Logger logger = LogManager.getLogger(LoadRoadsGrid.class);

    /**
     * Load road data and create a GraphStream network. Each road has three IDs, the road ID, the ID of the first
     * node in the road and the ID of the very last node in the road. Each road is a polyline composed of a series
     * of xy co-ordinates. A node is created for each set of co-ordinates, with the first node taking the second ID
     * as identifier and the last node taking the third ID as identifier. Intermediate node get the road ID with
     * an incrementing integer as a suffix eg. 1234567679.0, 1234567679.1 etc.
     * Edges are created for the GraphStream network between the co-ordinate pairs of the polyline.
     * @param properties The properties attribute that contains information for where the data files are stored
     * @return A GraphStream network of the roads
     */
    public static Graph loadRoads(Properties properties) {
        Graph graph = new SingleGraph("Road Networks GraphStream Test");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("ROADS_DATA");
        try {
            BNGRoads bngRoads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            RoadTypes roadTypes = LoadRoadTypes.loadRoadTypes(properties);
            int intNodeCount =0;
            int edgeCount = 0;
            if (bngRoads != null) {
                String roadID = null;
                for (BNGRoad road : bngRoads.getRoads()) {
                    roadID  = road.getRoadIDs()[0];
                    intNodeCount = 0;
                    edgeCount = 0;
                    ArrayList<PointDouble> points = road.getPolylineCoordinates();
                    String prevnode = null;
                    for (PointDouble point : points) {
                        String nodeID;
                        if (intNodeCount == 0) {
                            // FIRST NODE OF ROAD
                            nodeID = road.getRoadIDs()[1];
                        } else if (intNodeCount == points.size() - 1) {
                            // LAST NODE OF ROAD
                            nodeID = road.getRoadIDs()[2];
                        } else {
                            // INTERMEDIATE NODES OF ROAD
                            nodeID = road.getRoadIDs()[0] + "." + intNodeCount;
                        }
                        // If a node does not exist, add it
                        if (graph.getNode(nodeID) == null) {
                            graph.addNode(nodeID);
                            graph.getNode(nodeID).setAttribute("xyz", point.getX(), point.getY(), 0);
                            graph.getNode(nodeID).setAttribute("road-id", roadID);
                            graph.getNode(nodeID).setAttribute("road-type", road.getRoadType());
                            if (roadTypes == null) {
                                roadTypes = new RoadTypes();
                            }
                            graph.getNode(nodeID).setAttribute("speed-limit", roadTypes.getSpeed(road.getRoadType()));
                        }
                        intNodeCount++;

                        // Connect current node to the previous node
                        if (prevnode != null) { //  && !prevnode.equals(nodeID)
//                            logger.trace("Add edge from {} to {}", prevnode, nodeID);
                            graph.addEdge(roadID + "." + edgeCount + ".R", nodeID, prevnode, true);
//                            logger.trace("Add edge from {} to {}", nodeID, prevnode);
                            graph.addEdge(roadID + "." + edgeCount + ".F", prevnode, nodeID, true);
                            edgeCount++;

                        }

                        prevnode = nodeID;
                    }

                }
                logger.info("Graph node count: {}", graph.getNodeCount());
                logger.info("{} roads in file", bngRoads.getRoads().size());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return graph;
    }

    /**
     * Original method - not used anymore.
     * Read the roads.json configuration from file and populate the road grid
     */
    public static void loadRoadsOld(FloodModelParameters floodModelParameters, GlobalVariables globalVariables,
                                    Properties properties, SimpleGrid roadGrid, HashMap<String,
                                    ArrayList<Point>> roadHashMap) {
//         		;; manually fix up the bridge over the river.
//         		;; XXX this should be done from a config file.
//         		ask roads with [road-oid = "4000000012487984"] [set road-elevation 10]
//
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("ROADS_DATA");
            logger.info("Reading: {}", filename);
            BNGRoads roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            // for each of the roads
            roads.getRoads().forEach(r -> {
                // get all the nodes that make up that road
                ArrayList<PointDouble> roadPoints = r.getPolylineCoordinates();
                ArrayList<Point> wholeRoad = new ArrayList<>();
                ArrayList<Point> cleanedWholeRoad = new ArrayList<>();
                ArrayList<PointInteger> pixelPoints = new ArrayList<>();
                // convert BNG coordinates to grid xy
                // for each node in the road convert the BNG coordinates to Grid xy
                roadPoints.forEach(p -> {
                    PointInteger xy  = Utilities.BNG2GridXY(globalVariables.getLowerLeftX(),
                            globalVariables.getLowerLeftY(),
                            (float)p.getX(),
                            (float)p.getY(),
                            globalVariables.getCellSize());
                    // flip y horizontally
                    xy.setY(floodModelParameters.getHeight() - 1 - xy.getY());
                    pixelPoints.add(xy);
                });
                // interpolate between two points for visualisation purposes
                for (int i = 1; i < pixelPoints.size(); i++) {
                    wholeRoad.addAll(interpolate(pixelPoints.get(i - 1).getX(), pixelPoints.get(i - 1).getY(),
                            pixelPoints.get(i).getX(), pixelPoints.get(i).getY()));
                }
                wholeRoad.forEach(point -> {
                    // Check that road is withing bounds
                    if (point.x >= 0 && point.x < floodModelParameters.getWidth() && point.y >= 0 && point.y < floodModelParameters.getHeight()) {
                        Road newRoad = new Road(Integer.toString(AgentIDProducer.getNewId()), r.getRoadIDs());
                        newRoad.setRoadLength(r.getRoadLength());
                        newRoad.setRoadType(r.getRoadType());
                        roadGrid.setCell(point.x, point.y, newRoad);
                        cleanedWholeRoad.add(point);
                    }
                });
                roadHashMap.put(r.getRoadIDs()[0],cleanedWholeRoad);
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to load roads from json into a GraphStream network
     */
    public static BNGRoads gsLoadRoads(Graph graph, HashMap<String, BNGRoad> roadsMap, Properties properties) {
        Gson gson_roadTypes = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        BNGRoads roads = null;
        try {
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("ROADS_DATA");
            String filen = properties.getProperty("INPUT_DATA") + properties.getProperty("ROAD_TYPES");
            logger.info("Read roads from {}", filename);
            RoadTypes roadTypes = gson_roadTypes.fromJson(new FileReader(filen), RoadTypes.class);
            roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            for (BNGRoad bngroad : roads.getRoads()) {
                int nodeInc = 0;
                int edgeInc = 0;
                String prevID = bngroad.getRoadIDs()[1];
                // only for the very first road where prevID is null
                if (graph.getNode(prevID) == null) {
                    graph.addNode(prevID);
//                    logger.debug("Add road start node {} owned by road {}", prevID, bngroad.getRoadIDs()[0]);
                    // This node (prevID) belongs to this road (bngroad)
                    roadsMap.put(prevID, bngroad);

                    graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(0).getX(),
                            bngroad.getPolylineCoordinates().get(0).getY(), 0);
                    setAttributes(graph, roadTypes, bngroad);

                }
                // for each road add all the xy co-ordinates in the file as a node
                // use the road ID plus a number as the ID of the node
                // add and edge between the previous node and the current node and
                // use the road ID plus a number as the ID of the edge
                for (int roadsection = 1; roadsection < bngroad.getPolylineCoordinates().size() - 1; roadsection++) {
                    String nodeID = bngroad.getRoadIDs()[0] + "." + nodeInc++;
                    if (graph.getNode(nodeID) == null) {
                        graph.addNode(nodeID);
//                        logger.debug("Add intermediate node {} owned by {} with a speedlimit of {}",
//                                nodeID, bngroad.getRoadIDs()[0], bngroad.getRoadSpeedLimit());
                        // This node (nodeID) belongs to this road (bngroad)
                        roadsMap.put(nodeID, bngroad);

                        graph.getNode(nodeID).setAttribute("xyz",
                                bngroad.getPolylineCoordinates().get(roadsection).getX(),
                                bngroad.getPolylineCoordinates().get(roadsection).getY(), 0);
                        setAttributes(graph, roadTypes, bngroad);

                        String edgeID = bngroad.getRoadIDs()[0] + edgeInc++;
                        // add forward edge
                        graph.addEdge(edgeID, graph.getNode(prevID),
                                graph.getNode(nodeID), true);
                        // add reverse edge
                        graph.addEdge(edgeID + "R",
                                graph.getNode(nodeID), graph.getNode(prevID), true);
                        graph.getEdge(edgeID + "R").setAttribute("road-type", bngroad.getRoadType());
                        prevID = nodeID;
                    }
                }
                // Add the last node in the road graph
                int last = bngroad.getPolylineCoordinates().size() - 1;
                if (graph.getNode(bngroad.getRoadIDs()[2]) == null) {
                    graph.addNode(bngroad.getRoadIDs()[2]);
//                    logger.trace("Add end node {} owned by {} with a speedlimit of {}", bngroad.getRoadIDs()[2],
//                            bngroad.getRoadIDs()[0], bngroad.getRoadSpeedLimit());

                    graph.getNode(bngroad.getRoadIDs()[2]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(last).getX(),
                            bngroad.getPolylineCoordinates().get(last).getY(), 0);
                    setAttributes(graph, roadTypes, bngroad);


                }
                String edgeID = bngroad.getRoadIDs()[0] + "." + edgeInc;
                // add forward edge
                graph.addEdge(edgeID, graph.getNode(prevID),
                        graph.getNode(bngroad.getRoadIDs()[2]), true);
                graph.getEdge(edgeID).setAttribute("speed", bngroad.getRoadType());
                // add reverse edge
                graph.addEdge(edgeID + "R",
                        graph.getNode(bngroad.getRoadIDs()[2]), graph.getNode(prevID), true);
//                graph.getEdge(edgeID + "R").setAttribute("road-type",bngroad.getRoadType());

            }
        } catch (FileNotFoundException e) {
            logger.error("File not found.");
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
        }
        return roads;

    }

    private static void setAttributes(Graph graph, RoadTypes roadTypes, BNGRoad bngroad) {
        graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("road-id", bngroad.getRoadIDs()[0]);
        graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("road-type", bngroad.getRoadType());
        graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("owning-road", bngroad.getRoadIDs()[0]);
        int speedLimit = roadTypes.getSpeed(bngroad.getRoadType());
        bngroad.setRoadSpeedLimit(speedLimit);
        graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("speed-limit", speedLimit);
    }


}
