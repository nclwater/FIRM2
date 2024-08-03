package uk.ac.ncl.nclwater.firm2.examples;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.util.MouseOverMouseManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.InteractiveElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.Roads;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class VisualiseNetwork {
    private static final Logger logger = LoggerFactory.getLogger(VisualiseNetwork.class);

    static List<String> nodes = new ArrayList<>();
    static Graph graph = new SingleGraph("Road Network");

    public VisualiseNetwork() {
    }

    public static void visualize() {
        // Set the GraphStream UI property
        System.setProperty("org.graphstream.ui", "swing");
        graph.setAttribute("ui.stylesheet", "node { fill-color: red; }");
        //        graph.addAttribute("ui.stylesheet", "node { size: 5px, 5px; }");
        Viewer viewer = graph.display();
        viewer.getDefaultView().enableMouseOptions();
        viewer.getDefaultView().setMouseManager(new MouseOverMouseManager(EnumSet.of(InteractiveElement.EDGE,
                InteractiveElement.NODE, InteractiveElement.SPRITE)));
        View view = viewer.getDefaultView();
        view.getCamera().setViewPercent(0.5);
    }

    public static void main(String[] args) {
        VisualiseNetwork visualiseNetwork = new VisualiseNetwork();

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        String filename = "DATA/inputs/BNG_roads.json";
        try {
            Roads roads = gson.fromJson(new FileReader(filename), Roads.class);

            // Add nodes to the graph
            roads.getRoads().forEach(road -> {
                String node1 = road.getRoadIDs()[1];
                String node2 = road.getRoadIDs()[2];
                if (graph.getNode(node1) == null) {
                    graph.addNode(node1).setAttribute("ui.label", node1);
                }
                if (graph.getNode(node2) == null) {
                    graph.addNode(node2).setAttribute("ui.label", node2);
                }

                String edgeId = road.getRoadIDs()[0];
                try {
                    if (graph.getEdge(edgeId) == null) {
                        graph.addEdge(edgeId, node1, node2, true)
                                .setAttribute("ui.label", road.getRoadLength());
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
            });

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        VisualiseNetwork.visualize();
    }
}