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
    static Graph graph = new SingleGraph("Road Network");

    /**
     * Read the roads.json configuration from file and populate the road grid
     */
    public static void loadRoads(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                 Properties properties, SimpleGrid roadGrid, HashMap<String, ArrayList<Point>> roadHashMap) {
//         		;; manually fix up the bridge over the river.
//         		;; XXX this should be done from a config file.
//         		ask roads with [road-oid = "4000000012487984"] [set road-elevation 10]
//
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("roads-data");
            logger.debug("Reading: {}", filename);
            Roads roads = gson.fromJson(new FileReader(filename), Roads.class);
            roads.getRoads().forEach(r -> {
                String node1 = r.getRoadIDs()[1];
                String node2 = r.getRoadIDs()[2];
                if (graph.getNode(node1) == null) {
                    graph.addNode(node1).setAttribute("ui.label", node1);
                }
                if (graph.getNode(node2) == null) {
                    graph.addNode(node2).setAttribute("ui.label", node2);
                }

                String edgeId = r.getRoadIDs()[0];
                try {
                    if (graph.getEdge(edgeId) == null) {
                        graph.addEdge(edgeId, node1, node2, true)
                                .setAttribute("ui.label", r.getRoadLength());
                    }
                } catch (IdAlreadyInUseException e) {
                    logger.debug("Error adding edge, id already in use: " + edgeId + " between " +
                            node1 + " and " + node2);
                } catch (ElementNotFoundException e) {
                    logger.debug("Error adding edge, element not found: " + edgeId + " between " +
                            node1 + " and " + node2);
                } catch (EdgeRejectedException e) {
                    logger.debug("Error adding edge, edge rejected " + edgeId + " between " +
                            node1 + " and " + node2);
                }
                /**
                ArrayList<PointDouble> roadPoints = r.getPolylineCoordinates();
                ArrayList<Point> pixelPoints = new ArrayList<>();
                ArrayList<Point> wholeRoad = new ArrayList<>();
                ArrayList<Point> cleanedWholeRoad = new ArrayList<>();
                for (PointDouble roadPoint : roadPoints) {
                    // Road co-ordinates have to be divided after read from the json file
                    Point coords = BNG2GridXY(globalVariables.getLowerLeftX(), globalVariables.getLowerLeftY(), (float) roadPoint.getX() / 1000,
                            (float) roadPoint.getY() / 1000, globalVariables.getCellSize());
                    // flip horizontally
                    coords.y = (floodModelParameters.getHeight() - 1 - coords.y);
                    pixelPoints.add(coords);
                }
                for (int i = 1; i < pixelPoints.size(); i++) {
                    wholeRoad.addAll(interpolate(pixelPoints.get(i - 1).x, pixelPoints.get(i - 1).y,
                            pixelPoints.get(i).x, pixelPoints.get(i).y));
                }
                wholeRoad.forEach(point -> {
                    if (point.x >= 0 && point.x < floodModelParameters.getWidth() && point.y >= 0 && point.y < floodModelParameters.getHeight()) {
                        Road newRoad = new Road(AgentIDProducer.getNewId(), r.getRoadIDs());
                        newRoad.setRoadLength(r.getRoadLength());
                        newRoad.setRoadType(r.getRoadType());
                        roadGrid.setCell(point.x, point.y, newRoad);
                        cleanedWholeRoad.add(point);
                    } else {
                        logger.trace("Road: {}, {} is out of bounds", point.x, point.y);
                    }
                });
                roadHashMap.put(r.getRoadIDs()[0],cleanedWholeRoad);
                 **/
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadBNGRoads() {

    }


}
