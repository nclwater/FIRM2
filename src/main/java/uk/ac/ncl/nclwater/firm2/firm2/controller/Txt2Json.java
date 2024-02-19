package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.ncl.nclwater.firm2.firm2.model.Bldng;
import uk.ac.ncl.nclwater.firm2.firm2.model.Building;
import uk.ac.ncl.nclwater.firm2.firm2.model.Buildings;
import uk.ac.ncl.nclwater.firm2.firm2.model.PointDouble;
import uk.ac.ncl.nclwater.firm2.model.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.model.Utilities.trimBrackets;

/**
 * Class for converting old txt format files to json
 */
public class Txt2Json {
    private static Properties properties;
    private static Buildings buildings = new Buildings();

    public Txt2Json() {

    }

    public static void main(String[] args) {
        final String APPLICATION_DIRECTORY = System.getProperty("user.home");
        final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";
        properties = Utilities.loadPropertiesFile(PROPERTIES_FILEPATH);
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("buildings-data")));
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                if (!line.startsWith(";;") && !line.trim().equals("") && !(line == null)) {
                    line = trimBrackets(line.trim().strip());
                    String[] xy = (line).split(" ");
                    PointDouble coords = new PointDouble(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                    int type = Integer.parseInt(xy[2]);
                    Building building = new Building(Model.getNewId(), type, coords);
                    System.out.println(building.getOrdinate().getX() + ", " + building.getOrdinate().getY());
                    buildings.add(building);
                }
            }

            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("input-data") + properties.get("buildings-data")).replace(".txt", ".json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(buildings, new FileWriter( outfile ));
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
