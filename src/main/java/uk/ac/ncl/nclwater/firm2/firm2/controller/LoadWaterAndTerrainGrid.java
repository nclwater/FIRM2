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
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Properties;
import static uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.Utils.getHeightmapGradient;

/**
 * THIS NEEDS REFACTORING. IT IS THE SAME AS LoadRoadsGrid.
 */
public class LoadWaterAndTerrainGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadWaterAndTerrainGrid.class);

    /**
     * Read the file containing the terrain elevations. If a tile is marked as null it is ocean and the terrain agent
     * should be set to the negative default water level. If the tile has an elevation the water level of the water
     * agent should be set to zero.
     *
     * @param globalVariables
     */
    public static void loadWaterAndTerrain(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                           Properties properties, SimpleGrid terrainGrid, SimpleGrid waterGrid) {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        // Read the file to populate the basic grid of cells

        // FQN used here because Path is redefined by the graphstream package
        java.nio.file.Path path = Paths.get(properties.getProperty("INPUT_DATA"), properties.getProperty("TERRAIN_DATA"));
        String filename = (path.toString());
        logger.info("Reading: {} to get water and terrain", filename);
        TerrainLayer terrainLayer = null;
        try {
            terrainLayer = gson.fromJson(new FileReader(filename), TerrainLayer.class);

            for (int grid_y = 0; grid_y < floodModelParameters.getHeight(); grid_y++) {
                TerrainLine terrainLine = terrainLayer.get(grid_y);
                for (int grid_x = 0; grid_x < floodModelParameters.getWidth(); grid_x++) {
                    String id = Integer.toString(AgentIDProducer.getNewId());
                    // if null assume tile is ocean
                    if (terrainLine.getElevation()[grid_x] != -9999) {
                        terrainGrid.setCell(grid_x, grid_y, new Terrain(id, terrainLine.getElevation()[grid_x]));
                        terrainGrid.getCell(grid_x, grid_y).setColour(
                                getHeightmapGradient("terrain", terrainLine.getElevation()[grid_x],
                                        globalVariables.getMinHeight(),
                                        globalVariables.getMaxHeight()));
                        waterGrid.setCell(grid_x, grid_y, new Water(id, 0, false));
                        waterGrid.getCell(grid_x, grid_y).setColour(new Color(0x00, 117, 0x99, 0x00));
                    } else {
                        terrainGrid.setCell(grid_x, grid_y, new Terrain(id, -floodModelParameters.getOceanDepth()));
                        waterGrid.setCell(grid_x, grid_y, new Water(id, floodModelParameters.getOceanDepth(), true));
                        waterGrid.getCell(grid_x, grid_y).setColour(new Color(0x00, 117, 0x99, 0xFF));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
