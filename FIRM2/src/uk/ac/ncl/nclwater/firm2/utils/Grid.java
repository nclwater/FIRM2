package uk.ac.ncl.nclwater.firm2.utils;

public class Grid {

    Agent[][] grid;
    boolean is_toroidal = false;

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
     */
    public Grid(int width, int height, boolean toroidal) {
        this.width = width;
        this.height = height;
        grid = new Agent[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = null;
            }
        }
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
        for (int row = y - 1; row <= y + 1; row++) {
            for (int col = x - 1; col <= x + 1; col++) {
                if ((col >= 0 && col < width) && (row >= 0 && row < height) && !(row == y && col == x)) {
                        if (grid[col][row] != null) {
                            if (grid[col][row].getClass() == t) {
                                neighbours++;
                            }
                        }

                }
            }
        }
        return neighbours;
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
}
