package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.Ordinance2GridXY;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.interpolate;

public class LoadRoadsGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadRoadsGrid.class);

    /**
     * Read the roads.json configuration from file and populate the road grid
     */
    public static void loadRoads(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                 Properties properties, SimpleGrid roadGrid, HashMap<String, ArrayList<Point>> roadHashMap) {
//         		;; manually fix up the bridge over the river.
//         		;; XXX this should be done from a config file.
//         		ask roads with [road-oid = "4000000012487984"] [set road-elevation 10]
//
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("roads-data");
            logger.debug("Reading: {}", filename);
            Roads roads = gson.fromJson(new FileReader(filename), Roads.class);
            roads.getRoads().forEach(r -> {
                ArrayList<PointInteger> roadPoints = r.getPolylineCoordinates();
                ArrayList<Point> wholeRoad = new ArrayList<>();
                ArrayList<Point> cleanedWholeRoad = new ArrayList<>();
                ArrayList<PointInteger> pixelPoints = new ArrayList<>(roadPoints);
                for (int i = 1; i < pixelPoints.size(); i++) {
                    wholeRoad.addAll(interpolate(pixelPoints.get(i - 1).getX(), pixelPoints.get(i - 1).getY(),
                            pixelPoints.get(i).getX(), pixelPoints.get(i).getY()));
                }
                wholeRoad.forEach(point -> {
                    if (point.x >= 0 && point.x < floodModelParameters.getWidth() && point.y >= 0 && point.y < floodModelParameters.getHeight()) {
                        Road newRoad = new Road(AgentIDProducer.getNewId(), r.getRoadIDs());
                        newRoad.setRoadLength(r.getRoadLength());
                        newRoad.setRoadType(r.getRoadType());
                        roadGrid.setCell(point.x, point.y, newRoad);
                        cleanedWholeRoad.add(point);
                    } else {
                        logger.trace("Road: {}, {} is out of bounds", point.x, point.y);
                    }
                });
                roadHashMap.put(r.getRoadIDs()[0],cleanedWholeRoad);
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
