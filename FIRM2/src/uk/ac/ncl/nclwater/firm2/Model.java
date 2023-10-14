package uk.ac.ncl.nclwater.firm2;

import uk.ac.ncl.nclwater.firm2.utils.Grid;
import java.util.Random;

/**
 *
 */
public class Model implements Runnable {
    private static int width = 100;
    private static int height = 100;
    private int cell_size = 5;
    private static boolean toroidal = false;
    private Random random = new Random();
    private static Grid grid = new Grid(width, height, toroidal);
    private int chance = 70;
    private int ticks = 10000;
    private static int ids = 0;
    private boolean run = false;
    Visualisation visualisation;

    public Model(Visualisation visualisation) {
        this.visualisation = visualisation;
        // Create grid
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int nextInt = random.nextInt(100);
                int newId = 0;
                if (nextInt < chance) {
                    newId = getNewId();
                    grid.setCell(col, row, new Alive(newId));
                }
                System.out.println(nextInt + ", " + newId + "\t");
            }
        }
    }

    public void setVisualisation(Visualisation visualisation) {
        this.visualisation = visualisation;
    }

    public void run() {
        System.out.println("Start run");
        while (true) {
            if (run) {
                tick();
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void step() {
        System.out.println("Step");
        tick();
    }

    /**
     * Actions to perform on every tick.
     */
    public void tick() {
        System.out.println("tick");
        Grid newGrid = new Grid(width, height, toroidal);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int neighbourCount = grid.occupiedNeighbourCount('m', col, row, Alive.class);
                // If cell is alive
                if (grid.getCell(col, row) != null) {
                    // has less than two neighbours, it dies
                    if (neighbourCount < 2) {
                        newGrid.setCell(col, row, null);
                    }
                    // has two or three neighbours keeps on living
                    if (neighbourCount == 2 || neighbourCount == 3) {
                        newGrid.setCell(col, row, grid.getCell(col, row));
                    }
                    // has more than three neighbours dies
                    if (neighbourCount > 3) {
                        newGrid.setCell(col, row, null);
                    }
                    // If cell is dead
                } else {
                    // has exactly three neighbours it becomes alive
                    if (neighbourCount == 3) {
                        newGrid.setCell(col, row, new Alive(getNewId()));
                    }
                }
            }
        }
        grid = newGrid;
        if (this.visualisation != null) {
            this.visualisation.repaint();
        }
//        printGridIds();

    }

    private static void printGridIds() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid.getCell(col, row) != null) {
                    System.out.print(grid.getCell(col, row).getAgent_id() + "\t");
                } else {
                    System.out.print("-" + "\t");
                }
            }
            System.out.println();
        }
    }

    private static void printNeighbourhoodCounts() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(grid.occupiedNeighbourCount('m',col, row, Alive.class) + "\t");
            }
            System.out.println();
        }
    }

    private static int getNewId() {
        ids++;
        return ids;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getCell_size() {
        return cell_size;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

}
