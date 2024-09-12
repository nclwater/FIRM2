package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RoadTypesTest {

    RoadTypes rt = new RoadTypes();

    @Test
    void getRoadTypes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String outfile = "roadTypes.json";
        try {
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(rt, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}