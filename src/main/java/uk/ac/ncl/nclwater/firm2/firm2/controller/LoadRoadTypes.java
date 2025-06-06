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
import uk.ac.ncl.nclwater.firm2.firm2.model.RoadTypes;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Properties;

public class LoadRoadTypes {

    private static final Logger logger = LoggerFactory.getLogger(LoadRoadTypes.class);

    public static RoadTypes loadRoadTypes(Properties properties) {
        RoadTypes roadTypes = null;
        try {
            Gson gson_roadTypes = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // FQN used here because Path is redefined by the graphstream package
            java.nio.file.Path path = Paths.get(properties.getProperty("INPUT_DATA"), properties.getProperty("ROAD_TYPES"));
            String filename = path.toString();
            roadTypes = gson_roadTypes.fromJson(new FileReader(filename), RoadTypes.class);
            roadTypes.getRoadTypes().forEach(roadType -> {
                logger.info("Road type: {}, speed: {}", roadType.getRoadType(), roadType.getSpeedLimit());

            });
        } catch (FileNotFoundException e) {
            logger.error("File not found");
        }
        return roadTypes;
    }

}
