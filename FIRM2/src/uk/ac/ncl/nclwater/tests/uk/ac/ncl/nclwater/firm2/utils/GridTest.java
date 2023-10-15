package uk.ac.ncl.nclwater.firm2.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ncl.nclwater.firm2.examples.conway.Alive;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    static int width = 10;
    static int height = 10;

    Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid(width, height, false);
        grid.setCell(3, 3, new Alive(1));
        grid.setCell(3, 2, new Alive(2));
        grid.setCell(3, 1, new Alive(3));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void occupiedNeighbourCount() {
        assertEquals(2, grid.occupiedNeighbourCount('m', 3, 2, Alive.class));
        assertEquals(1, grid.occupiedNeighbourCount('m', 3, 3, Alive.class));
        assertEquals(1, grid.occupiedNeighbourCount('m', 3, 1, Alive.class));
    }

    @Test
    void countAgents() {
        assertEquals(3, grid.countAgents(Alive.class));
    }
}