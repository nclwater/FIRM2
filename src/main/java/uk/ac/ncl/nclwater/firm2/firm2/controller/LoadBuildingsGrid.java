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


package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
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
            // FQN used here because Path is redefined by the graphstream package
            java.nio.file.Path path = Paths.get(properties.getProperty("INPUT_DATA"), properties.getProperty("BUILDINGS_DATA"));
            String filename = path.toString();
            Buildings buildings = gson.fromJson(new FileReader(filename), Buildings.class);
            logger.info("Reading: {}", filename);
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
