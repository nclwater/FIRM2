package uk.ac.ncl.nclwater.firm2.examples.conway;

import uk.ac.ncl.nclwater.firm2.model.Model;
import uk.ac.ncl.nclwater.firm2.model.Visualisation;
import uk.ac.ncl.nclwater.firm2.utils.Grid;

import java.util.Random;

/**
 * An implementation of Conway's Game of live to illustrate the use of the FIRM2 agent modelling framework
 */
public class Conway extends Model {

    /**
     * Constructor
     */
    public Conway() {
        modelParameters.setWidth(1000);
        modelParameters.setHeight(1000);
        modelParameters.setToroidal(false);
        modelParameters.setTicks(0);
        modelParameters.setVisualise(true);
        modelParameters.setCell_size(1);
        modelParameters.setChance(50);
        modelParameters.setTitle("Conway's Game of Life");
        modelInit();
    }

    @Override
    public void tick() {
        Grid newGrid = new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal());
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                int neighbourCount = grids.get(0).occupiedNeighbourCount('m', col, row, Alive.class);
                // If cell is alive
                if (grids.get(0).getCell(col, row) != null) {
                    // has two or three neighbours keeps on living
                    if (neighbourCount == 2 || neighbourCount == 3) {
                        newGrid.setCell(col, row, grids.get(0).getCell(col, row));
                    } else {
                        newGrid.setCell(col, row, null);
                    }
                } else { // If cell is dead
                    // has exactly three neighbours it becomes alive
                    if (neighbourCount == 3) {
                        newGrid.setCell(col, row, new Alive(getNewId()));
                    }
                }
            }
        }
        grids.set(0, newGrid);
//        printGrid('x', Alive.class);
    }


    @Override
    public void modelInit() {
        this.grids.add(new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal()));

        // Create grid
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                Random random = new Random();
                int nextInt = random.nextInt(100);
                int newId = 0;
                if (nextInt < modelParameters.getChance()) {
                    newId = getNewId();
                    this.grids.get(0).setCell(col, row, new Alive(newId));
                }
            }
        }
        // Should the model be visualised?
        if (modelParameters.isVisualise()) {
            visualisation = new Visualisation(this);
        }
        //tick();
    }


    public static void main(String[] args) {
        Conway model = new Conway();
        model.setRun(true);
        Thread modelthread = new Thread(model);
        modelthread.start();
    }

}
