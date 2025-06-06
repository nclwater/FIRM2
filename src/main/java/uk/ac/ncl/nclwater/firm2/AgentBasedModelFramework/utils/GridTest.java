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


package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;

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
        SimpleGrid grid = new SimpleGrid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[0].x, nx);
        assertEquals(points[0].y, ny);
    }

    @org.junit.jupiter.api.Test
    void getVNNeighborhoodSouth() {
        SimpleGrid grid = new SimpleGrid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[1].x, sx);
        assertEquals(points[1].y, sy);
    }

    @org.junit.jupiter.api.Test
    void getVNNeighborhoodEast() {
        SimpleGrid grid = new SimpleGrid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[2].x, ex);
        assertEquals(points[2].y, ey);
    }

    @org.junit.jupiter.api.Test
    void getVNNeighborhoodWest() {
        SimpleGrid grid = new SimpleGrid(10, 10, false, "test");
        Point[] points = grid.getVNNeighborhood(x, y);
        assertEquals(points[3].x, wx);
        assertEquals(points[3].y, wy);
    }
}