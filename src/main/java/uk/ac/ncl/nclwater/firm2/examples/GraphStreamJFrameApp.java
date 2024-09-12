package uk.ac.ncl.nclwater.firm2.examples;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.controller.LoadRoadsGrid;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;

public class GraphStreamJFrameApp extends JFrame implements ViewerListener {
    private static final Logger logger = LoggerFactory.getLogger(GraphStreamJFrameApp.class);
    private boolean loop = true;
    private ViewerPipe fromViewer;
    private Graph graph;
    private String prevnode = "";
    private AStar astar;

    public GraphStreamJFrameApp() {
        System.setProperty("org.graphstream.ui", "swing");
        setTitle("GraphStream in JFrame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the graph and add nodes and edges
        graph = createInitializedGraph();
        graph.setStrict(false);

        // Load and apply the stylesheet
        loadStylesheet(graph);

        // Initialize A* algorithm
        astar = new AStar(graph);

        // Create the viewer without opening any extra windows
        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        // Get the embedded view panel
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false); // false to not create a new window

        // Add the view panel to the JFrame
        add(viewPanel, BorderLayout.CENTER);

        // Initialize the ViewerPipe to handle events from the viewer
        fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);

        // Start a thread to pump events from the viewer pipe
        new Thread(this::runViewerPipe).start();

        // Add a window listener to handle closing the application properly
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopViewerPipe();
                viewer.close();
            }
        });
    }

    // Method to create and initialize the graph
    private Graph createInitializedGraph() {
        Graph graph = new SingleGraph("Embedded Graph");
        Properties properties = Utilities.loadPropertiesFile();
        HashMap<String, BNGRoad> hsh_roads = new HashMap<>();
        LoadRoadsGrid.gsLoadRoads(graph, hsh_roads, properties);

        return graph;
    }

    // Load the stylesheet from a file and apply it to the graph
    private void loadStylesheet(Graph graph) {
        File file = new File("stylesheet.css");
        try {
            String stylesheet = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            graph.setAttribute("ui.stylesheet", stylesheet);
        } catch (IOException e) {
            logger.error("Error loading stylesheet: {}", e.getMessage());
        }
    }

    // Thread to handle viewer pipe events
    private void runViewerPipe() {
        while (loop) {
            try {
                synchronized (fromViewer) {
                    if (fromViewer != null) {
                        fromViewer.pump();  // Handle viewer events safely
                    }
                }
                Thread.sleep(100);  // Control the frequency of pumping events
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Error in viewer pipe loop: {}", e.getMessage());
            }
        }
    }

    // Method to stop the viewer pipe safely
    private void stopViewerPipe() {
        loop = false;
        synchronized (fromViewer) {
            if (fromViewer != null) {
                fromViewer.removeViewerListener(this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphStreamJFrameApp app = new GraphStreamJFrameApp();
            app.setVisible(true);
        });
    }

    @Override
    public void viewClosed(String viewName) {
        stopViewerPipe();
    }

    @Override
    public void buttonPushed(String id) {
        synchronized (graph) {
            if (prevnode != null && !prevnode.isEmpty()) {
                logger.debug("Previous node: {}", prevnode);
                graph.getNode(prevnode).setAttribute("ui.style", "fill-color: orange;");
                logger.debug("Calculating shortest path between {} and {}", prevnode, id);
                astar.compute(prevnode, id);
                Path shortestPath = astar.getShortestPath();
                if (shortestPath != null) {
                    shortestPath.getEdgePath().forEach(edge -> edge.setAttribute("ui.class", "marked"));
                }
            }
            graph.getNode(id).setAttribute("ui.style", "fill-color: red;");
        }
        prevnode = id;
    }

    @Override
    public void buttonReleased(String id) {
        logger.debug("Mouse released on node: {}", id);
    }

    @Override
    public void mouseOver(String id) {
        // Optional: Implement if mouse over events are needed
    }

    @Override
    public void mouseLeft(String id) {
        // Optional: Implement if mouse left events are needed
    }
}
