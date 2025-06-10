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
    private Long tickTimeValue = 1L; // the length of time each tick represents in seconds
    private Long timestamp = 0L;

    private boolean runOnStartUp;

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

    public boolean isRunOnStartUp() {
        return runOnStartUp;
    }

    public void setRunOnStartUp(boolean runOnStartUp) {
        this.runOnStartUp = runOnStartUp;
    }
}
