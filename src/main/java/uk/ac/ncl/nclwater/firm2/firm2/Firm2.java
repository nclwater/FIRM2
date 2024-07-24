package uk.ac.ncl.nclwater.firm2.firm2;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.*;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer;
import uk.ac.ncl.nclwater.firm2.firm2.controller.*;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

public class Firm2 extends Model {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static FloodModelParameters floodModelParameters;

    private float x_origin;
    private float y_origin;
    private int cellMeters;
    Properties properties;
    Long modelTimeStamp = 0L;
    ModelState modelState = new ModelState();
    int modelStateIndex = 0;
    ModelStateChanges modelStateChanges;
    Float maintainSeaLevel;
    HashMap<String, VehicleCode> vehicleCodes = new HashMap<>();
    HashMap<String, ArrayList<Point>> roadHashMap = new HashMap<>();

    /**
     * Initialise the model
     */
    @Override
    public void modelInit() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // Read global variable (eventually to be read from environment vars for DAFNI)
            GlobalVariables globalVariables = gson.fromJson(new FileReader(
                            properties.getProperty("input-data") + properties.getProperty("model-parameters")),
                    GlobalVariables.class);
            x_origin = globalVariables.getLowerLeftX();
            y_origin = globalVariables.getLowerLeftY();
            cellMeters = globalVariables.getCellSize();
            floodModelParameters.setWidth(globalVariables.getColumns());
            floodModelParameters.setHeight(globalVariables.getRows());
            vehicleCodes = VehicleCodeDescriptors.loadVehicleCodeDescriptors(properties);


