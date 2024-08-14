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
import uk.ac.ncl.nclwater.firm2.firm2.model.PointDouble;
import uk.ac.ncl.nclwater.firm2.firm2.model.RoadNetwork;
import uk.ac.ncl.nclwater.firm2.firm2.model.Roads;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class RoadNetworkGSTest  implements ViewerListener {

    RoadNetworkTest roadNetworkTest = new RoadNetworkTest();
    protected boolean loop = true;
    Graph graph = new SingleGraph("Road Networks GraphStream Test");
    Node first = null;
    Node second = null;
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();


    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");
        new RoadNetworkGSTest();
    }

    RoadNetworkGSTest() {
        String filename = "DATA/inputs/BNG_roads.json";
        try {
            Roads roads = gson.fromJson(new FileReader(filename), Roads.class);
            roads.getRoads().forEach(bngroad -> {
                int nodeInc = 0;
                int edgeInc = 0;
                PointDouble road = bngroad.getPolylineCoordinates().get(1);
                String prevID = bngroad.getRoadIDs()[1];
                graph.addNode(prevID);
                graph.getNode(bngroad.getRoadIDs()[1]).setAttribute("xyz",
                        bngroad.getPolylineCoordinates().getFirst().getX(),
                        bngroad.getPolylineCoordinates().getFirst().getY(), 0);
                System.out.print(prevID + ", ");
                for (int roadsection = 1; roadsection < bngroad.getPolylineCoordinates().size() - 1; roadsection++) {
                    String nodeID = bngroad.getRoadIDs()[0] + "." + nodeInc++;
                    System.out.print(nodeID + ", ");
                    graph.addNode(nodeID);
                    graph.getNode(nodeID).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(roadsection).getX(),
                            bngroad.getPolylineCoordinates().get(roadsection).getY(), 0);
                    graph.addEdge(bngroad.getRoadIDs()[0] + edgeInc++, graph.getNode(prevID),
                            graph.getNode(nodeID));
                    prevID = nodeID;
                }
                int last = bngroad.getPolylineCoordinates().size() - 1;
                if (graph.getNode(bngroad.getRoadIDs()[2]) != null) {
                    graph.addNode(bngroad.getRoadIDs()[2]);
                    graph.getNode(bngroad.getRoadIDs()[2]).setAttribute("xyz",
                            bngroad.getPolylineCoordinates().get(last).getX(),
                            bngroad.getPolylineCoordinates().get(last).getY(), 0);
                }
                graph.addEdge(bngroad.getRoadIDs()[2] + edgeInc, graph.getNode(prevID),
                        graph.getNode(bngroad.getRoadIDs()[2]));
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
        System.out.println("Button pushed on node " + id);
        System.out.println(Arrays.toString(graph.getNode(id).getArray(id)));
        if (first == null) {
            if (second != null) {
                second.removeAttribute("ui.class");
                second = null;
            }
            System.out.println("Set first");
            first = graph.getNode(id);
            graph.getNode(id).setAttribute("ui.class", "marked");
        } else {
            if (second == null) {
                System.out.println("Set second");
                second = graph.getNode(id);
                graph.getNode(id).setAttribute("ui.class", "marked");
                AStar aStar = new AStar(graph);
                aStar.compute(first.getId(), second.getId());
                System.out.println("aStar.getShortestPath() = " + aStar.getShortestPath());
                aStar.getShortestPath().getEdgePath().forEach(p->{
                    p.setAttribute("ui.class", "marked");
                    System.out.println("make red");
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

