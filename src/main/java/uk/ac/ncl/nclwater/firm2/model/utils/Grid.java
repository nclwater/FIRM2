package uk.ac.ncl.nclwater.firm2.model.utils;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Grid {

    Agent[][] grid;

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
     * Generates a grid of cells of specified with and height
     * @param width The width of the grid
     * @param height The height of the grid
     * @param toroidal If toroidal is true the edges of the grid wraps around to the beginning
     * @param name A name for the grid as a string
     */
    public Grid(int width, int height, boolean toroidal, String name) {
        this.width = width;
        this.height = height;
        this.is_toroidal = toroidal;
        grid = new Agent[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = null;
            }
        }
    }

    private Agent getAgentOffsetClamp(int x, int y) {
        return getAgentOffsetClamp(x, y, null);
    }

    private Agent getAgentOffsetClamp(int x, int y, Agent clampTo) {
        return (x < 0 || x >= width || y < 0 || y >= height) ? clampTo : grid[x][y];
    }

    private Agent getAgentOffsetMirror(int x, int y) {
        return grid[Math.min(Math.max(x, 0), width)][Math.min(Math.max(y, 0), height)];
    }
    private Agent getAgentOffsetWrap(int x, int y) {
        return grid[Math.floorMod(x, width)][Math.floorMod(y, height)];
    }

    /**
     *
     * @param direction 'f' for forward 'b' for backward
     * @param x x-coordinate of cell
     * @param y y-coordinate of cell
     * @param t Distance is calculated between the current cell and the first cell of type t
     * @return the distance between the two cells
     */
    public int distanceBetween(char direction, int x, int y, Class<?> t) {
        int distance = 0;
        int currentPos = (x + 1) % getWidth();
        if (getCell(x, y) != null) {
            while ((getCell(currentPos, y)) == null || (getCell(currentPos, y).getClass() != t)) {
                currentPos = (currentPos + 1) % getWidth();
                distance++;
            }
        }
        return distance;
    }

    /**
     * Returns the number of neighbourhood cells occupied by the class type specified. The algorithm for calculating
     * the number of neighbours depend on the neighbourhood_type specified
     * @param neighbourhood_type Moore (m) neighbourhood includes 8 surrounding squares, Von Neumann neighbourhood (n)
     *                           includes only up, down, left and right
     * @param x x-coordinate of cell
     * @param y y-coordinate of cell
     * @return the number of neighbours based on the type requested
     */
    public int occupiedNeighbourCount(char neighbourhood_type, int x, int y, Class<?> t) {
        int neighbours = 0;

        if (neighbourhood_type == 'm') {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    if (!(dx == 0 && dy == 0) &&
                            (is_toroidal ? getAgentOffsetWrap(x + dx, y + dy) != null : getAgentOffsetClamp(x + dx, y + dy) != null)) {
                        neighbours++;
                    }
                }
            }
        }

        return neighbours;
    }

    /**
     * Return the xy co-oridnates for the von Neuman Neigbourhood cells.
     * @param x
     * @param y
     * @return von Neumann neighbourhood as north, south, east, west
     */
    public Point[] getVNNeighborhood(int x, int y) {
        Point[] neighborhood = new Point[4];
        // north
        if (y + 1 < height) {
            neighborhood[0] = new Point(x, y + 1);
        } else if (is_toroidal) {
            neighborhood[0] = new Point(x, 0);
        } else
            neighborhood[0] = new Point(x, y);
        //south
        if (y - 1 >= 0) {
            neighborhood[1] = new Point(x, y - 1);
        } else if (is_toroidal) {
            neighborhood[1] = new Point(x, height);
        } else
            neighborhood[1] = new Point(x, y);
        //east
        if (x + 1 < width) {
            neighborhood[2] = new Point(x + 1, y);
        } else if (is_toroidal) {
            neighborhood[2] = new Point(0, y);
        } else
            neighborhood[2] = new Point(x, y);
        //west
        if (x - 1 >= 0) {
            neighborhood[3] = new Point(x - 1, y);
        } else if (is_toroidal) {
            neighborhood[3] = new Point(width, y);
        } else
            neighborhood[3] = new Point(x, y);
        return neighborhood;
    }

    public void setCell(int x, int y, Agent agent) {
        grid[x][y] = agent;
    }

    public Agent getCell(int x, int y) {
        return grid[x][y];
    }

    public int countAgents(Class<?> t) {
        int agents = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[col][row] != null && grid[col][row].getClass() == t) {
                    agents++;
                }
            }
        }
        return agents;
    }

    public Agent[][] getGrid() {
        return grid;
    }

    public void setGrid(Agent[][] grid) {
        this.grid = grid;
    }

    public void createPNG(String path, String timestamp) {

        int width = getWidth();
        int height = getHeight();

        // Create a BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Set the color of each pixel
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                image.setRGB(col, row, getCell(col, row).getColour().getRGB());
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
     * Thanks to https://www.baeldung.com/java-rgb-color-representation
     * Convert Color to an int
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    int rgbToInt(int alpha, int red, int green, int blue) {
        alpha = clamp(alpha, 0, 255);
        red = clamp(red, 0, 255);
        green = clamp(green, 0, 255);
        blue = clamp(blue, 0, 255);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Thanks to https://www.baeldung.com/java-rgb-color-representation
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