            // Create and populate all grids
            SimpleGrid waterGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "water");
            SimpleGrid terrainGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "terrain");
            LoadWaterAndTerrainGrid.loadWaterAndTerrain(globalVariables, floodModelParameters, properties, terrainGrid, waterGrid);
            SimpleGrid roadsGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "roads");
            LoadRoadsGrid.loadRoads(globalVariables, floodModelParameters, properties, roadsGrid, roadHashMap);
            grids.put("terrain", terrainGrid);
            grids.put("buildings", LoadBuildingsGrid.loadBuildings(globalVariables, floodModelParameters, properties));
            grids.put("roads", roadsGrid);
            grids.put("defences", LoadDefencesGrid.loadDefences(globalVariables, floodModelParameters, properties));
            grids.put("water", waterGrid);
            ComplexGrid vehicles = new ComplexGrid("vehicles");
            grids.put("vehicles", vehicles);

            modelStateChanges = ModelStateChanges.readTimeLine(properties);
            modelState = modelStateChanges.getModelStates().get(modelStateIndex);
            // Visualise if visualisation is set to true
            if (floodModelParameters.isVisualise()) {
                visualisation = new Visualisation(this);
            }
            // Do an initial tick
            Timestamp mts = new Timestamp(floodModelParameters.getTimestamp() * 1000);
            tick();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Logic to happen on every tick
     */
    @Override
    public void tick() {
        // FYI https://www.unixtimestamp.com/
        // increment time: time = model start time + tick time value
        if (maintainSeaLevel == null) maintainSeaLevel = floodModelParameters.getOceanDepth();
        modelTimeStamp += floodModelParameters.getTickTimeValue() * 1000;
        long timestamp = 0;
        if (modelState != null) {
            int hours = Integer.parseInt(modelState.getTime().split(":")[0]);
            int minutes = Integer.parseInt(modelState.getTime().split(":")[1]);
            timestamp = ((floodModelParameters.getTimestamp() * 1000) + (hours * 3600000L) + (minutes * 60000L));

        }
        Timestamp ts = new Timestamp(timestamp);
        Timestamp mts = new Timestamp(modelTimeStamp);
        SimpleGrid waterGrid = (SimpleGrid) grids.get("water");
        SimpleGrid terrainGrid = (SimpleGrid) grids.get("terrain");
        SimpleGrid defenceGrid = (SimpleGrid) grids.get("defences");
        SimpleGrid roadGrid = (SimpleGrid) grids.get("roads");
        SimpleGrid newWaterGrid = new SimpleGrid(waterGrid.getWidth(), waterGrid.getHeight(), waterGrid.isIs_toroidal(), waterGrid.getGridName());
        // Initialise new grid to be the same as the old grid.
        for (int row = 0; row < waterGrid.getHeight(); row++) {
            for (int col = 0; col < waterGrid.getWidth(); col++) {
                newWaterGrid.setCell(col, row, new Water(waterGrid.getCell(col, row).getAgent_id(),
                        ((Water) waterGrid.getCell(col, row)).getWaterLevel(), ((Water) waterGrid.getCell(col, row)).isOcean()));
            }
        }

        // reset sea level for ocean cells on every tick
        setSeaLevel(waterGrid, maintainSeaLevel);
        //
        // If there is a timestamp in the timeline for current time, execute the state change
        //
        if (timestamp == modelTimeStamp) {
            logger.debug("STATE CHANGE {}:", mts);
            Float stateSeaLevel = modelState.getSeaLevel();
            modelStateIndex = (modelStateIndex + 1 < modelStateChanges.getModelStates().size())?modelStateIndex + 1:modelStateIndex;
            // get sealevel change
            if (stateSeaLevel != null) {
                logger.debug("sea level change: {}", stateSeaLevel);
                setSeaLevel(waterGrid, stateSeaLevel);
                maintainSeaLevel = stateSeaLevel;
            }
            // get defence breaches
            if (modelState.getDefenceBreach() != null) {
                ArrayList<String> defences = (ArrayList<String>) modelState.getDefenceBreach();
                logger.debug("defence breach: {}", defences);

                for (int row = 0; row < floodModelParameters.getHeight(); row++) {
                    for (int col = 0; col < floodModelParameters.getWidth(); col++) {

                        if (defences != null) {
                            Defence defenceCell = (Defence) defenceGrid.getCell(col, row);
                            if (defenceCell != null) {
                                for (int i = 0; i < defences.size(); i++) {
                                    if (defenceCell.getName().equals(defences.get(i))) {
                                        defenceGrid.setCell(col, row, null);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }
            }
            // Get entering vehicles
            if (modelState.getVehicles() != null) {
                if (!modelState.getVehicles().isEmpty()) {
                    // Place vehicles
                    ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) modelState.getVehicles();
                    logger.debug("{} vehicle types enter: {}", modelState.getVehicles().size(), vehicles);
                    for (int i = 0; i < vehicles.size(); i++) {
                        Vehicle vehicle = vehicles.get(i);

                        String nearestRoad = vehicleCodes.get(vehicle.getCode()).getNearestRoad();
                        logger.debug("vehicles: type: {} dist: {} sd: {} qty: {} road: {}", vehicle.getCode(), vehicle.getDist(),
                                vehicle.getSd(), vehicle.getQty(), nearestRoad);
                        Point roadOrigin = roadHashMap.get(nearestRoad).get(0);
                        for (int c = 0; c < vehicle.getQty(); c++){
                            int id = AgentIDProducer.getNewId();
                            Car car = new Car(id, roadHashMap.get(nearestRoad));
                            ((ComplexGrid) grids.get("vehicles")).add(car);
                        }

                    }
                }
            }
        }
        moveWater(waterGrid, terrainGrid, defenceGrid, newWaterGrid);
        moveVehicles();
        // read the next state change
        modelState = modelStateChanges.getModelStates().get(modelStateIndex);
        if (floodModelParameters.isVisualise()) {
            visualisation.getDrawPanel().repaint();
        }
    }

    private void moveVehicles() {
        ((ComplexGrid) grids.get("vehicles")).getAgents().forEach(ComplexAgent::incrementMovementIndex);
    }

    /**
     * Restore ocean to level
     * @param waterGrid the water grid
     * @param level the level to which the ocean cells have to be restored
     */
    private void setSeaLevel(SimpleGrid waterGrid, float level) {
        for (int row = 0; row < floodModelParameters.getHeight(); row++) {
            for (int col = 0; col < floodModelParameters.getWidth(); col++) {
                Water w = (Water) waterGrid.getCell(col, row);
                if (w.isOcean()) w.setWaterLevel(level);
            }
        }
    }

    private void moveWater(SimpleGrid water, SimpleGrid terrain, SimpleGrid defence, SimpleGrid newWaterGrid) {
        // MOVE WATER
        for (int row = 0; row < floodModelParameters.getHeight(); row++) {
            for (int col = 0; col < floodModelParameters.getWidth(); col++) {
                // Get current cell
                Water currentWaterCell = (Water) water.getCell(col, row);
                // check if the water level has risen above sea level
                // Get water height of current cell
                float waterHeight = ((Water) water.getCell(col, row)).getWaterLevel();
                // Terrain height + plus water of the current cell
                float currentCellWaterHeight = waterHeight + ((Terrain) terrain.getCell(col, row)).getElevation();
                if (waterHeight > 0) {
                    // this will become the neighbour with the lowest elevation+water
                    Water targetNeighbour = null;
                    // This will become the new water level of the current cell
                    float targetHeight = currentCellWaterHeight;
                    // this will become the position of the neighbour with the lowest elevation+water
                    Point targetPos = null;
                    // find the von Neumann neighbours
                    Point[] neighbours = water.getVNNeighborhood(col, row);
                    // find the von Neumann neighbour with the lowest elevation+water
                    for (Point pos : neighbours) {
                        // retrieve the neighbour cell
                        Water neighbour = (Water) water.getCell(pos.x, pos.y);
                        // calculate the total height of the terrain plus the water level plus the height of the defence
                        float neighbourHeight = neighbour.getWaterLevel()
                                + ((Terrain) terrain.getCell(pos.x, pos.y)).getElevation()
                                + ((defence.getCell(col, row) == null) ? 0 : ((Defence) defence.getCell(col, row)).getHeight());
                        // if the height of the neighbour is less than that off the current cell
                        if (neighbourHeight < targetHeight) {
                            targetNeighbour = neighbour;
                            targetHeight = neighbourHeight;
                            targetPos = pos;
                        }
                    }
                    // if a neighbour was found with the lowest level lower than the current cell
                    if (targetNeighbour != null) {

                        Water newCell = (Water)newWaterGrid.getCell(col, row);
                        Water newTarget = (Water)newWaterGrid.getCell(targetPos.x, targetPos.y);
                        float difference = currentCellWaterHeight - targetHeight;
                        float portion = difference < 0.0000001f ? difference : difference / 2.0f;
                        float newHeight = newCell.getWaterLevel() - portion;
                        float newTargetHeight = newTarget.getWaterLevel() + portion;

                        if (!newCell.isOcean())
                            newCell.setWaterLevel(newHeight);
                        newTarget.setWaterLevel(newTargetHeight);
                    } else {
                        // if none of the neighbours were lower and the cell does not yet exist in the new grid
                        // create it
                        if (newWaterGrid.getCell(col, row) == null) {
                            newWaterGrid.setCell(col, row, new Water(currentWaterCell.getAgent_id(),
                                    currentWaterCell.getWaterLevel(), currentWaterCell.isOcean()));
                        }
                    }
                } else {
                    // if there is no water on the cell so just create it in the new grid
                    if (newWaterGrid.getCell(col, row) == null) {
                        newWaterGrid.setCell(col, row, new Water(currentWaterCell.getAgent_id(),
                                currentWaterCell.getWaterLevel(), currentWaterCell.isOcean()));
                    }
                }
                Water newWaterCell = (Water) newWaterGrid.getCell(col, row);
                // Set color based on water is in ocean or not
                if (newWaterCell.getWaterLevel() > 0) {
                    if (newWaterCell.isOcean()) {
                        newWaterCell.setColour(new Color(0x00, 0x00, 0xff));
                    } else {
                        newWaterCell.setColour(new Color(0x11, 0x55, 0xff));
                    }
                }
            }
        }
        grids.put("water", newWaterGrid);

        // Generate a one pixel per cell PNG image on tick
        if (floodModelParameters.isPngOnTick()) {
            ((SimpleGrid) grids.get("water")).createPNG(properties.getProperty("output-data"), "water_" + Long.toString(modelTimeStamp));
            ((SimpleGrid) grids.get("terrain")).createPNG(properties.getProperty("output-data"), "terrain_" + Long.toString(modelTimeStamp));
            ((SimpleGrid) grids.get("defences")).createPNG(properties.getProperty("output-data"), "defences_" + Long.toString(modelTimeStamp));
        }
    }



    /**
     * Default constructor
     */
    public Firm2() {
        super(new FloodModelParameters());
        floodModelParameters = (FloodModelParameters) modelParameters;
        // load properties file or create one if it doesn't exist and add default values
        createPropertiesFile();
        properties = loadPropertiesFile();
        floodModelParameters.setToroidal(Boolean.parseBoolean(properties.getProperty("toroidal")));
        floodModelParameters.setTicks(Integer.parseInt(properties.getProperty("ticks")));
        floodModelParameters.setVisualise(Boolean.parseBoolean(properties.getProperty("visualise")));
        floodModelParameters.setCell_size(Integer.parseInt(properties.getProperty("cell-size")));
        floodModelParameters.setChance(Integer.parseInt(properties.getProperty("chance")));
        floodModelParameters.setTitle(String.valueOf(properties.get("application-title")));
        floodModelParameters.setOceanDepth(Float.parseFloat(properties.getProperty("ocean-depth")));
        floodModelParameters.setTimestamp(Long.parseLong(properties.getProperty("time-stamp")));
        floodModelParameters.setTickTimeValue(Long.parseLong(properties.getProperty("tick-time-value")));
        modelTimeStamp = floodModelParameters.getTimestamp() * 1000; // start time for model
        floodModelParameters.setSlowdown(Integer.parseInt(properties.getProperty("slowdown")));
        floodModelParameters.setTitle(properties.getProperty("application-title"));
        floodModelParameters.setPngOnTick(Boolean.parseBoolean(properties.getProperty("PNG-on-tick")));
        floodModelParameters.setPngOnTick(Boolean.parseBoolean(properties.getProperty("PNG-on-tick")));
        modelInit();
        Thread modelthread = new Thread(this);
        setRun(floodModelParameters.isRunOnStartUp()); // don't start running on program startup
        modelthread.start();
    }
}