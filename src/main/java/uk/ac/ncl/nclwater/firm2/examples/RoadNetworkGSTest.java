package uk.ac.ncl.nclwater.firm2.examples;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.ViewerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.controller.LoadRoadsGrid;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoad;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoads;
import uk.ac.ncl.nclwater.firm2.firm2.view.ViewGrid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.createPropertiesFile;

public class RoadNetworkGSTest  implements ViewerListener {

    private static final Logger logger = LoggerFactory.getLogger(RoadNetworkGSTest.class);
    private static Graph graph = new SingleGraph("Road Networks GraphStream Test");
    private Node first = null;
    private Node second = null;
    private final HashMap<String, BNGRoad> roadsMap = new HashMap<>();
    AStar aStar = new AStar(graph);
    Path shortest = null;
    private static final Properties properties = createPropertiesFile();
    File file;
    static String filename = "url('\\c:\\Users\\janne\\IdeaProjects\\FIRM2_\\stylesheet.css')";

    public RoadNetworkGSTest() {
        logger.debug("Run RoadNetworkGSTest");
        System.setProperty("org.graphstream.ui", "swing");
        String stylesheet = null;
        logger.trace("Stylesheet {}", filename);
        graph.setAttribute("ui.stylesheet", filename);
        graph = LoadRoadsGrid.loadRoads(properties);
    }

    private void viewNetwork(Graph graph, Properties properties, ViewerListener vl) {
        ViewGrid viewgrid = new ViewGrid();
        String css = readCSSFile("stylesheet.css");
        graph.setAttribute("ui.stylesheet", css);
        viewgrid.displayGraph(graph,vl);
        logger.debug("Add a node");
    }


    private String readCSSFile(String filename) {
        Scanner scanner = null;
        String content = null;
        try {
            scanner = new Scanner(Paths.get(filename), StandardCharsets.UTF_8.name());
            content = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
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
                logger.debug("First node {} ({}) is part of road {}, with speed limit {}", first,first.getAttribute("xyz"),
                        first.getAttribute("road-id"), first.getAttribute("road-type"));
                first.setAttribute("ui.class", "marked");
            }
        } else {
            if (second == null) {
                second = graph.getNode(id);
                if (second != null) {
                    logger.debug("Second node {} ({}) is part of road {}, with speed limit {}", second, second.getAttribute("xyz"),
                           second.getAttribute("road-id"), second.getAttribute("road-type"));
                    graph.getNode(id).setAttribute("ui.class", "marked");
                    aStar.compute(first.getId(), second.getId());
                    shortest = aStar.getShortestPath();
                    if (shortest != null) {
                        logger.debug("aStar.getShortestPath() = {}", shortest.toString());
                        shortest.getEdgePath().forEach(p -> {
                            p.setAttribute("ui.class", "marked");
                        });


                        logger.debug("Distance between road {} and {} is {}", first.getAttribute("xyz"),
                                second.getAttribute("xyz"),
                                Utilities.distanceBetweenNodes(first, second));

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

    public static void main(String[] args) {
        RoadNetworkGSTest test = new RoadNetworkGSTest();
        AStar aStar = new AStar(graph);
        aStar.compute("4000000012472821", "4000000012843295");
        Path path = aStar.getShortestPath();
        logger.debug(path.toString());
        test.viewNetwork(graph, properties, test);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        RoadNetworkGSTest.graph = graph;
    }
}

