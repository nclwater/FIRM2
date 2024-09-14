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
import java.util.HashMap;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.createPropertiesFile;


public class RoadNetworkGSTest  implements ViewerListener {

    private static final Logger logger = LoggerFactory.getLogger(RoadNetworkGSTest.class);
    private static Graph graph = new SingleGraph("Road Networks GraphStream Test");
    private Node first = null;
    private Node second = null;
    private final HashMap<String, BNGRoad> roadsMap = new HashMap<>();
    AStar aStar = new AStar(graph);
    Path shortest = null;
    BNGRoads roads = null;
    private static final Properties properties = createPropertiesFile();

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
        roads = loadRoads();
    }

    public BNGRoads loadRoads() {
        return LoadRoadsGrid.gsLoadRoads(graph, roadsMap, properties);
    }

    private void viewNetwork(Graph graph, Properties properties, ViewerListener vl) {

//        LoadRoadsGrid.viewGraph(graph, this);
        ViewGrid viewgrid = new ViewGrid();
        viewgrid.displayGraph(graph, properties,vl);
        logger.debug("Add a node");

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
        //test.viewNetwork(graph, properties, test);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        RoadNetworkGSTest.graph = graph;
    }
}

