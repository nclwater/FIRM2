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


package uk.ac.ncl.nclwater.firm2.firm2.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RoadNetwork {

    private List<String> nodes = new ArrayList<>();
    private HashSet<Connection> connections = new HashSet<>();
    private HashMap<String, Point> nodePositions = new HashMap<>();

    public RoadNetwork() {
    }

    public void addNode(String node) {
        nodes.add(node);
        nodePositions.put(node, new Point());
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public HashSet<Connection> getConnections() {
        return connections;
    }

    public HashMap<String, Point> getNodePositions() {
        return nodePositions;
    }


}
