package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.examples.conway.Alive;
import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.utils.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Firm2 extends Model {
    public Firm2() {
        System.out.println("Firm2");
        modelParameters.setWidth(248);
        modelParameters.setHeight(178);
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
        this.grid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());

        // Create grid
//        for (int row = 0; row < modelParameters.getHeight(); row++) {
//            for (int col = 0; col < modelParameters.getWidth(); col++) {
//
//            }
//        }
        int id = getNewId();
        this.grid.setCell(294191255, 378471609, new Road(id));
        if (modelParameters.isVisualise()) {
            visualisation = new Visualisation(this);
        }
        tick();
    }

    public void readRoad() {
        try {
            Scanner sc = new Scanner(new File("../data/roads.txt"));
//            while (sc.hasNext()) {
//                String line = sc.nextLine();
//            }
            String[] tokens = sc.nextLine().split(" ");
            System.out.println(tokens);
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
