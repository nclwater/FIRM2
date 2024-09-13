package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer.getNewId;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.Ordinance2GridXY;

public class LoadBuildingsGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadBuildingsGrid.class);

    /**
     * Read the building.json file and populate the buildings grid
     */
    public static SimpleGrid loadBuildings(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                           Properties properties) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("BUILDINGS_DATA");
            Buildings buildings = gson.fromJson(new FileReader(filename), Buildings.class);
            logger.debug("Reading: {}", filename);
            SimpleGrid buildingGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                    floodModelParameters.isToroidal(), "buildings");
            buildings.getBuildings().forEach(b -> {
                PointInteger coords = Ordinance2GridXY(globalVariables.getLowerLeftX(), globalVariables.getLowerLeftY(), (float) b.getOrdinate().getX(),
                        (float) b.getOrdinate().getY(), globalVariables.getCellSize());
                coords.setY(floodModelParameters.getHeight() - 1 - coords.getY()); // flip horizontally
                int type = b.getType();
                if (coords.getX() > 0 && coords.getX() < floodModelParameters.getWidth() &&
                        coords.getY() > 0 && coords.getY() < floodModelParameters.getHeight()) {
                    Building building = new Building(Integer.toString(getNewId()), type, b.getOrdinate(), b.getNearestRoad_ID());
                    buildingGrid.setCell(coords.getX(), coords.getY(), building);
                }
            });
            return buildingGrid;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
