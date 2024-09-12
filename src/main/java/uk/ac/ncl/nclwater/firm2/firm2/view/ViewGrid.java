package uk.ac.ncl.nclwater.firm2.firm2.view;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewGrid  extends JFrame implements ViewerListener {
    private static final Logger logger = LoggerFactory.getLogger(ViewGrid.class);
    static boolean loop = true;
    ViewerListener viewerListener;
    Graph graph = null;


    public void displayGraph(Graph graph, Object s, ViewerListener vl) {
            this.graph = graph;
            if (vl != null) {
                this.viewerListener = vl;
            }
            String stylesheet = null;
            Path pth_styleSheet = null;
            try {
                pth_styleSheet = Paths.get(s.getClass().getResource("/stylesheet.css").toURI());
            } catch (NullPointerException e) {
                logger.debug("CSS file not found.");
            } catch (URISyntaxException e) {
                logger.debug("URI syntax error.");
            }
            if (pth_styleSheet != null) {
                try {
                    stylesheet = new String(Files.readAllBytes(pth_styleSheet));
                    graph.setAttribute("ui.stylesheet", stylesheet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
