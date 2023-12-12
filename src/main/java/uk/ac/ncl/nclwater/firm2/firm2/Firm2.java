package uk.ac.ncl.nclwater.firm2.firm2;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.utils.Grid;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.model.Utilities.*;

public class Firm2 extends Model {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private float x_origin;
    private float y_origin;
    private int cellMeters;
    private int _NODATA;

    /**
     * Default constructor
     */
    public Firm2() {
        modelParameters.setToroidal(false);
        modelParameters.setTicks(30);
        modelParameters.setVisualise(true);
        modelParameters.setCell_size(3);
        modelParameters.setChance(50);
        modelParameters.setTitle("FIRM2");
        modelInit();
    }

    @Override
    public void modelInit() {
        try {
            // Read the file to populate the basic grid of cells
            Scanner sc = new Scanner(new File("/data/inputs/terrain.txt"));
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
            plotRoads();
            plotBuildings(); // Do plotRoads first so that x and y origins are set
            plotDefences();
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
        try {
            // read file containing the road co-ordinates
            Scanner sc = new Scanner(new File("/data/inputs/roads.txt"));
            // Create a layer for the roads
            Grid roadGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());;
            while (sc.hasNext()) {
                String line = trimBrackets(sc.nextLine());

                // trim off the brackets and parse the line
                int firstBracket = line.indexOf('[');
                String topHalf = line.trim().substring(0,firstBracket);
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
                    wholeRoad.addAll(interpolate(roadPoints.get(i-1).x, roadPoints.get(i-1).y, roadPoints.get(i).x, roadPoints.get(i).y));
                }
                wholeRoad.forEach(point -> {
                    if (point.x > 0 && point.x < modelParameters.getWidth() && point.y > 0 && point.y < modelParameters.getHeight()) {
                        Road newRoad = new Road(getNewId());
                        newRoad.setColour(Color.BLACK);
                        roadGrid.setCell(point.x, point.y, newRoad);
                    } else {
                        System.out.printf("Road: " + point.x + ", " + point.y + " is out of bounds");
                    }
                });
                grids.add(roadGrid);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void plotBuildings() {
        try {
            Scanner sc = new Scanner(new File("/data/inputs/buildings.txt"));
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
            Scanner sc = new Scanner(new File("/data/inputs/defences.txt"));
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
//                    Terrain tmp = (Terrain)this.grid.getCell(coords.x, coords.y);
//                    Defence defence = new Defence(getNewId());
//                    tmp.setSurfaceAgent(defence);
//                    this.grid.getCell(coords.x, coords.y).setColour(defence.getColour());
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
