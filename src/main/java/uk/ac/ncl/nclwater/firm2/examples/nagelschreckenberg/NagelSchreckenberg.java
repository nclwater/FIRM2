package uk.ac.ncl.nclwater.firm2.examples.nagelschreckenberg;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Model;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Visualisation;

import java.awt.*;
import java.util.Random;

import static uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer.getNewId;

/**
 * https://en.wikipedia.org/wiki/Nagel–Schreckenberg_model
 */
public class NagelSchreckenberg extends Model {
    double nsProbability = 0.3; // Randomness, probability of deceleration
    int numberOfCars = 5; // number of cars to simulate
    int maxVelocity = 3; // the maximum velocity of all cars

    NagelSchreckenberg() {
        modelParameters.setWidth(200); // Width of the simulation grid
        modelParameters.setHeight(1); // Height of the simulation grid
        modelParameters.setToroidal(true); // Is the simulation toroidal
        modelParameters.setTicks(0); // 0 to run infinitely otherwise the number of ticks to run
        modelParameters.setVisualise(true); // to visualise or not
        modelParameters.setCell_size(5); // size of a cell when visualised
        modelParameters.setChance(1);
        modelParameters.setTitle("Nagel-Schreckenberg Traffic Simulation");
        modelParameters.setSlowdown(100); // milliseconds pause between ticks to slow model down for visualisation
        modelInit();
    }

    @Override
    public void tick() {
        SimpleGrid newGrid = new SimpleGrid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal(), "cars");
        // Rule 1, Acceleration: All cars not at the maximum velocity have their velocity increased by one unit.
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Car c = (Car)((SimpleGrid)grids.get("cars")).getCell(col, row);
                if ((c != null) && (c.getVelocity() < c.getMaxVelocity())) {
                    c.setVelocity(c.getVelocity() + 1);
                }
            }
        }
        // Rule 2, Deceleration: All cars are checked to see if the distance between it and the car in front
        // is smaller than its current velocity. If the distance is smaller than the velocity, the velocity is
        // reduced to the number of empty cells in front of the car – to avoid a collision.
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Car c = (Car)((SimpleGrid)grids.get("cars")).getCell(col, row);
                if (c != null) {
                    int distance = ((SimpleGrid)grids.get("cars")).distanceBetween('f', col, row, Car.class);
                    if (distance < c.getVelocity()) {
                        c.setVelocity(distance);
                    }
                }
            }
        }
        // Rule 3, Randomization:  The speed of all cars that have a velocity of at least 1, is now reduced by one
        // unit with a probability of p
        Random random = new Random();
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Car c = (Car)((SimpleGrid)grids.get("cars")).getCell(col, row);
                if (c != null) {
                    int velocity = c.getVelocity();
                    if (velocity >= 1) {
                        if (random.nextDouble() < nsProbability) {
                            c.setVelocity(velocity - 1);
                        }

                    }
                }
            }
        }

        // Rule 4, Vehicle movement: Move cars forward the number of cells equal to their velocity
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                if (((SimpleGrid)grids.get("cars")).getCell(col, row) != null) {
                    newGrid.setCell((((Car)(((SimpleGrid)grids.get("cars")).getCell(col, row))).getVelocity() + col) %
                            ((SimpleGrid)grids.get("cars")).getWidth(), row, ((SimpleGrid)grids.get("cars")).getCell(col, row));
                }
            }
        }
        grids.put("cars", newGrid);
//        printGrid('i', Car.class);
    }

    @Override
    public void modelInit() {
        this.grids.put("cars", new SimpleGrid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal(), "cars"));


        // Create grid
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Random random = new Random();
                int nextInt = random.nextInt(100);
                System.out.println("Next int: " + nextInt);
                if (nextInt < modelParameters.getChance()) {
                    int newId = getNewId();
                    if (newId == 1)
                        ((SimpleGrid)this.grids.get("cars")).setCell(col, row, new Car(Integer.toString(newId), Color.RED));
                    else
                        ((SimpleGrid)this.grids.get("cars")).setCell(col, row, new Car(Integer.toString(newId), Color.BLUE));
                }
            }
        }
        // Should the model be visualised?
        if (modelParameters.isVisualise()) {
            visualisation = new Visualisation(this);
        };
    }

    public static void main(String[] args) {
        NagelSchreckenberg model = new NagelSchreckenberg();
        model.setRun(true);
        Thread modelthread = new Thread(model);
        modelthread.start();
    }
}
