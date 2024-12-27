package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.VehicleCode;
import uk.ac.ncl.nclwater.firm2.firm2.model.VehiclesCodes;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class VehicleCodeDescriptors {
    private static final Logger logger = LoggerFactory.getLogger(VehicleCodeDescriptors.class);

    public static HashMap<String, VehicleCode> loadVehicleCodeDescriptors(Properties properties) {

        try {
            HashMap<String, VehicleCode> hsh_vehicles = new HashMap<>();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // FQN used here because Path is redefined by the graphstream package
            java.nio.file.Path path = Paths.get(properties.getProperty("INPUT_DATA"), properties.getProperty("VEHICLES_DATA"));
            String filename = path.toString();
            VehiclesCodes vehiclesCodes = gson.fromJson(new FileReader(filename), VehiclesCodes.class);
            logger.info("Reading vehicle code descriptors: {}", filename);
            vehiclesCodes.getVehicleCodes().forEach(v -> {
                hsh_vehicles.put(v.getCode(), v);
            });
            return hsh_vehicles;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}