package uk.ac.ncl.nclwater.firm2.examples.nagelschreckenberg;

import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.model.utils.Grid;

import java.awt.*;
import java.util.Random;

public class NagelSchreckenberg extends Model {
    NagelSchreckenberg() {
        modelParameters.setWidth(200);
        modelParameters.setHeight(1);
        modelParameters.setToroidal(true);
        modelParameters.setTicks(0);
        modelParameters.setVisualise(true);
        modelParameters.setCell_size(5);
        modelParameters.setChance(50);
        modelParameters.setTitle("Nagel-Schreckenberg Traffic Simulation");
        modelParameters.setSlowdown(500);
        modelInit();
    }

    @Override
    public void tick() {
        Grid newGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal(), "cars");
        // Acceleration: All cars not at the maximum velocity have their velocity increased by one unit.
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Car c = (Car)grids.get("cars").getCell(col, row);
                if ((c != null) && (c.getVelocity() < c.getMaxVelocity())) {
                    c.setVelocity(c.getVelocity() + 1);
                }
            }
        }
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Car c = (Car)grids.get("cars").getCell(col, row);
                if (c != null) {
                    int distance = grids.get("cars").distanceBetween('f', col, row, Car.class);
                    if (distance < c.getVelocity()) {
                        c.setVelocity(distance);
                    }
                }
            }
        }
        // Move cars forward the number of cells equal to their velocity
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                if (grids.get("cars").getCell(col, row) != null) {
                    newGrid.setCell((((Car)(grids.get("cars").getCell(col, row))).getVelocity() + col) %
                            grids.get("cars").getWidth(), row, grids.get("cars").getCell(col, row));
                }
            }
        }
        grids.put("cars", newGrid);
//        printGrid('i', Car.class);
    }

    @Override
    public void modelInit() {
        this.grids.put("cars", new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal(), "cars"));

        // Create grid
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Random random = new Random();
                int nextInt = random.nextInt(100);
                int newId = 0;
                if (nextInt < modelParameters.getChance()) {
                    newId = getNewId();
                    if (newId == 1)
                        this.grids.get("cars").setCell(col, row, new Car(newId, Color.RED));
                    else
                        this.grids.get("cars").setCell(col, row, new Car(newId, Color.BLUE));
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
