package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    Car car = new Car( new PointDouble(299088.3125, 379406.3125), new PointDouble(299088.3125, 379406.3125));

    @Test
    void getJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(car, Car.class);
        assertEquals("{\n" +
                "  \"startcoordinates\": {\n" +
                "    \"x\": 299088.3125,\n" +
                "    \"y\": 379406.3125\n" +
                "  },\n" +
                "  \"endcoordinates\": {\n" +
                "    \"x\": 299088.3125,\n" +
                "    \"y\": 379406.3125\n" +
                "  }\n" +
                "}", json);
    }
}