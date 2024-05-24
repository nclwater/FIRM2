package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

/**
 * Global parameters for a model
 */
public class ModelParameters {
    private int width = 30;
    private int height = 30;
    private int cell_size = 1;
    private boolean toroidal = false;
    private int ticks = 0;
    private Grid grid;
    private int chance = 70;
    private boolean visualise = false;
    private String title = "Agent Based Model";
    private int slowdown = 200;

    public ModelParameters() {
        grid = new Grid(width, height, toroidal, "");
    }
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCell_size() {
        return cell_size;
    }

    public void setCell_size(int cell_size) {
        this.cell_size = cell_size;
    }

    public boolean isToroidal() {
        return toroidal;
    }

    public void setToroidal(boolean toroidal) {
        this.toroidal = toroidal;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public boolean isVisualise() {
        return visualise;
    }

    public void setVisualise(boolean visualise) {
        this.visualise = visualise;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public int getSlowdown() {
        return slowdown;
    }

    public void setSlowdown(int slowdown) {
        this.slowdown = slowdown;
    }

}
