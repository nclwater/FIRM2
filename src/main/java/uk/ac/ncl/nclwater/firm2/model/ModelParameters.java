package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

/**
 * Global parameters for a model
 */
public class ModelParameters {
    private int width = 30;
    private int height = 30;
    private int cell_size = 1;
    /**
     * If toroidal is true the model "wraps around borders"
     */
    private boolean toroidal = false;
    private int ticks = 0;
    private int chance = 70;
    private boolean visualise = false;
    private String title = "Agent Based Model";
    private int slowdown = 200;
    private Long tickTimeValue = 60L; // the length of time each tick represents in seconds
    private Long timestamp = 0L;

    public ModelParameters() {
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

    public void setTickTimeValue(Long tickTimeValue) {
        this.tickTimeValue = tickTimeValue;
    }

    public Long getTickTimeValue() {
        return tickTimeValue;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
