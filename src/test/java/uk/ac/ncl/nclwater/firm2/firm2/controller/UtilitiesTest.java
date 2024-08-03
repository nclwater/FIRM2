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

}