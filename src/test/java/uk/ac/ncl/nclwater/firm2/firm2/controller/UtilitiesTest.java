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


package uk.ac.ncl.nclwater.firm2.firm2.controller;

import org.junit.jupiter.api.Test;
import uk.ac.ncl.nclwater.firm2.firm2.model.PointInteger;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void convertMphToMpsTest() {
        assertEquals(13.4112, Utilities.convertMphToMps(30));
    }

    @Test
    void BNGToLatLonTest() {
        assertEquals(53.292353995052224, Utilities.BNGToLatLon(294191.255, 378471.609)[0]);
        assertEquals(-3.5875918387056784, Utilities.BNGToLatLon(294191.255, 378471.609)[1]);
    }

    @Test
    void distanceTravelledTest() {
        // Test case 1: speed = 30 km/h
        double speed = 30.0;
        double expected = 13.4112; // This is the expected distance in meters
        double actual = Utilities.distanceTravelled(speed);
        assertEquals(expected, actual, 1e-9, "Distance travelled at 30 mph should be approximately 8.33 meters");
        // Test case 2: speed = 0 km/h (edge case)
        speed = 0.0;
        expected = 0.0; // Expect no distance traveled if speed is 0
        actual = Utilities.distanceTravelled(speed);
        assertEquals(expected, actual, "Distance travelled at 0 km/h should be 0 meters");
        // Test case 3: speed = 120 km/h (higher speed)
        speed = 120.0;
        expected = 53.6448; // Expected distance for 120 km/h
        actual = Utilities.distanceTravelled(speed);
        assertEquals(expected, actual, 1e-9, "Distance travelled at 120mph should be approximately 33.33 meters");
    }

    //298978.0, 378571.0, 0
    @Test
    void BNG2GridXYTest() {
        int cellMeters = 50;
        float x_origin = 292485.78f;
        float y_origin = 374690.12f;
        float x = 298978.0f;
        float y = 378571.0f;

        PointInteger xy = Utilities.BNG2GridXY(x_origin, y_origin, x, y, cellMeters);
        assertEquals(130, xy.getX());
        assertEquals(78, xy.getY());

        x = 294013.382f;
        y = 378051.155f;
        xy = Utilities.BNG2GridXY(x_origin, y_origin, x, y, cellMeters);
        assertEquals(31, xy.getX());
        assertEquals(67, xy.getY());
    }

    @Test
    void timeToUnixTimestamp() {
        long timestamp = 1704067200;
        long unixTimestamp = Utilities.timeToUnixTimestamp(timestamp, 0, 1, 0);
        assertEquals(1704067260000L, unixTimestamp);
    }

    @Test
    void timeStringToUnixTimestamp() {
        String time = "01:00:00";
        long timestamp = 0;
        long unixTimestamp = Utilities.timeStringToUnixTimestamp(timestamp, time);
        assertEquals(3600000L, unixTimestamp);
    }

    @Test
    void unixTimetoModelTime() {
        long unixTimeStamp = 3600; // in seconds
        assertEquals("01:00:00", Utilities.unixTimetoModelTime(unixTimeStamp));
    }

}