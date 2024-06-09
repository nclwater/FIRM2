package uk.ac.ncl.nclwater.firm2.examples.GsonTest;

// TimelineReader.java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.IOException;

public class TimelineReader {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileReader reader = new FileReader("DATA/inputs/timeline.json")) {
            Timeline timeline = gson.fromJson(reader, Timeline.class);
            for (ModelState modelState : timeline.getModelStates()) {
                System.out.println("Time: " + modelState.getTime());
                System.out.println("Sea Level: " + modelState.getSeaLevel());
                System.out.println("Vehicles: " + modelState.getVehicles());
                System.out.println("Defence Breach: " + modelState.getDefenceBreach());
                System.out.println();
            }
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
