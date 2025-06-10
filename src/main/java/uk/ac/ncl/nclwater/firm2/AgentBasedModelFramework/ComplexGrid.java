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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ComplexGrid implements Grid {

    ArrayList<ArrayList<ArrayList<Agent>>> grid;
    private static final Logger logger = LoggerFactory.getLogger(ComplexGrid.class);

    /**
     * If the model is toroidal (i.e. wraps around) set this to true
     */
    boolean is_toroidal = false;

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public boolean isIs_toroidal() {
        return is_toroidal;
    }

    public void setIs_toroidal(boolean is_toroidal) {
        this.is_toroidal = is_toroidal;
    }

    String gridName;

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
    int width, height;


    /**
     * Generates a grid of cells of specified with and height. Each cell can hold multiple agents of different
     * agent types
     * @param width The width of the grid
     * @param height The height of the grid
     * @param toroidal If toroidal is true the edges of the grid wraps around to the beginning
     * @param name A name for the grid as a string
     */
    public ComplexGrid(int width, int height, boolean toroidal, String name) {
        grid = new ArrayList<ArrayList<ArrayList<Agent>>>();
        this.width = width;
        this.height = height;
        this.is_toroidal = toroidal;
        for (int row = 0; row < height; row++) {
            ArrayList newRow = new ArrayList<>();
            grid.add(row, newRow);
            for (int col = 0; col < width; col++) {
                ArrayList<Agent> agents = new ArrayList<>();
                newRow.add(col, agents);
            }
        }
    }

    private int neighborhoodClamp(int value, int max) {
        return value < 0   ? (is_toroidal ? max : 0  ) :
                value > max ? (is_toroidal ? 0   : max) :
                        value;
    }

    /**
     * Return the xy co-oridnates for the von Neuman Neigbourhood cells.
     * @param x x co-ordinate of cell
     * @param y y co-ordinate of cell
     * @return von Neumann neighbourhood as north, south, east, west
     */
    public Point[] getVNNeighborhood(int x, int y) {
        Point[] neighborhood = new Point[4];

        neighborhood[0] = new Point(neighborhoodClamp(x, width - 1), neighborhoodClamp(y - 1, height - 1));
        neighborhood[1] = new Point(neighborhoodClamp(x, width - 1), neighborhoodClamp(y + 1, height - 1));
        neighborhood[2] = new Point(neighborhoodClamp(x - 1, width - 1), neighborhoodClamp(y, height - 1));
        neighborhood[3] = new Point(neighborhoodClamp(x + 1, width - 1), neighborhoodClamp(y, height - 1 ));

        return neighborhood;
    }

    public void setCell(int x, int y, ArrayList<Agent> agents) {
        grid.get(y).set(x, agents);
    }

    public void addCell(int x, int y, Agent agents) {

        if (x > width || x < 0 || y > height || y < 0) {
            logger.error("Error: Out of bounds for {} {}. x={} y={}", width, height, x, y);
        } else {
            if (grid.get(y).get(x) == null) {
                grid.get(y).set(x, new ArrayList<Agent>());
            }
            grid.get(y).get(x).add(agents);
        }
    }

    public ArrayList<Agent> getCells(int x, int y) {
        return grid.get(y).get(x);
    }


    public ArrayList<ArrayList<ArrayList<Agent>>> getGrid() {
        return grid;
    }

    public void setGrid(ArrayList<ArrayList<ArrayList<Agent>>> grid) {
        this.grid = grid;
    }

    /**
     * Create a PNG image from the grid
     * https://github.com/nayuki/PNG-library
     * @param path
     * @param timestamp
     */
    public void createPNG(String path, String timestamp) {

        int width = getWidth();
        int height = getHeight();

        // Create a BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Set the color of each pixel
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //image.setRGB(col, row, getCell(col, row).getColour().getRGB());
            }
        }

        // Write the image to a file
        try {
            ImageIO.write(image, "png", new File(path +  timestamp + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thanks to <a href="https://www.baeldung.com/java-rgb-color-representation">...</a>
     * Convert Color to an int
     * @param alpha alpha value of colour
     * @param red red channel 0 to 255
     * @param green green channel 0 to 255
     * @param blue blue channel 0 to 255
     * @return integer value of colour
     */
    int rgbToInt(int alpha, int red, int green, int blue) {
        alpha = clamp(alpha, 0, 255);
        red = clamp(red, 0, 255);
        green = clamp(green, 0, 255);
        blue = clamp(blue, 0, 255);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Thanks to <a href="https://www.baeldung.com/java-rgb-color-representation">...</a>
     * Helper method for rgbToInt
     * @param value
     * @param min
     * @param max
     * @return
     */
    int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

}
