package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.examples.RoadNetworkGSTest;
import uk.ac.ncl.nclwater.firm2.firm2.model.RoadType;
import uk.ac.ncl.nclwater.firm2.firm2.model.RoadTypes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.createPropertiesFile;

class RoadTypeSingleton {
    private static final Logger logger = LoggerFactory.getLogger(RoadTypeSingleton.class);
    private static RoadTypeSingleton roadTypeSingleton = null;
    private static RoadTypes roadTypes = null;

    private RoadTypeSingleton() {}

    public static RoadTypeSingleton getInstance(Properties properties) {
        if (roadTypeSingleton == null) {
            RoadTypeSingleton roadTypeSingleton = new RoadTypeSingleton();
            roadTypes = loadRoadTypes(properties);
        }
        return roadTypeSingleton;
    }

    private static RoadTypes loadRoadTypes(Properties properties) {
        RoadTypes roadTypes = null;
        try {
            Gson gson_roadTypes = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("ROAD_TYPES");
            roadTypes = gson_roadTypes.fromJson(new FileReader(filename), RoadTypes.class);
            logger.debug("Road types: {}", filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        return roadTypes;

    }

    public static RoadTypes getRoadTypes() {

        return roadTypes;
    }
}
