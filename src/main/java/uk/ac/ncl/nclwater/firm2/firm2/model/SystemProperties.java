package uk.ac.ncl.nclwater.firm2.firm2.model;

import java.util.HashMap;


/**
 * This is a singleton
 */
public class SystemProperties {

    static SystemProperties sysProperties = null;
    private static final HashMap<String, String> hsh_properties = new HashMap<String, String>();
    
    private SystemProperties() {
        hsh_properties.put("TOROIDAL","false");
        hsh_properties.put("TICKS","0"); // number of ticks to run the model for. 0 means infinitely
        hsh_properties.put("VISUALISE","TRUE");
        hsh_properties.put("CELL_SIZE","5"); // pixels width and height
        hsh_properties.put("CHANCE","50"); // used for random numbers
        hsh_properties.put("APPLICATION_TITLE","FIRM2");
        hsh_properties.put("INPUT_DATA", "/data/inputs/");
        hsh_properties.put("OUTPUT_DATA", "/data/outputs/");
        hsh_properties.put("TERRAIN_DATA", "terrain.json");
        hsh_properties.put("ROADS_DATA", "roads.json");
        hsh_properties.put("BUILDINGS_DATA", "buildings.json");
        hsh_properties.put("DEFENCES_DATA", "defences.json");
        hsh_properties.put("MODEL_PARAMETERS", "globals.json");
        hsh_properties.put("VEHICLES_DATA", "vehicles.json");
        hsh_properties.put("SLOWDOWN", "0"); // milliseconds
        hsh_properties.put("TIME_STAMP", "1719874800"); // unix timestamp
        hsh_properties.put("TICK_TIME_VALUE", "1"); // seconds
        hsh_properties.put("TIMELINE", "timeline.json");
        hsh_properties.put("OCEAN_DEPTH", "4"); // meters
        hsh_properties.put("RUN_ON_STARTUP", "TRUE");
        hsh_properties.put("VEHICLE_FLOOD_DEPTH", "0.025"); // centimeter
        hsh_properties.put("ROAD_TYPES", "road_types.json");
    }

    public static SystemProperties getInstance() {
        if (sysProperties == null) {
            sysProperties = new SystemProperties();
        }
        return sysProperties;
    }

    public HashMap<String, String> getProperties() {
        return hsh_properties;
    }
}
