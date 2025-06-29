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


package uk.ac.ncl.nclwater.firm2.examples;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CheckRoads.class);
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
