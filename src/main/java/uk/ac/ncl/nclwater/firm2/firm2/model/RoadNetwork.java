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
