package uk.ac.ncl.nclwater.firm2.firm2.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void convertMphToMps() {
        assertEquals(13.4112, Utilities.convertMphToMps(30));
    }

    @Test
    void BNGToLatLon() {
        assertEquals(53.292353995052224, Utilities.BNGToLatLon(294191.255, 378471.609)[0]);
        assertEquals(-3.5875918387056784, Utilities.BNGToLatLon(294191.255, 378471.609)[1]);
    }

    @Test
    void distanceTravelled() {
        // Test case 1: speed = 30 km/h
        double speed = 30.0;
        double expected = 8.333333333333334; // This is the expected distance in meters
        double actual = Utilities.distanceTravelled(speed);
        assertEquals(expected, actual, 1e-9, "Distance travelled at 30 km/h should be approximately 8.33 meters");
        // Test case 2: speed = 0 km/h (edge case)
        speed = 0.0;
        expected = 0.0; // Expect no distance traveled if speed is 0
        actual = Utilities.distanceTravelled(speed);
        assertEquals(expected, actual, "Distance travelled at 0 km/h should be 0 meters");
        // Test case 3: speed = 120 km/h (higher speed)
        speed = 120.0;
        expected = 33.333333333333336; // Expected distance for 120 km/h
        actual = Utilities.distanceTravelled(speed);
        assertEquals(expected, actual, 1e-9, "Distance travelled at 120 km/h should be approximately 33.33 meters");
    }
}