package uk.ac.ncl.nclwater.firm2.examples;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.controller.LoadRoadsGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.PointDouble;
import uk.ac.ncl.nclwater.firm2.firm2.model.Road;
import uk.ac.ncl.nclwater.firm2.firm2.model.Roads;
import uk.ac.ncl.nclwater.firm2.firm2.view.ViewGrid;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class RoadNetworkGSTest  implements ViewerListener {

    private final Graph graph = new SingleGraph("Road Networks GraphStream Test");
    private Node first = null;
    private Node second = null;
    private static final Logger logger = LoggerFactory.getLogger(RoadNetworkGSTest.class);
    private final HashMap<String, Road> roadsMap = new HashMap<>();
    AStar aStar = new AStar(graph);
    Path shortest = null;
    Roads roads = null;

    public static void main(String[] args) {
        new RoadNetworkGSTest();
    }

    public RoadNetworkGSTest() {
        logger.debug("Run RoadNetworkGSTest");
        System.setProperty("org.graphstream.ui", "swing");
        String stylesheetName = "/stylesheet.css";
        String stylesheet = null;
        try {
            stylesheet = new String(Files.readAllBytes(Paths.get(getClass().getResource(stylesheetName).toURI())));
            graph.setAttribute("ui.stylesheet", stylesheet);
            logger.debug("Set stylesheet to {}", stylesheetName);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String filename = "DATA/inputs/sample_roads.json";
        roads = LoadRoadsGrid.gsLoadRoads(filename, graph, roadsMap);
        LoadRoadsGrid.viewGraph(graph, this);

    }


    public void viewClosed(String id) {
        logger.debug("Exiting");
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
                logger.debug("First node {} is part of road {}", first,roadsMap.get(id)==null?"":roadsMap.get(id).getRoadIDs()[0]);
                graph.getNode(id).setAttribute("ui.class", "marked");
            }
        } else {
            if (second == null) {
                second = graph.getNode(id);
                if (second != null) {
                    logger.debug("Second node {} is part of road {}", second, roadsMap.get(id)==null?"":roadsMap.get(id).getRoadIDs()[0]);
                    graph.getNode(id).setAttribute("ui.class", "marked");
                    aStar.compute(first.getId(), second.getId());
                    shortest = aStar.getShortestPath();
                    if (shortest != null) {
                        logger.debug("aStar.getShortestPath() = {}", shortest.toString());
                        shortest.getEdgePath().forEach(p -> {
                            p.setAttribute("ui.class", "marked");
                        });                    } else {
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

