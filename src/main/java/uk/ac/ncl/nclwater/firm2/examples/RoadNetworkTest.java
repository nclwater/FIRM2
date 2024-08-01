package uk.ac.ncl.nclwater.firm2.examples;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoads;
import uk.ac.ncl.nclwater.firm2.firm2.model.Connection;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RoadNetworkTest {

    private List<String> nodes = new ArrayList<>();
    private HashSet<Connection> connections = new HashSet<>();
    private HashMap<String, Point> nodePositions = new HashMap<>();

    public RoadNetworkTest() {
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

    public static void main(String[] args) {
        RoadNetworkTest roadNetworkTest = new RoadNetworkTest();

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        String filename = "DATA/inputs/BNG_roads.json";
        try {
            BNGRoads roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            roads.getRoads().forEach(bngroad -> {
                roadNetworkTest.addNode(bngroad.getRoadIDs()[1]);
                roadNetworkTest.addNode(bngroad.getRoadIDs()[2]);
                Connection connection = new Connection(bngroad.getRoadIDs()[0],
                        bngroad.getRoadIDs()[1],
                        bngroad.getRoadIDs()[2],
                        bngroad.getRoadLength());
                roadNetworkTest.addConnection(connection);
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Road Network Visualization");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new RoadNetworkPanel(roadNetworkTest));
            frame.setSize(1280, 1024);
            frame.setVisible(true);
        });
    }
}

class RoadNetworkPanel extends JPanel {
    private final RoadNetworkTest RoadNetworkTest;
    private final int NODE_SIZE = 3;
    private final Random random = new Random();

    public RoadNetworkPanel(RoadNetworkTest RoadNetworkTest) {
        this.RoadNetworkTest = RoadNetworkTest;
        generateRandomPositions();
    }

    private void generateRandomPositions() {
        int panelWidth = 1280;
        int panelHeight = 1024;
        int margin = 5;

        for (String node : RoadNetworkTest.getNodes()) {
            int x = random.nextInt(panelWidth) + margin;
            int y = random.nextInt(panelHeight) + margin;
            RoadNetworkTest.getNodePositions().put(node, new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw connections
        g2d.setColor(Color.BLACK);
        for (Connection connection : RoadNetworkTest.getConnections()) {
            Point p1 = RoadNetworkTest.getNodePositions().get(connection.getID1());
            Point p2 = RoadNetworkTest.getNodePositions().get(connection.getID2());
            if (p1 != null && p2 != null) {
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Draw nodes
        g2d.setColor(Color.RED);
        for (String node : RoadNetworkTest.getNodes()) {
            Point p = RoadNetworkTest.getNodePositions().get(node);
            if (p != null) {
                g2d.fillOval(p.x - NODE_SIZE / 2, p.y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
                //g2d.drawString(node, p.x + NODE_SIZE / 2, p.y - NODE_SIZE / 2);
            }
        }
    }
}
