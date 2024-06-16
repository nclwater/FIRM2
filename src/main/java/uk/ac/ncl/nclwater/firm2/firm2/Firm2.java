package uk.ac.ncl.nclwater.firm2.firm2;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.model.utils.Grid;
import uk.ac.ncl.nclwater.firm2.model.utils.Utils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;
import static uk.ac.ncl.nclwater.firm2.model.utils.Utils.getHeightmapGradient;

public class Firm2 extends Model {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static FloodModelParameters floodModelParameters;

    private float x_origin;
    private float y_origin;
    private int cellMeters;
    private int _NODATA;
    Properties properties = createPropertiesFile();
    Long modelTimeStamp = 0L;
    ModelState modelState = new ModelState();
    int modelStateIndex = 0;
    ModelStateChanges modelStateChanges;
    Float maintainSeaLevel;


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
            floodModelParameters.setWidth(globalVariables.getColumns());
            floodModelParameters.setHeight(globalVariables.getRows());


            plotWaterAndTerrain(globalVariables);
            plotBuildings();
            plotRoads();
            plotDefences();
            modelStateChanges = readTimeLine();
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
     * Read the file containing the terrain elevations. If a tile is marked as null it is ocean and the terrain agent
     * should be set to the negative default water level. If the tile has an elevation the water level of the water
     * agent should be set to zero.
     *
     * @param globalVariables
     */
    private void plotWaterAndTerrain(GlobalVariables globalVariables) {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        // Read the file to populate the basic grid of cells
        Grid terrainGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "terrain");
        Grid waterGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "water");

        String filename = (properties.getProperty("input-data") + properties.getProperty("terrain-data"));
        logger.debug("Reading: {}", filename);
        TerrainLayer terrainLayer = null;
        try {
            terrainLayer = gson.fromJson(new FileReader(filename), TerrainLayer.class);

            for (int grid_y = 0; grid_y < floodModelParameters.getHeight(); grid_y++) {
                TerrainLine terrainLine = terrainLayer.get(grid_y);
                for (int grid_x = 0; grid_x < floodModelParameters.getWidth(); grid_x++) {
                    int id = getNewId();
                    // if null assume tile is ocean
                    if (terrainLine.getElevation()[grid_x] != null) {
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

            x_origin = globalVariables.getLowerLeftX();
            y_origin = globalVariables.getLowerLeftY();
            cellMeters = globalVariables.getCellSize();

            grids.put("terrain", terrainGrid);
            grids.put("water", waterGrid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read the roads.json configuration from file and populate the road grid
     */
    private void plotRoads() {
//
//         		;; manually fix up the bridge over the river.
//         		;; XXX this should be done from a config file.
//         		ask roads with [road-oid = "4000000012487984"] [set road-elevation 10]
//
        try {
            Grid roadGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "roads");
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("roads-data");
            logger.debug("Reading: {}", filename);
            Roads roads = gson.fromJson(new FileReader(filename), Roads.class);
            roads.getRoads().forEach(r -> {
                ArrayList<PointDouble> roadPoints = r.getPolylineCoordinates();
                ArrayList<Point> pixelPoints = new ArrayList<>();
                ArrayList<Point> wholeRoad = new ArrayList<>();
                for (PointDouble roadPoint : roadPoints) {
                    // Road co-ordinates have to be divided after read from the json file
                    Point coords = Ordinance2GridXY(x_origin, y_origin, (float) roadPoint.getX() / 1000,
                            (float) roadPoint.getY() / 1000, cellMeters);
                    // flip horizontally
                    coords.y = floodModelParameters.getHeight() - 1 - coords.y;
                    pixelPoints.add(coords);
                }
                for (int i = 1; i < pixelPoints.size(); i++) {
                    wholeRoad.addAll(interpolate(pixelPoints.get(i - 1).x, pixelPoints.get(i - 1).y,
                            pixelPoints.get(i).x, pixelPoints.get(i).y));
                }
                wholeRoad.forEach(point -> {
                    if (point.x > 0 && point.x < floodModelParameters.getWidth() && point.y > 0 && point.y < floodModelParameters.getHeight()) {
                        Road newRoad = new Road(getNewId(), r.getRoadIDs());
                        newRoad.setRoadLength(r.getRoadLength());
                        newRoad.setRoadType(r.getRoadType());
                        roadGrid.setCell(point.x, point.y, newRoad);
                    } else {
                        logger.trace("Road: {}, {} is out of bounds", point.x, point.y);
                    }
                });
            });
            grids.put("roads", roadGrid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read the building.json file and populate the buildings grid
     */
    private void plotBuildings() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("buildings-data");
            Buildings buildings = gson.fromJson(new FileReader(filename), Buildings.class);
            logger.debug("Reading: {}", filename);
            Grid buildingGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                    floodModelParameters.isToroidal(), "buildings");
            buildings.getBuildings().forEach(b -> {
                Point coords = Ordinance2GridXY(x_origin, y_origin, (float) b.getOrdinate().getX(),
                        (float) b.getOrdinate().getY(), cellMeters);
                coords.y = floodModelParameters.getHeight() - 1 - coords.y; // flip horizontally
                int type = b.getType();
                if (coords.x > 0 && coords.x < floodModelParameters.getWidth() &&
                        coords.y > 0 && coords.y < floodModelParameters.getHeight()) {
                    Building building = new Building(getNewId(), type, b.getOrdinate(), b.getNearestRoad_ID());
                    buildingGrid.setCell(coords.x, coords.y, building);
                }
            });
            grids.put("buildings", buildingGrid);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read the defences.json file and populate the defences grid
     */
    public void plotDefences() {
        Grid defenceGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "defences");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("defences-data");
            Defences defences = gson.fromJson(new FileReader(filename), Defences.class);
            logger.debug("Reading: {}", filename);
            defences.getDefences().forEach(d -> {
                Point coords = Ordinance2GridXY(x_origin, y_origin, (float) d.getOrdinate().getX(),
                        (float) d.getOrdinate().getY(), cellMeters);
                coords.y = floodModelParameters.getHeight() - 1 - coords.y; // flip horizontally
                if (coords.x > 0 && coords.x < floodModelParameters.getWidth() && coords.y > 0 && coords.y < floodModelParameters.getHeight()) {
                    Defence defence = new Defence(getNewId(), d.getOrdinate(), d.getName(), d.getHeight());
                    defenceGrid.setCell(coords.x, coords.y, defence);
                } else {
                    logger.trace("Building: " + coords.x + ", " + coords.y + " is out of bounds");
                }
            });

            grids.put("defences", defenceGrid);
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
        // increment time - model start time + tick time value
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
        Grid waterGrid = grids.get("water");
        Grid terrainGrid = grids.get("terrain");
        Grid defenceGrid = grids.get("defences");
        Grid newWaterGrid = new Grid(waterGrid.getWidth(), waterGrid.getHeight(), waterGrid.isIs_toroidal(), waterGrid.getGridName());
        // Initialise new grid to be the same as the old grid.
        for (int row = 0; row < waterGrid.getHeight(); row++) {
            for (int col = 0; col < waterGrid.getWidth(); col++) {
                newWaterGrid.setCell(col, row, new Water(waterGrid.getCell(col, row).getAgent_id(),
                        ((Water) waterGrid.getCell(col, row)).getWaterLevel(), ((Water) waterGrid.getCell(col, row)).isOcean()));
            }
        }

        // reset sea level for ocean cells on every tick
        logger.debug("Sea level: {}", maintainSeaLevel);
        setSeaLevel(waterGrid, maintainSeaLevel);
        // If there is a timestamp in the timeline for current time, execute the state change
        if (timestamp == modelTimeStamp) {
            Float stateSeaLevel = modelState.getSeaLevel();
            modelStateIndex = (modelStateIndex + 1 < modelStateChanges.getModelStates().size())?modelStateIndex + 1:modelStateIndex;
            // get sealevel change
            if (stateSeaLevel != null) {
                setSeaLevel(waterGrid, stateSeaLevel);
                maintainSeaLevel = stateSeaLevel;
            }
            // get defence breaches
            ArrayList<String> defences = (ArrayList<String>) modelState.getDefenceBreach();;
            logger.debug(mts + ": STATE CHANGE: sea level: {}", modelState);

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
        moveWater(waterGrid, terrainGrid, defenceGrid, newWaterGrid);
        // read the next state change
        modelState = modelStateChanges.getModelStates().get(modelStateIndex);
        if (floodModelParameters.isVisualise()) {
            visualisation.getDrawPanel().repaint();
        }
    }

    /**
     * Restore ocean to level
     * @param waterGrid the water grid
     * @param level the level to which the ocean cells have to be restore
     */
    private void setSeaLevel(Grid waterGrid, float level) {
        for (int row = 0; row < floodModelParameters.getHeight(); row++) {
            for (int col = 0; col < floodModelParameters.getWidth(); col++) {
                Water w = (Water) waterGrid.getCell(col, row);
                if (w.isOcean()) w.setWaterLevel(level);
            }
        }
    }

    private void moveWater(Grid water, Grid terrain, Grid defence, Grid newWaterGrid) {
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
                //water with opacity on water level
//                    newWaterCell.setColour(Utils.getHeightmapGradient("water",
//                        newWaterCell.getWaterLevel(), 0, 20));
            }
        }
        grids.put("water", newWaterGrid);

        // Generate a one pixel per cell PNG image on tick
        if (floodModelParameters.isPngOnTick()) {
            grids.get("water").createPNG(properties.getProperty("output-data"), "water_" + Long.toString(modelTimeStamp));
            grids.get("terrain").createPNG(properties.getProperty("output-data"), "terrain_" + Long.toString(modelTimeStamp));
            grids.get("defences").createPNG(properties.getProperty("output-data"), "defences_" + Long.toString(modelTimeStamp));
        }
    }

    /**
     * Read the json file containing the timeline for the model. The timeline is an arraylist stored in the
     * ModeStateChanges class. Each item in the array is an event at a specific time and is stored in a ModelState
     * class
     *
     * @return
     */
    public ModelStateChanges readTimeLine() {
        ModelStateChanges modelStateChanges;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + "/timeline.json";
            modelStateChanges = gson.fromJson(new FileReader(filename), ModelStateChanges.class);
            logger.debug("Reading timeline: {}", filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return modelStateChanges;
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
        floodModelParameters.setTitle(String.valueOf(properties.get("title")));
        floodModelParameters.setOceanDepth(Float.parseFloat(properties.getProperty("ocean-depth")));
        floodModelParameters.setTimestamp(Long.parseLong(properties.getProperty("time-stamp")));
        floodModelParameters.setTickTimeValue(Long.parseLong(properties.getProperty("tick-time-value")));
        modelTimeStamp = floodModelParameters.getTimestamp() * 1000; // start time for model
        floodModelParameters.setSlowdown(Integer.parseInt(properties.getProperty("slowdown")));
        floodModelParameters.setTitle(properties.getProperty("application-title"));
        floodModelParameters.setPngOnTick(Boolean.parseBoolean(properties.getProperty("PNG-on-tick")));
        modelInit();
        Thread modelthread = new Thread(this);
        setRun(floodModelParameters.isRunOnStartUp()); // don't start running on program startup
        modelthread.start();
    }


}