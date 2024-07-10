package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Grid;
import uk.ac.ncl.nclwater.firm2.firm2.model.Building;
import uk.ac.ncl.nclwater.firm2.firm2.model.Buildings;
import uk.ac.ncl.nclwater.firm2.firm2.model.FloodModelParameters;
import uk.ac.ncl.nclwater.firm2.firm2.model.GlobalVariables;

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
    public static Grid loadBuildings(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                               Properties properties) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("buildings-data");
            Buildings buildings = gson.fromJson(new FileReader(filename), Buildings.class);
            logger.debug("Reading: {}", filename);
            Grid buildingGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                    floodModelParameters.isToroidal(), "buildings");
            buildings.getBuildings().forEach(b -> {
                Point coords = Ordinance2GridXY(globalVariables.getLowerLeftX(), globalVariables.getLowerLeftY(), (float) b.getOrdinate().getX(),
                        (float) b.getOrdinate().getY(), globalVariables.getCellSize());
                coords.y = floodModelParameters.getHeight() - 1 - coords.y; // flip horizontally
                int type = b.getType();
                if (coords.x > 0 && coords.x < floodModelParameters.getWidth() &&
                        coords.y > 0 && coords.y < floodModelParameters.getHeight()) {
                    Building building = new Building(getNewId(), type, b.getOrdinate(), b.getNearestRoad_ID());
                    buildingGrid.setCell(coords.x, coords.y, building);
                }
            });
            return buildingGrid;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
