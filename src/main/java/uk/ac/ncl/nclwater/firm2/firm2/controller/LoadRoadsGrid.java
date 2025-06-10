/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

public class LoadRoadsGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadRoadsGrid.class);

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
        // FQN used here because Path is redefined by the graphstream package
        java.nio.file.Path path = Paths.get(properties.getProperty("INPUT_DATA"), properties.getProperty("ROADS_DATA"));
        String filename = path.toString();
        try {
            BNGRoads bngRoads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            RoadTypes roadTypes = LoadRoadTypes.loadRoadTypes(properties);
            int intNodeCount =0;
            int edgeCount = 0;
            int roadSpeed = 0;
            if (bngRoads != null) {
                String roadID = null;
                for (BNGRoad road : bngRoads.getRoads()) {
                    roadSpeed = roadTypes.getSpeed(road.getRoadType());
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
                            graph.getNode(nodeID).setAttribute("speed-limit", roadSpeed);
                        }
                        intNodeCount++;

                        // EDGE: Connect current node to the previous node
                        if (prevnode != null) { //  && !prevnode.equals(nodeID)
                            double edgeLength = Utilities.distanceBetweenNodes(graph.getNode(prevnode),
                                    graph.getNode(nodeID) );
                            String edgeID = roadID + "." + edgeCount;
//                            logger.trace("Add edge from {} to {} length {} speed {}",
//                                    nodeID, prevnode, edgeLength, roadSpeed);

                            // Reverse edge
                            graph.addEdge(edgeID + ".R", nodeID, prevnode, true);
                            graph.getEdge(edgeID + ".R").setAttribute("car-count",
                                    0);
                            int capacity = ((int) Math.floor(edgeLength / roadSpeed * 3) < 1?1:(int) Math.floor(edgeLength / roadSpeed * 3));
                            graph.getEdge(edgeID + ".R").setAttribute("car-capacity",
                                    capacity); // because of two second distance between cars
                            graph.getEdge(edgeID + ".R").setAttribute("edge-speed",
                                    roadSpeed);
                            // Forward edge
                            graph.addEdge(edgeID + ".F", prevnode, nodeID, true);
                            graph.getEdge(edgeID + ".F").setAttribute("car-count", 0);
                            graph.getEdge(edgeID + ".F").setAttribute("car-capacity",
                                    capacity); // because of two second distance between cars
                            graph.getEdge(edgeID + ".F").setAttribute("edge-speed",
                                    roadSpeed);
                            edgeCount++;
                        }
                        prevnode = nodeID;
                    }
                }
                logger.info("Graph node count: {}", graph.getNodeCount());
                logger.info("Graph edge count: {}", graph.getEdgeCount());
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
            // FQN used here because Path is redefined by the graphstream package
            java.nio.file.Path path = Paths.get(properties.getProperty("INPUT_DATA"), properties.getProperty("ROADS_DATA"));
            String filename = path.toString();
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
}
