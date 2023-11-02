package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.examples.FloodPlain.Terrain;
import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.utils.Grid;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Firm2 extends Model {

    float x_origin;
    float y_origin;
    int cellmeters;
    int _NODATA;

    /**
     * Default constructor
     */
    public Firm2() {
        System.out.println("Firm2");

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
        System.out.println("modelInit");
        try {
            // Read the file to populate the basic grid of cells
            Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/FIRM2/data/inputs/terrain.txt"));
            String line = sc.nextLine();
            modelParameters.setWidth(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            modelParameters.setHeight(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            x_origin = (Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            y_origin = (Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            cellmeters = (Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            _NODATA = (Integer.parseInt(trimBrackets(line).split("\t")[1]));
            this.grid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            line = sc.nextLine();

            // Create grid
            for (int row = 0; row < modelParameters.getHeight(); row++) {
                line = sc.nextLine();

                String tokens[] = line.substring(1,line.length() - 1).split("\t");
                for (int col = 0; col < modelParameters.getWidth(); col++) {
                    int id = getNewId();
                    this.grid.setCell(col, row, new Terrain(id, Float.parseFloat(tokens[col])));
                    if (Float.parseFloat(tokens[col]) == _NODATA) {
                        this.grid.getCell(col, row).setColour(Color.blue);
                    } else {
                        this.grid.getCell(col, row).setColour(new Color(170, 170, 170));
                    }

                }
            }
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
        try {
            Scanner sc = new Scanner(new File(System.getProperty("user.dir") +
                    "/FIRM2/data/inputs/roads.txt"));
            while (sc.hasNext()) {
                String line = trimBrackets(sc.nextLine());
                int firstBracket = line.indexOf('[');
                String topHalf = line.trim().substring(0,firstBracket);

                String bottomHalf = trimBrackets(line.trim().substring(firstBracket));
                String match = "] \\[";
                String[] coordinates = trimBrackets(bottomHalf).split(match); // extract item 5 which contain co-ordinates
                for (String coordinate : coordinates) {
                    System.out.println(coordinate);
                    String[] xy = (coordinate).split(" ");
                    int x_coord = Math.round(((Float.parseFloat(xy[0]) / 1000) - x_origin) / cellmeters);
                    int y_coord = Math.round(((Float.parseFloat(xy[1]) / 1000) - y_origin) / cellmeters);
                    y_coord = modelParameters.getHeight() - 1 - y_coord;
                    if (x_coord > 0 && x_coord < modelParameters.getWidth() && y_coord > 0 && y_coord < modelParameters.getHeight()) {
                        this.grid.getCell(x_coord, y_coord).setColour(Color.BLACK);
                        System.out.println("Change color: " + x_coord + ", " + y_coord);
                    } else {
//                        System.out.println((x_coord + ", " + y_coord + " is out of bounds"));
                    }

                }
            }
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

    /**
     * Helper method to rim brackets of a string
     * @param str The string to be trimmed
     * @return The trimmed string
     */
    private String trimBrackets(String str) {
        return str.substring(1,str.length()-1).trim();
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
