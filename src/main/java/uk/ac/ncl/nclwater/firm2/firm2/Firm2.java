package uk.ac.ncl.nclwater.firm2.firm2;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.utils.Grid;
import java.nio.file.Paths;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.model.Utilities.*;

public class Firm2 extends Model {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private float x_origin;
    private float y_origin;
    private int cellMeters;
    private int _NODATA;
    Properties properties = new Properties();
    private static final String APPLICATION_DIRECTORY = System.getProperty("user.home");
    private static final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";

    /**
     * Default constructor
     */
    public Firm2() {
        // load properties file or create one if it doesn't exist and add default values
        if (!Files.exists(Paths.get(PROPERTIES_FILEPATH))) {
            createPropertiesFile();
        }
        loadPropertiesFile();
        modelParameters.setToroidal(Boolean.valueOf(properties.getProperty("toroidal")));
        modelParameters.setTicks(Integer.valueOf(properties.getProperty("ticks")));
        modelParameters.setVisualise(Boolean.valueOf(properties.getProperty("visualise")));
        modelParameters.setCell_size(Integer.valueOf(properties.getProperty("cell-size")));
        modelParameters.setChance(Integer.valueOf(properties.getProperty("chance")));
        modelParameters.setTitle(String.valueOf(properties.get("title")));
        modelInit();
    }

