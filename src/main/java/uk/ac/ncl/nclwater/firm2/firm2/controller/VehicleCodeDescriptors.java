package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.VehicleCode;
import uk.ac.ncl.nclwater.firm2.firm2.model.VehiclesCodes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;

public class VehicleCodeDescriptors {
    private static final Logger logger = LoggerFactory.getLogger(VehicleCodeDescriptors.class);

    public static HashMap<String, VehicleCode> loadVehicleCodeDescriptors(Properties properties) {

        try {
            HashMap<String, VehicleCode> hsh_vehicles = new HashMap<>();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("vehicles-data");
            VehiclesCodes vehiclesCodes = gson.fromJson(new FileReader(filename), VehiclesCodes.class);
            logger.debug("Reading: {}", filename);
            vehiclesCodes.getVehicleCodes().forEach(v -> {
                hsh_vehicles.put(v.getCode(), v);
                System.out.println("vehicle: " + v.getCode());
            });
            return hsh_vehicles;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}