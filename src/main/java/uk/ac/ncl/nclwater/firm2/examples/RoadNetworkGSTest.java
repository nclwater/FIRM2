package uk.ac.ncl.nclwater.firm2.examples;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.controller.LoadRoadsGrid;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoad;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoads;
import uk.ac.ncl.nclwater.firm2.firm2.model.Road;
import uk.ac.ncl.nclwater.firm2.firm2.model.Roads;
import uk.ac.ncl.nclwater.firm2.firm2.view.ViewGrid;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.createPropertiesFile;


public class RoadNetworkGSTest  implements ViewerListener {

    private final Graph graph = new SingleGraph("Road Networks GraphStream Test");
    private Viewer viewer = null;
    private Node first = null;
    private Node second = null;
    private static final Logger logger = LoggerFactory.getLogger(RoadNetworkGSTest.class);
    private final HashMap<String, BNGRoad> roadsMap = new HashMap<>();
    AStar aStar = new AStar(graph);
    Path shortest = null;
    BNGRoads roads = null;

    public RoadNetworkGSTest() {
        logger.debug("Run RoadNetworkGSTest");
        System.setProperty("org.graphstream.ui", "swing");
        String stylesheet = null;
        try {
            File file = new File("stylesheet.css");
            stylesheet = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            logger.trace("Stylesheet {}", file.getAbsolutePath());
            graph.setAttribute("ui.stylesheet", stylesheet);
        } catch (IOException e) {
            logger.error("Stylesheet not found");
            //throw new RuntimeException(e);
        }
        Properties properties = createPropertiesFile();
        roads = LoadRoadsGrid.gsLoadRoads(graph, roadsMap, properties);
//        LoadRoadsGrid.viewGraph(graph, this);
        ViewGrid viewgrid = new ViewGrid();
        viewgrid.displayGraph(graph, properties, this);

    }


    public void viewClosed(String id) {
        logger.trace("Exiting");
//        loop = false;
    }

    public void buttonPushed(String id) {
        if (first == null) {
            if (shortest != null) {

                logger.debug("aStar.getShortestPath() = {}", shortest.toString());
                shortest.getEdgePath().forEach(p -> {
                    p.removeAttribute("ui.class");
                });                    } else {
                logger.debug("Remove previous paths");
            }
            if (second != null) {
                second.removeAttribute("ui.class");
                second = null;
            }
            first = graph.getNode(id);
            if (first != null) {
                logger.debug("First node {} ({}) is part of road {}", first,first.getAttribute("xyz"),
                        roadsMap.get(id)==null?"":roadsMap.get(id).getRoadIDs()[0]);
                graph.getNode(id).setAttribute("ui.class", "marked");
            }
        } else {
            if (second == null) {
                second = graph.getNode(id);
                if (second != null) {
                    logger.debug("Second node {} ({}) is part of road {}", second, second.getAttribute("xyz"),
                            roadsMap.get(id)==null?"":roadsMap.get(id).getRoadIDs()[0]);
                    graph.getNode(id).setAttribute("ui.class", "marked");
                    aStar.compute(first.getId(), second.getId());
                    shortest = aStar.getShortestPath();
                    if (shortest != null) {
                        logger.debug("aStar.getShortestPath() = {}", shortest.toString());
                        shortest.getEdgePath().forEach(p -> {
                            p.setAttribute("ui.class", "marked");
                        });
                        Object[] xyzValues1 = (Object[]) first.getAttribute("xyz");
                        double northing1 = (double) xyzValues1[0];
                        double easting1 = (double) xyzValues1[1];
                        Object[] xyzValues2 = (Object[]) first.getAttribute("xyz");
                        double northing2 = (double) xyzValues2[0];
                        double easting2 = (double) xyzValues2[1];

                        logger.debug("Distance between road {} and {} is {}", first.getAttribute("xyz"),
                                second.getAttribute("xyz"),
                                Utilities.calculateDistance(easting1, northing1, easting2, northing2 ));

                    } else {
                        logger.debug("No path found between nodes");
                    }

                    first.removeAttribute("ui.class");
                    first = null;
                }
            }
        }
    }

    public void buttonReleased(String id) {
    }

    public void mouseOver(String id) {
    }

    public void mouseLeft(String id) {
    }
}

