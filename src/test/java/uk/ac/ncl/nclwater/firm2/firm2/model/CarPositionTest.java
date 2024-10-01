package uk.ac.ncl.nclwater.firm2.firm2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarPositionTest {
    double x = 301815.0;
    double y = 374822.0;
    double d = 1622.6237105537055;
    CarPosition carPosition = new CarPosition(x, y, d);

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCarPosition() {
        new CarPosition(301815.0, 374822.0, 1622.6237105537055);
        assertEquals(x, carPosition.getX());
    }
}