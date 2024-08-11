package uk.ac.ncl.nclwater.firm2.examples;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.awt.*;

public class GraphStreamTest implements ViewerListener {
    protected boolean loop = true;
    Graph graph = new SingleGraph("Clicks");
    Node first = null;
    Node second = null;

    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui", "swing");
        new GraphStreamTest();
    }

    public GraphStreamTest() {
        // We do as usual to display a graph. This
        // connect the graph outputs to the viewer.
        // The viewer is a sink of the graph.
        graph.addNode("a").setAttribute("ui.label", "a");
        graph.getNode("a").setAttribute("ui.style", "fill-color: blue;");
        graph.addNode("b").setAttribute("ui.label", "b");
        graph.addNode("c").setAttribute("ui.label", "c");
        graph.addNode("d").setAttribute("ui.label", "d");
        graph.addNode("e").setAttribute("ui.label", "e");
        graph.addNode("f").setAttribute("ui.label", "f");

        graph.addEdge("1", "a", "b", true)
                .setAttribute("ui.label", 1);
        graph.getEdge("1").setAttribute("weight", 1);

        graph.addEdge("2", "a", "d", true)
                .setAttribute("ui.label", 20);
        graph.getEdge("2").setAttribute("weight", 2);

        graph.addEdge("3", "d", "f", true)
                .setAttribute("ui.label", 1);
        graph.getEdge("3").setAttribute("weight", 1);

        graph.addEdge("4", "b", "c", true)
                .setAttribute("weight", 4);
        graph.addEdge("5", "c", "d", true)
                .setAttribute("weight", 4);
        graph.addEdge("6", "c", "f", true)
                .setAttribute("weight", 4);
        graph.addEdge("7", "e", "f", true)
                .setAttribute("weight", 4);



        Viewer viewer = graph.display();
        viewer.getDefaultView().enableMouseOptions();

        // The default action when closing the view is to quit
        // the program.
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        // We connect back the viewer to the graph,
        // the graph becomes a sink for the viewer.
        // We also install us as a viewer listener to
        // intercept the graphic events.
        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener( this);
        fromViewer.addSink(graph);

        // Then we need a loop to do our work and to wait for events.
        // In this loop we will need to call the
        // pump() method before each use of the graph to copy back events
        // that have already occurred in the viewer thread inside
        // our thread.

        while (loop) {
            fromViewer.pump(); // or fromViewer.blockingPump(); in the nightly builds

            // here your simulation code.

            // You do not necessarily need to use a loop, this is only an example.
            // as long as you call pump() before using the graph. pump() is non
            // blocking.  If you only use the loop to look at event, use blockingPump()
            // to avoid 100% CPU usage. The blockingPump() method is only available from
            // the nightly builds.
        }
    }

    public void viewClosed(String id) {
        loop = false;
    }

    public void buttonPushed(String id) {
        System.out.println("Button pushed on node " + id);
        if (first == null) {
            System.out.println("Set first");
            first = graph.getNode(id);
            graph.getNode(id).setAttribute("ui.style", "fill-color: red;");
        } else {
            if (second == null) {
                System.out.println("Set second");
                second = graph.getNode(id);
                graph.getNode(id).setAttribute("ui.style", "fill-color: green;");
                AStar aStar = new AStar(graph);
                aStar.compute(first.getId(), second.getId());
                System.out.println("aStar.getShortestPath() = " + aStar.getShortestPath());
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

