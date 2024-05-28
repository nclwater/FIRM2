package uk.ac.ncl.nclwater.firm2.firm2;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.utils.Grid;

import java.awt.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

public class Firm2 extends Model {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private FloodModelParameters floodModelParameters;

    private float x_origin;
    private float y_origin;
    private int cellMeters;
    private int _NODATA;
    Properties properties = Utilities.createPropertiesFile();
    Long modelTimeStamp = 0L;
    ModelState modelState = new ModelState();
    int modelStateIndex = 0;
    ModelStateChanges modelStateChanges;

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
            plotDefences();
            plotRoads();
            modelStateChanges = readTimeLine();
            modelState = modelStateChanges.get(modelStateIndex);
            // Visualise if visualisation is set to true
            if (floodModelParameters.isVisualise()) {
                visualisation = new Visualisation(this);
            }
            // Do an initial tick
            tick();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Read the file containing the terrain elevations. If a tile is marked as null it is ocean and the terrain agent
     * should be set to the negative default water level. If the tile has an elevation the water level of the the water
     * agent should be set to zero.
     *
     * @param globalVariables
     */
    private void plotWaterAndTerrain(GlobalVariables globalVariables) {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        // Read the file to populate the basic grid of cells
        Grid terrainGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "terrain");
        Grid waterGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "water");

        String filename = (properties.getProperty("input-data") + properties.getProperty("terrain-data").replaceFirst(".txt", ".json"));
        System.out.println("Read file: " + filename);
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
                                getHeightmapGradient(terrainLine.getElevation()[grid_x],
                                        globalVariables.getMinHeight(),
                                        globalVariables.getMaxHeight()));
                        waterGrid.setCell(grid_x, grid_y, new Water(id, 0, false));
                    } else {
                        terrainGrid.setCell(grid_x, grid_y, new Terrain(id, -floodModelParameters.getOceanDepth()));

                        waterGrid.setCell(grid_x, grid_y, new Water(id, floodModelParameters.getOceanDepth(), true));
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
            System.out.println("Filename: " + properties.getProperty("roads-data").replaceFirst(".txt", ".json"));
            Roads roads = gson.fromJson(
                    new FileReader(properties.getProperty("input-data")
                            + properties.getProperty("roads-data").replaceFirst(".txt", ".json")),
                    Roads.class);
            roads.getRoads().forEach(r -> {
                ArrayList<PointDouble> roadPoints = r.getPolylineCoordinates();
                ArrayList<Point> pixelPoints = new ArrayList<>();
                ArrayList<Point> wholeRoad = new ArrayList<>();
                for (PointDouble roadPoint : roadPoints) {
                    Point coords = Ordinance2GridXY(x_origin, y_origin, (float) roadPoint.getX() / 1000,
                            (float) roadPoint.getY() / 1000, cellMeters);
                    coords.y = floodModelParameters.getHeight() - 1 - coords.y; // flip horizontally
                    pixelPoints.add(coords);
                }
                for (int i = 1; i < pixelPoints.size(); i++) {
                    wholeRoad.addAll(interpolate(pixelPoints.get(i - 1).x, pixelPoints.get(i - 1).y,
                            pixelPoints.get(i).x, pixelPoints.get(i).y));
                }
                wholeRoad.forEach(point -> {
                    if (point.x > 0 && point.x < floodModelParameters.getWidth() && point.y > 0 && point.y < floodModelParameters.getHeight()) {
                        Road newRoad = new Road(getNewId(), r.getRoadIDs());
                        roadGrid.setCell(point.x, point.y, newRoad);
                    } else {
                        //System.out.print("Road: " + point.x + ", " + point.y + " is out of bounds\n");
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
            Buildings buildings = gson.fromJson(new FileReader(properties.getProperty("input-data") +
                            properties.getProperty("buildings-data").replaceFirst(".txt", ".json")),
                    Buildings.class);
            Grid buildingGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "buildings");
            buildings.getBuildings().forEach(b -> {
                Point coords = Ordinance2GridXY(x_origin, y_origin, (float) b.getOrdinate().getX(),
                        (float) b.getOrdinate().getY(), cellMeters);
                coords.y = floodModelParameters.getHeight() - 1 - coords.y; // flip horizontally
                int type = b.getType();
                if (coords.x > 0 && coords.x < floodModelParameters.getWidth() && coords.y > 0 && coords.y < floodModelParameters.getHeight()) {
                    Building building = new Building(getNewId(), type);
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
            Defences defences = gson.fromJson(
                    new FileReader(properties.getProperty("input-data") +
                            properties.getProperty("defences-data").replaceFirst(".txt", ".json")), Defences.class);
            defences.getDefences().forEach(d -> {
                Point coords = Ordinance2GridXY(x_origin, y_origin, (float) d.getOrdinate().getX(),
                        (float) d.getOrdinate().getY(), cellMeters);
                coords.y = floodModelParameters.getHeight() - 1 - coords.y; // flip horizontally
                if (coords.x > 0 && coords.x < floodModelParameters.getWidth() && coords.y > 0 && coords.y < floodModelParameters.getHeight()) {
                    Defence defence = new Defence(getNewId());
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

    @Override
    public void tick() {
        // FYI https://www.unixtimestamp.com/
        // increment time - model start time + tick time value
        modelTimeStamp += floodModelParameters.getTickTimeValue();
        int hours = Integer.parseInt(modelState.getTime().split(":")[0]);
        int minutes = Integer.parseInt(modelState.getTime().split(":")[1]);
        long timestamp = (floodModelParameters.getTimestamp() + (hours * 3600L) + (minutes * 60L));
        if (timestamp == modelTimeStamp) {
            logger.debug("STATE CHANGE");
            Grid newWaterGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(), floodModelParameters.isToroidal(), "water");
            Grid water = grids.get("water");
            Grid terrain = grids.get("terrain");
            for (int row = 0; row < floodModelParameters.getHeight(); row++) {
                for (int col = 0; col < floodModelParameters.getWidth(); col++) {
                    // check if the water level has risen above sea level
                    if (((Terrain) terrain.getCell(col, row)).getElevation() + ((Water) water.getCell(col, row)).getWaterLevel() > 0) {
                        logger.debug("FLOOD: " + row + ", " + col);
                    }
                }
            }
            modelState = modelStateChanges.get(modelStateIndex);
        }
        if (floodModelParameters.isVisualise()) {
            visualisation.getDrawPanel().repaint();
        }

//        printGrid('x', null);
    }

    public ModelStateChanges readTimeLine() {
        ModelStateChanges modelStateChanges;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            modelStateChanges = gson.fromJson(
                    new FileReader(properties.getProperty("input-data") + "/timeline.json"),
                    ModelStateChanges.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return modelStateChanges;
    }


    class GradientData {
        public Color value;
        public float threshold;

        public GradientData(Color v, float t) {
            value = v;
            threshold = t;
        }
    }

    private Color getHeightmapGradient(float height, float height_min, float height_max) {
        final GradientData[] gradient = new GradientData[]{
                new GradientData(new Color(0xe0, 0xce, 0xb5, 0xff), 0.0f),
                new GradientData(new Color(0x97, 0x70, 0x3c, 0xff), 0.5f),
                new GradientData(new Color(0x0B, 0x08, 0x04, 0xff), 1.0f),
        };

        float threshold = (height - height_min) / height_max;

        for (int i = 1; i < gradient.length; i++) {
            if (threshold <= gradient[i].threshold) {
                float t = (threshold - gradient[i - 1].threshold) / gradient[i].threshold;
                return new Color(
                        (gradient[i - 1].value.getRed() * (1.0f - t) + gradient[i].value.getRed() * t) / 255.0f,
                        (gradient[i - 1].value.getGreen() * (1.0f - t) + gradient[i].value.getGreen() * t) / 255.0f,
                        (gradient[i - 1].value.getBlue() * (1.0f - t) + gradient[i].value.getBlue() * t) / 255.0f,
                        (gradient[i - 1].value.getAlpha() * (1.0f - t) + gradient[i].value.getAlpha() * t) / 255.0f
                );
            }
        }
        return gradient[gradient.length - 1].value;
    }

    /**
     * Default constructor
     */
    public Firm2() {
        super(new FloodModelParameters());
        floodModelParameters = (FloodModelParameters) modelParameters;
        // load properties file or create one if it doesn't exist and add default values
        Utilities.createPropertiesFile();
        properties = Utilities.loadPropertiesFile();
        floodModelParameters.setToroidal(Boolean.parseBoolean(properties.getProperty("toroidal")));
        floodModelParameters.setTicks(Integer.parseInt(properties.getProperty("ticks")));
        floodModelParameters.setVisualise(Boolean.parseBoolean(properties.getProperty("visualise")));
        floodModelParameters.setCell_size(Integer.parseInt(properties.getProperty("cell-size")));
        floodModelParameters.setChance(Integer.parseInt(properties.getProperty("chance")));
        floodModelParameters.setTitle(String.valueOf(properties.get("title")));
        floodModelParameters.setOceanDepth(Float.parseFloat(properties.getProperty("ocean-depth")));
        floodModelParameters.setTimestamp(Long.parseLong(properties.getProperty("time-stamp")));
        floodModelParameters.setTickTimeValue(Long.parseLong(properties.getProperty("tick-time-value")));
        modelTimeStamp = floodModelParameters.getTimestamp(); // start time for model
        floodModelParameters.setSlowdown(0);
        modelInit();
    }

    /**
     * This is where the program starts
     */
    public static void main(String[] args) {
        Firm2 model = new Firm2();
        Thread modelthread = new Thread(model);
        model.setRun(true); // don't start running on program startup
        modelthread.start();
    }
}