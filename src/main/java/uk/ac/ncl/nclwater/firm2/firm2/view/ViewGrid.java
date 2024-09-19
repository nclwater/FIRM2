package uk.ac.ncl.nclwater.firm2.firm2.view;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;

public class ViewGrid  extends JFrame implements ViewerListener {
    private static final Logger logger = LoggerFactory.getLogger(ViewGrid.class);
    static boolean loop = true;
    ViewerListener viewerListener;
    Graph graph = null;


    public void displayGraph(Graph graph, ViewerListener vl) {
            this.graph = graph;
            if (vl != null) {
                this.viewerListener = vl;
            }

            System.setProperty("org.graphstream.ui", "swing");

            Viewer viewer = graph.display();
            viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

            ViewerPipe fromViewer = viewer.newViewerPipe();
            fromViewer.addViewerListener(viewerListener);
            fromViewer.addSink(graph);
            while (loop) {
                try {
                    fromViewer.pump();
                } catch (Exception e) {
                    logger.error("here: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }



    @Override
    public void viewClosed(String s) {
        loop = false;
    }

    @Override
    public void buttonPushed(String s) {
        logger.debug("Pressed button on {}", s);

    }

    @Override
    public void buttonReleased(String s) {
        logger.debug("Button released");
    }

    @Override
    public void mouseOver(String id) {

    }

    @Override
    public void mouseLeft(String id) {

    }


}
