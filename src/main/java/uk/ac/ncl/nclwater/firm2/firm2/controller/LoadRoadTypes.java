package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ncl.nclwater.firm2.firm2.model.RoadTypes;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

public class LoadRoadTypes {

    private static final Logger logger = LogManager.getLogger(LoadRoadTypes.class);

    public static RoadTypes loadRoadTypes(Properties properties) {
        RoadTypes roadTypes = null;
        try {
            Gson gson_roadTypes = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("ROAD_TYPES");
            roadTypes = gson_roadTypes.fromJson(new FileReader(filename), RoadTypes.class);
            roadTypes.getRoadTypes().forEach(roadType -> {
                logger.trace("Road type: {}, speed: {}", roadType.getRoadType(), roadType.getSpeedLimit());

            });
        } catch (FileNotFoundException e) {
            logger.error("File not found");
        }
        return roadTypes;
    }

}
