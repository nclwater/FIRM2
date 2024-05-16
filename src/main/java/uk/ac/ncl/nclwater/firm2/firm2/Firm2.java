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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

public class Firm2 extends Model {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private float x_origin;
    private float y_origin;
    private int cellMeters;
    private int _NODATA;
    Properties properties = Utilities.createPropertiesFile();

    /**
     * Initialise the model
     */
    @Override
    public void modelInit() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // Read global variable (eventually to be read from environment vars for DAPHNE)
            GlobalVariables globalVariables = gson.fromJson(new FileReader(
                            properties.getProperty("input-data") + properties.getProperty("model-parameters")),
                    GlobalVariables.class);
            modelParameters.setWidth(globalVariables.getColumns());
            modelParameters.setHeight(globalVariables.getRows());


            // Read the file to populate the basic grid of cells
            Grid terrainGrid1 = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            Grid waterGrid1 = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());

            String filename = (properties.getProperty("input-data") + properties.getProperty("terrain-data").replaceFirst(".txt", ".json"));
            System.out.println("Read file: " + filename);
            TerrainLayer terrainLayer = gson.fromJson(new FileReader(filename), TerrainLayer.class);
            for (int grid_y = 0; grid_y < modelParameters.getHeight(); grid_y++) {
                TerrainLine terrainLine = terrainLayer.get(grid_y);
                for (int grid_x = 0; grid_x < modelParameters.getWidth(); grid_x++) {
                    int id = getNewId();
                    if (terrainLine.getElevation()[grid_x] != null) {
                        terrainGrid1.setCell(grid_x, grid_y, new Terrain(id, terrainLine.getElevation()[grid_x]));
                        terrainGrid1.getCell(grid_x, grid_y).setColour(
                                getHeightmapGradient(terrainLine.getElevation()[grid_x],
                                        globalVariables.getMinHeight(),
                                        globalVariables.getMaxHeight()));
                    } else {
                        terrainGrid1.setCell(grid_x, grid_y, new Water(id));
                    }
                }
            }

            x_origin = globalVariables.getLowerLeftX();
            y_origin = globalVariables.getLowerLeftY();
            cellMeters = globalVariables.getCellSize();

            grids.add(terrainGrid1);
            grids.add(waterGrid1);
            plotBuildings(); // Do plotRoads first so that x and y origins are set
            plotDefences();
            plotRoads();
            // Visualise if visualisation is set to true
            if (modelParameters.isVisualise()) {
                visualisation = new Visualisation(this);
            }
            // Do an initial tick
            tick();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void plotRoads() {
//
//         		;; manually fix up the bridge over the river.
//         		;; XXX this should be done from a config file.
//         		ask roads with [road-oid = "4000000012487984"] [set road-elevation 10]
//
        try {
//            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // read file containing the road co-ordinates
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("roads-data")));
            // Create a layer for the roads
            Grid roadGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            int segment = 0;
            while (sc.hasNext()) {
                segment = (segment > 2) ? 0 : ++segment;
                String line = sc.nextLine();
                if (!line.trim().equals("") && !line.trim().startsWith("%")) {
                    line = trimBrackets(line);
                    // trim off the brackets and parse the line
                    int firstBracket = line.indexOf('[');
                    String topHalf = line.trim().substring(0, firstBracket);
                    String[] road_ids = new String[3];
                    road_ids[0] = topHalf.split(" ")[0].replace('"', ' ').trim();
                    road_ids[1] = topHalf.split(" ")[1].replace('"', ' ').trim();
                    road_ids[2] = topHalf.split(" ")[2].replace('"', ' ').trim();
                    String bottomHalf = trimBrackets(line.trim().substring(firstBracket));
                    String match = "] \\[";
                    String[] coordinates = trimBrackets(bottomHalf).split(match); // extract item 5 which contain co-ordinates
                    ArrayList<Point> roadPoints = new ArrayList<>();
                    // read all roadsegment points into an ArrayList of points
                    for (String coordinate : coordinates) {
                        String[] xy = (coordinate).split(" ");
                        Point coords = Ordinance2GridXY(x_origin, y_origin, Float.parseFloat(xy[0]) / 1000,
                                Float.parseFloat(xy[1]) / 1000, cellMeters);
                        coords.y = modelParameters.getHeight() - 1 - coords.y; // flip horizontally
                        roadPoints.add(coords);
                    }
                    // infer line between points and add to one big array.
                    ArrayList<Point> wholeRoad = new ArrayList<>();
                    for (int i = 1; i < roadPoints.size(); i++) {
                        wholeRoad.addAll(interpolate(roadPoints.get(i - 1).x, roadPoints.get(i - 1).y, roadPoints.get(i).x, roadPoints.get(i).y));
                    }
                    wholeRoad.forEach(point -> {
                        if (point.x > 0 && point.x < modelParameters.getWidth() && point.y > 0 && point.y < modelParameters.getHeight()) {
                            Road newRoad = new Road(getNewId(), road_ids);
//                            newRoad.setColour();
                            roadGrid.setCell(point.x, point.y, newRoad);
                        } else {
                            System.out.print("Road: " + point.x + ", " + point.y + " is out of bounds\n");
                        }
                    });
                }
            }
            grids.add(roadGrid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void plotBuildings() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            Buildings buildings = gson.fromJson(new FileReader(properties.getProperty("input-data") +
                            properties.getProperty("buildings-data").replaceFirst(".txt", ".json")),
                    Buildings.class);
            Grid buildingGrid1 = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            buildings.getBuildings().forEach(b -> {
                Point coords = Ordinance2GridXY(x_origin, y_origin, (float) b.getOrdinate().getX(),
                        (float) b.getOrdinate().getY(), cellMeters);
                coords.y = modelParameters.getHeight() - 1 - coords.y; // flip horizontally
                int type = b.getType();
                if (coords.x > 0 && coords.x < modelParameters.getWidth() && coords.y > 0 && coords.y < modelParameters.getHeight()) {
                    Building building = new Building(getNewId(), type);
                    buildingGrid1.setCell(coords.x, coords.y, building);
                }
            });
            grids.add(buildingGrid1);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void plotDefences() {
        Grid defenceGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            Defences defences = gson.fromJson(
                    new FileReader(properties.getProperty("input-data") +
                            properties.getProperty("defences-data").replaceFirst(".txt", ".json")), Defences.class);
            defences.getDefences().forEach(d -> {
                    Point coords = Ordinance2GridXY(x_origin, y_origin, (float) d.getOrdinate().getX(),
                            (float) d.getOrdinate().getY(), cellMeters);
                    coords.y = modelParameters.getHeight() - 1 - coords.y; // flip horizontally
                if (coords.x > 0 && coords.x < modelParameters.getWidth() && coords.y > 0 && coords.y < modelParameters.getHeight()) {
                    Defence defence = new Defence(getNewId());
                    defenceGrid.setCell(coords.x, coords.y, defence);
                } else {
//                    logger.debug("Building: " + coords.x + ", " + coords.y + " is out of bounds");
                }
            });

            grids.add(defenceGrid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void tick() {
        Grid newGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {

            }
        }
        if (modelParameters.isVisualise()) {
            visualisation.getDrawPanel().repaint();
        }

//        printGrid('x', null);
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
        final float heightMin = height_min;//0.0f;
        final float heightMax = height_max;//200.0f;
        final GradientData[] gradient = new GradientData[]{
                new GradientData(new Color(0xe0, 0xce, 0xb5, 0xff), 0.0f),
                new GradientData(new Color(0x97, 0x70, 0x3c, 0xff), 0.5f),
                new GradientData(new Color(0x0B, 0x08, 0x04, 0xff), 1.0f),
        };

        float threshold = (height - heightMin) / heightMax;

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
        // load properties file or create one if it doesn't exist and add default values
        Utilities.createPropertiesFile();
        properties = Utilities.loadPropertiesFile();
        modelParameters.setToroidal(Boolean.parseBoolean(properties.getProperty("toroidal")));
        modelParameters.setTicks(Integer.parseInt(properties.getProperty("ticks")));
        modelParameters.setVisualise(Boolean.parseBoolean(properties.getProperty("visualise")));
        modelParameters.setCell_size(Integer.parseInt(properties.getProperty("cell-size")));
        modelParameters.setChance(Integer.parseInt(properties.getProperty("chance")));
        modelParameters.setTitle(String.valueOf(properties.get("title")));
        modelInit();
    }

    /**
     * This is where the program starts
     */
    public static void main(String[] args) {
        Firm2 model = new Firm2();
        Thread modelthread = new Thread(model);
        model.setRun(false); // don't start running on program startup
        modelthread.start();
    }
}
