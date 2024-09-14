package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    Car car = new Car("CarABC", "4000000012472821", "4000000012843295");

    @Test
    void getJSON() {
        assertEquals("4000000012472821", car.getStartNode());
    }
}