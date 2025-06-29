/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.LinkedHashMap;

/**
 * This abstract class has to be implemented to define the model behaviour. The model should run in its own thread and
 * would require the **run** methode to be implemented
 */
public abstract class Model implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Model.class);
    protected ModelParameters modelParameters;
    private boolean run = false;
    protected Visualisation visualisation;
    protected LinkedHashMap<String, Grid> grids = new LinkedHashMap<>();
    private boolean running = true;
    private int total_ticks = 0;

    public Model() {
        this(new ModelParameters());
    }

    public Model(ModelParameters parameters) {
        this.modelParameters = parameters;
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
                running = false;
                run = false;
                logger.debug("Model run completed: " + total_ticks);
                if (modelParameters.getTicks() == total_ticks && !modelParameters.isVisualise()) {
                    System.exit(0);
                }
            }
            try {
                Thread.sleep(1);
                if (run) {
                    total_ticks++;
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
    protected void printGrid(char type, Class<?> t, int layer) {
        String text = "o";
        if (grids.get(layer) instanceof SimpleGrid) {
            SimpleGrid g = (SimpleGrid) grids.get(layer);
            for (int row = 0; row < modelParameters.getHeight(); row++) {
                for (int col = 0; col < modelParameters.getWidth(); col++) {
                    switch (type) {
                        case 'n':
                            text = "" + g.occupiedNeighbourCount('m', col, row, t);
                            break;
                        case 'd':
                            text = "" + g.distanceBetween('f', col, row, t);
                            break;
                        case 'x':
                            text = "x";
                            break;
                        case 'i':
                            text = g.getCell(col, row) == null ? " " : "" + g.getCell(col, row).getAgent_id();
                            break;
                    }
                    if (g.getCell(col, row) != null) {
                        System.out.print(text + "\t");
                    } else {
                        System.out.print(text + "\t");
                    }
                }
                System.out.println();
            }
        }
        System.out.println("\033[H\033[2J");
    }

    /**
     * Returns the grid in its current state
     * @return the grid in its current state
     */
    public LinkedHashMap<String, Grid> getGrids() {
        return grids;
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
     * Returns a FloodModelParameters object containing all the parameter values
     * @return FloodModelParameters
     */
    public ModelParameters getModelParameters() {
        return modelParameters;
    }
}
