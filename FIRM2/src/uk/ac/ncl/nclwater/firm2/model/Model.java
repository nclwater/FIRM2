package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;

/**
 * This abstract class has to be implemented to define the model behaviour. The model should run in its own thread and
 * would require the **run** methode to be implemented
 */
public abstract class Model implements Runnable {
    protected ModelParameters modelParameters = new ModelParameters();
    private static int ids = 0;
    private boolean run = false;
    protected Visualisation visualisation;
    protected Grid grid;
    private boolean running = true;
    private int total_ticks = 0;

    public Model() {
        if (!modelParameters.isVisualise()) setRun(true);
    }

    public abstract void modelInit();

    /**
     * Actions to perform on every tick.
     */
    public abstract void tick();

    /**
     * Take one step in the model, i.e. one tick
     */
    public void step() {
        tick();
        if (modelParameters.isVisualise()) {
            visualisation.repaint();
        }
    }

    /**
     * Because the model runs as a thread, the **run** method has to be defined. Run provides two loops. The outer loop
     * should keep the model 'alive' by keeping the thread going. The inner loop allows the model to be paused without
     * exiting the thread.
     */

    @Override
    public void run() {

        while (running) {
            if (modelParameters.getTicks() > 0 && modelParameters.getTicks() == total_ticks) {
                System.out.println("Model.class: ticks");
                running = false;
                run = false;
            }
            try {
                Thread.sleep(1);
                if (run) {
                    System.out.println("Run");
                    total_ticks++;
                    System.out.println(modelParameters.getTicks() + ", " + total_ticks);
                    tick();
                    if (modelParameters.isVisualise()) {
                        Thread.sleep(modelParameters.getSlowdown());
                        SwingUtilities.invokeLater(visualisation.getRunModel());
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Print the grid to the terminal. A text based visualisation of the grid
     *
     * @param type 'n' for neighbourhoodcounts, 'x' for x's, 'd' for distance and 'i' for ids, where the cell contents is not null
     */
    protected void printGrid(char type, Class<?> t) {
        String text = "o";
        for (int row = 0; row < modelParameters.getHeight(); row++) {
            for (int col = 0; col < modelParameters.getWidth(); col++) {
                switch (type) {
                    case 'n':
                        text = "" + grid.occupiedNeighbourCount('m', col, row, t);
                        break;
                    case 'd':
                        text = "" + grid.distanceBetween('f', col, row, t);
                        break;
                    case 'x':
                        text = "x";
                        break;
                    case 'i':
                        text = grid.getCell(col, row) == null ? " " : "" + grid.getCell(col, row).getAgent_id();
                        break;
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
    protected int getNewId() {
        ids++;
        return ids;
    }

    /**
     * Returns the grid in its current state
     * @return the grid in its current state
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * When visualised, how many pixels high and wide should a cell be?
     *
     * @return the size that a cell should be on the screen in pixels (height = width)
     */
    public int getCell_size() {
        return modelParameters.getCell_size();
    }

    /**
     * Condition to indicate whether the module thread should be running. If you want the model to run when the
     * program is started, make sure to set this to true in main
     *
     * @param run true if running, false is pausing
     */
    public void setRun(boolean run) {
        this.run = run;
    }

    public boolean getRun() {
        return this.run;
    }

    /**
     * Returns a ModelParameters object containing all the parameter values
     * @return ModelParameters
     */
    public ModelParameters getModelParameters() {
        return modelParameters;
    }
}
