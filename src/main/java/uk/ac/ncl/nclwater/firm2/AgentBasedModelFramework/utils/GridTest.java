package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Grid;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    int x = 9;
    int y = 0;
    int nx = 9;
    int ny = 0;
    int sx = 9;
    int sy = 1;
    int ex = 8;
    int ey = 0;
    int wx = 9;
    int wy = 0;

    @org.junit.jupiter.api.Test
    void getVNNeighborhoodNorth() {
        Grid grid = new Grid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[0].x, nx);
        assertEquals(points[0].y, ny);
    }

    @org.junit.jupiter.api.Test
    void getVNNeighborhoodSouth() {
        Grid grid = new Grid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[1].x, sx);
        assertEquals(points[1].y, sy);
    }

    @org.junit.jupiter.api.Test
    void getVNNeighborhoodEast() {
        Grid grid = new Grid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[2].x, ex);
        assertEquals(points[2].y, ey);
    }

    @org.junit.jupiter.api.Test
    void getVNNeighborhoodWest() {
        Grid grid = new Grid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[3].x, wx);
        assertEquals(points[3].y, wy);
    }
}