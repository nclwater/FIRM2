package uk.ac.ncl.nclwater.firm2;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;
import java.util.Random;

/**
 *
 */
public class Model implements Runnable {
    private int width;
    private int height;
    private int cell_size;
    private boolean toroidal;
    private int chance;
    private int ticks;
    private boolean visualise;
    private Grid grid;
    private int ids = 0;
    private boolean run = false;
    private Visualisation visualisation;

    public Model(ModelParameters modelParameters) {
        this.width = modelParameters.getWidth();
        this.height = modelParameters.getHeight();
        this.cell_size = modelParameters.getCell_size();
        this.toroidal = modelParameters.isToroidal();
        this.chance = modelParameters.getChance();
        this.ticks = modelParameters.getTicks();
        this.visualise = modelParameters.isVisualise();
        this.grid = new Grid(this.width, this.height, this.toroidal);
        if (this.visualise) {
            visualisation = new Visualisation(this);
        }

        // Create grid
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                Random random = new Random();
                int nextInt = random.nextInt(100);
                int newId = 0;
                if (nextInt < this.chance) {
                    newId = getNewId();
                    this.grid.setCell(col, row, new Alive(newId));
                }
            }
        }
    }

    public void run() {
        System.out.println("Start run");
        while (true) {
            try {
                if (run) {
                    Thread.sleep(5);
                    tick();
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Take one step in the model, i.e. one tick
     */
    public void step() {
        tick();
    }

    /**
     * Actions to perform on every tick.
     */
    public void tick() {
        Grid newGrid = new Grid(width, height, toroidal);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int neighbourCount = grid.occupiedNeighbourCount('m', col, row, Alive.class);
                // If cell is alive
                if (grid.getCell(col, row) != null) {
                    // has two or three neighbours keeps on living
                    if (neighbourCount == 2 || neighbourCount == 3) {
                        newGrid.setCell(col, row, grid.getCell(col, row));
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
        grid = newGrid;
        if (visualise) {
            visualisation.getDrawPanel().repaint();
        }
        //        printGrid('x');
    }

    /**
     * Print the grid to the terminal
     *
     * @param type 'n' for neighbourhoodcounts, 'x' for x's and 'i' for ids, where the cell contents is not null
     */
    private void printGrid(char type) {
        String text = "o";
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                switch (type) {
                    case 'n':
                        text = "" + grid.occupiedNeighbourCount('m', col, row, Alive.class);
                    case 'x':
                        text = "x";
                    case 'i':
                        text = grid.getCell(col, row) == null ? "o" : "" + grid.getCell(col, row).getAgent_id();
                }
                if (grid.getCell(col, row) != null) {
                    System.out.print(text + "\t");
                } else {
                    System.out.print(text + "\t");
                }
            }
            System.out.println();
        }
        System.out.println("\033[H\033[2J");
    }

    /**
     * Increment the id value
     *
     * @return a new id
     */
    private int getNewId() {
        ids++;
        return ids;
    }

    public Grid getGrid() {
        return grid;
    }

    /**
     * When visualised, how many pixels high and wide should a cell be?
     *
     * @return the size that a cell should be on the screen in pixels (height = width)
     */
    public int getCell_size() {
        return cell_size;
    }

    /**
     * Condition to indicate whether the module thread should be running
     *
     * @param run true if running, false is pausing
     */
    public void setRun(boolean run) {
        this.run = run;
    }

    public Visualisation getVisualisation() {
        return visualisation;
    }

    public void setVisualisation(Visualisation visualisation) {
        this.visualisation = visualisation;
    }
}