    /**
     * Load properties from the properties file
     */
    private void loadPropertiesFile() {
        try (InputStream input = new FileInputStream(PROPERTIES_FILEPATH)) {
            // load a properties file
            properties.load(input);
            System.out.println("Properties read from " + PROPERTIES_FILEPATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create a new properties file and set default properties
     */
    private void createPropertiesFile() {
        File propertiesFile = new File(PROPERTIES_FILEPATH);
        try {
            OutputStream output = new FileOutputStream(propertiesFile);
            properties.setProperty("toroidal","false");
            properties.setProperty("ticks","30");
            properties.setProperty("visualise","TRUE");
            properties.setProperty("cell-size","3");
            properties.setProperty("chance","50");
            properties.setProperty("application-title","FIRM2");
            properties.setProperty("input-data", "/data/inputs/");
            properties.setProperty("output-data", "/data/outputs/");
            properties.setProperty("terrain-data", "terrain.txt");
            properties.setProperty("roads-data", "roads.txt");
            properties.setProperty("buildings-data", "buildings.txt");
            properties.setProperty("defences-data", "defences.txt");
            properties.store(output, null);
            System.out.println("File " + propertiesFile.getAbsolutePath() + " created");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialise the model
     */
    @Override
    public void modelInit() {
        try {
            // Read the file to populate the basic grid of cells
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("terrain-data")));
            String line = sc.nextLine();
            modelParameters.setWidth(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            modelParameters.setHeight(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            x_origin = (Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            y_origin = (Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            cellMeters = (Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            _NODATA = (Integer.parseInt(trimBrackets(line).split("\t")[1]));
            Grid terrainGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            Grid waterGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            line = sc.nextLine(); // read and ignore the first line
            float maxheight = 0;
            float minheight = 0;
            // Create grid

            for (int row = 0; row < modelParameters.getHeight(); row++) {
                line = sc.nextLine();

                String tokens[] = line.substring(1,line.length() - 1).split("\t");
                for (int col = 0; col < modelParameters.getWidth(); col++) {
                    int id = getNewId();
                    float elevation = Float.parseFloat(tokens[col]);
                    maxheight = (elevation > maxheight)?elevation:maxheight;
                    minheight = (elevation < minheight && elevation != -9999.0)?elevation:minheight;
                    terrainGrid.setCell(col, row, new Terrain(id, elevation));

                    if (Float.parseFloat(tokens[col]) == _NODATA) {
                        waterGrid.setCell(col, row, new Water(getNewId()));
                    } else {
                        terrainGrid.getCell(col, row);
                        terrainGrid.getCell(col, row).setColour(getHeightmapGradient(elevation));
                    }

                }
            }
            grids.add(terrainGrid);
            grids.add(waterGrid);
//            logger.info("Max height: " + maxheight);
//            logger.info("Min height: " + minheight);
            plotBuildings(); // Do plotRoads first so that x and y origins are set
            plotDefences();
            plotRoads();
            // Visualise if visualisation is set to true
            if (modelParameters.isVisualise()) {
                visualisation = new Visualisation(this);
            }
            // Do an initial tick
            tick();
            sc.close();
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
            // read file containing the road co-ordinates
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("roads-data")));
            // Create a layer for the roads
            Grid roadGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            int segment = 0;
            while (sc.hasNext()) {
                segment = (segment > 2)?0:++segment;
                String line = sc.nextLine();
                if (!line.trim().equals("") && !line.trim().startsWith("%")) {
                    line = trimBrackets(line);
                    // trim off the brackets and parse the line
                    int firstBracket = line.indexOf('[');
                    String topHalf = line.trim().substring(0, firstBracket);
                    String road_ids[] = new String[3];
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
                            System.out.printf("Road: " + point.x + ", " + point.y + " is out of bounds\n");
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
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("buildings-data")));
            Grid buildingGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());;
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                if (!line.startsWith(";;") && !line.trim().equals("") && !(line == null)) {
                    line = trimBrackets(line.trim().strip());
                    String[] xy = (line).split(" ");
                    Point coords = Ordinance2GridXY(x_origin, y_origin, Float.parseFloat(xy[0]),
                            Float.parseFloat(xy[1]), cellMeters);
                    coords.y = modelParameters.getHeight() - 1 - coords.y; // flip horizontally
                    int type = Integer.parseInt(xy[2]);
                    if (coords.x > 0 && coords.x < modelParameters.getWidth() && coords.y > 0 && coords.y < modelParameters.getHeight()) {
                        Building building = new Building(getNewId(), type);
                        buildingGrid.setCell(coords.x, coords.y, building);
                    } else {
//                        logger.debug("Building: " + coords.x + ", " + coords.y + " is out of bounds");
                    }
                }
            }
            grids.add(buildingGrid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void plotDefences() {
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("defences-data")));
            Grid defenceGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());;

            while (sc.hasNext()) {
                String[] line = trimBrackets(sc.nextLine().trim()).split("\t");
                if (line.length > 2) {
                    line[2] =  trimQuotes(line[2]);
                }
                Point coords = Ordinance2GridXY(x_origin, y_origin, Float.parseFloat(line[0]),
                        Float.parseFloat(line[1]), cellMeters);
                coords.y = modelParameters.getHeight() - 1 - coords.y; // flip horizontally
                if (coords.x > 0 && coords.x < modelParameters.getWidth() && coords.y > 0 && coords.y < modelParameters.getHeight()) {
                    Defence defence = new Defence(getNewId());
                    defenceGrid.setCell(coords.x, coords.y, defence);
                } else {
//                    logger.debug("Building: " + coords.x + ", " + coords.y + " is out of bounds");
                }
            }
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

    private Color getHeightmapGradient(float height) {
        final float heightMin = 0.0f;
        final float heightMax = 200.0f;
        final GradientData[] gradient = new GradientData[]{
            new GradientData(new Color(0xe0, 0xce, 0xb5, 0xff), 0.0f),
            new GradientData(new Color(0x97, 0x70, 0x3c, 0xff), 0.5f),
            new GradientData(new Color(0x0B, 0x08, 0x04, 0xff), 1.0f),
        };

        float threshold = (height - heightMin) / heightMax;

        for (int i = 1; i < gradient.length; i++) {
            if (threshold <= gradient[i].threshold) {
                float t = (threshold - gradient[i - 1].threshold) / gradient[i].threshold;
                Color result = new Color(
                    (gradient[i - 1].value.getRed() * (1.0f - t) + gradient[i].value.getRed() * t) / 255.0f,
                    (gradient[i - 1].value.getGreen() * (1.0f - t) + gradient[i].value.getGreen() * t) / 255.0f,
                    (gradient[i - 1].value.getBlue() * (1.0f - t) + gradient[i].value.getBlue() * t) / 255.0f,
                    (gradient[i - 1].value.getAlpha() * (1.0f - t) + gradient[i].value.getAlpha() * t) / 255.0f
                );
                return result;
            }
        }
        return gradient[gradient.length - 1].value;
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
