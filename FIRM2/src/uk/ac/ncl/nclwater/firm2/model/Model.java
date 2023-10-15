package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.examples.conway.Alive;
import uk.ac.ncl.nclwater.firm2.utils.Agent;
import uk.ac.ncl.nclwater.firm2.utils.Grid;

public abstract class Model implements Runnable {
    protected ModelParameters modelParameters = new ModelParameters();
    private static int ids = 0;
    private boolean run = false;
    protected Visualisation visualisation;
    protected Grid grid;

    public Model() {
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

    @Override
    public void run() {
        while (true) {
            try {
                if (run) {
                    for (int i = 0; i < modelParameters.getTicks(); i++) {
                        tick();
                        Thread.sleep(5);
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Print the grid to the terminal
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
     * Condition to indicate whether the module thread should be running
     *
     * @param run true if running, false is pausing
     */
    public void setRun(boolean run) {
        System.out.println("Run = " + run);
        this.run = run;
    }

    public Visualisation getVisualisation() {
        return visualisation;
    }

    public void setVisualisation(Visualisation visualisation) {
        this.visualisation = visualisation;
    }

    public ModelParameters getModelParameters() {
        return modelParameters;
    }

}
