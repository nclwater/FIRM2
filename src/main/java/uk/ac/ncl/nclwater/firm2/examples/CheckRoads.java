package uk.ac.ncl.nclwater.firm2.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Model;
import uk.ac.ncl.nclwater.firm2.firm2.controller.LoadRoadsGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.createPropertiesFile;

public class CheckRoads {
    private static final Logger logger = LogManager.getLogger(CheckRoads.class);
    private static final HashMap<String, BNGRoad> roadsMap = new HashMap<>();
    private static final Properties properties = createPropertiesFile();

    public static void main(String[] args) {
//        verifyRoads();
        Graph graph = LoadRoadsGrid.loadRoads(properties);
        AStar aStar = new AStar(graph);
        aStar.compute("4000000012473101", "4000000012473883");
        logger.debug("Shortest path: {}", aStar.getShortestPath().toString());
//        logger.debug(graph.getNode("4000000012472821").toString());
    }

     public static void verifyRoads() {
        String filename = properties.getProperty("INPUT_DATA") + "/original_textfile_data/roads.txt";
        HashMap<String, String> roadsMap = new HashMap<>();
        Scanner sc = null;
        try {
            logger.debug("Reading file {}", filename);
            sc = new Scanner(new File(filename));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split(" ");
                String id = tokens[0].substring(2, tokens[0].length() - 1);
                if (roadsMap.containsKey(id)) {
                    logger.debug("Road {} already exists in road network", id);
                } else {
                    roadsMap.put(line, line);
                }
            }
            logger.debug("{} Roads found in road network", roadsMap.size());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
