/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


package uk.ac.ncl.nclwater.firm2.firm2.view;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.RoadTypes;

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
