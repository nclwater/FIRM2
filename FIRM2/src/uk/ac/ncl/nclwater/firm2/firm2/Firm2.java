package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.examples.FloodPlain.Terrain;
import uk.ac.ncl.nclwater.firm2.examples.conway.Alive;
import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.utils.Grid;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class Firm2 extends Model {
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
            modelParameters.setWidth(Integer.parseInt(line.substring(1, line.length()).split("\t")[1]));
            line = sc.nextLine();
            modelParameters.setHeight(Integer.parseInt(line.substring(1, line.length()).split("\t")[1]));
            this.grid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
            for (int i = 0; i < 5; i++) {
                sc.nextLine();
            }
            // Create grid
            for (int row = 0; row < modelParameters.getHeight(); row++) {
                line = sc.nextLine();

                String tokens[] = line.substring(1,line.length() - 1).split("\t");
                for (int col = 0; col < modelParameters.getWidth(); col++) {
                    int id = getNewId();
//                    System.out.println(tokens[col]);
                    this.grid.setCell(col, row, new Terrain(id, Float.parseFloat(tokens[col])));
                    if (Float.parseFloat(tokens[col]) == -9999) {
                        this.grid.getCell(col, row).setColour(Color.blue);
                    } else {
                        this.grid.getCell(col, row).setColour(new Color(170, 170, 170));
                    }

                }
            }
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

    public void readRoad() {
        try {
            Scanner sc = new Scanner(new File("../data/roads.txt"));
            String[] tokens = sc.nextLine().split(" ");
//            System.out.println(tokens);
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

        printGrid('x', null);
    }

    public static void main(String[] args) {
        Firm2 model = new Firm2();
        Thread modelthread = new Thread(model);
        model.setRun(false); // don't start running on program startup
        modelthread.start();
    }
}
