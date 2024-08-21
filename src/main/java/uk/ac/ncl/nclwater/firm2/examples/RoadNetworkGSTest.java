package uk.ac.ncl.nclwater.firm2.examples;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected boolean loop = true;
    private Graph graph = new SingleGraph("Road Networks GraphStream Test");
    private Node first = null;
    private Node second = null;
    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private static final Logger logger = LoggerFactory.getLogger(RoadNetworkGSTest.class);
    private HashMap<String, Road> roadsMap = new HashMap<>();


    public static void main(String[] args) {
        new RoadNetworkGSTest();
    }

    public RoadNetworkGSTest() {
        logger.debug("Run RoadNetworkGSTest");
        System.setProperty("org.graphstream.ui", "swing");
        String stylesheet = null;
        try {
            stylesheet = new String(Files.readAllBytes(Paths.get(getClass().getResource("/stylesheet.css").toURI())));
            graph.setAttribute("ui.stylesheet", stylesheet);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String filename = "DATA/inputs/BNG_roads.json";
        try {
            Roads roads = gson.fromJson(new FileReader(filename), Roads.class);
            roads.getRoads().forEach(bngroad -> {
                int nodeInc = 0;
                int edgeInc = 0;
                PointDouble road = bngroad.getPolylineCoordinates().get(1);
                String prevID = bngroad.getRoadIDs()[1];
                if (graph.getNode(prevID) == null) {
                    graph.addNode(prevID);
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
                        // This node (nodeID) belongs to this road (bngroad)
                        roadsMap.put(nodeID, bngroad);
                        graph.getNode(nodeID).setAttribute("xyz",
                                bngroad.getPolylineCoordinates().get(roadsection).getX(),
                                bngroad.getPolylineCoordinates().get(roadsection).getY(), 0);
                        graph.addEdge(bngroad.getRoadIDs()[0] + edgeInc++, graph.getNode(prevID),
                                graph.getNode(nodeID));
                        prevID = nodeID;
                    }
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
            logger.debug("File not found.");
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            logger.debug(e.getMessage());
        }


        Viewer viewer = graph.display();
        viewer.getDefaultView().enableMouseOptions();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);


        while (loop) {
                fromViewer.pump(); // or fromViewer.blockingPump(); in the nightly builds
        }
    }


    public void viewClosed(String id) {
        loop = false;
    }

    public void buttonPushed(String id) {
        if (first == null) {
            if (second != null) {
                second.removeAttribute("ui.class");
                second = null;
            }
            first = graph.getNode(id);
            logger.debug("First node {} is part of road {}", first, roadsMap.get(id).getRoadIDs());
            graph.getNode(id).setAttribute("ui.class", "marked");
        } else {
            if (second == null) {
                second = graph.getNode(id);
                logger.debug("Second node {} is part of road {}", second, roadsMap.get(id).getRoadIDs());
                graph.getNode(id).setAttribute("ui.class", "marked");
                AStar aStar = new AStar(graph);
                aStar.compute(first.getId(), second.getId());
                logger.debug("aStar.getShortestPath() = {}", aStar.getShortestPath());
                aStar.getShortestPath().getEdgePath().forEach(p->{
                    p.setAttribute("ui.class", "marked");
                });
                first.removeAttribute("ui.class");
                first = null;
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

