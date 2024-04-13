package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
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
import static uk.ac.ncl.nclwater.firm2.model.Utilities.trimQuotes;

/**
 * Class for converting old txt format files to json
 */
public class Txt2Json {
    private static Properties properties;

    /**
     * Read original buildings.txt file and convert it to json as buildings.json
     */
    public static void BuildingsTxt2Json() {
        Buildings buildings = new Buildings();
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("buildings-data")));
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                // skip lines that start with ;;
                if (!line.startsWith(";;") && !line.trim().equals("") && !(line == null)) {
                    // trim any brackets of line
                    line = trimBrackets(line.trim());
                    // tokenise read line
                    String[] xy = (line).split(" ");
                    // extract co-ordinates which are the first two tokens
                    PointDouble coords = new  PointDouble(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                    // extract third token
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

    public static void DefencesTxt2Json() {
        Defences defences = new Defences();
        String defenceNameHolder = "";
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("defences-data")));
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                // skip all lines that start with ;;
                if (!line.startsWith(";;") && !line.trim().equals("") && !(line == null)) {
                    System.out.println("Line: " + line);
                    // trim all brackets of read line
                    line = trimBrackets(line.trim().strip());
                    // tokenise line
                    String[] tokens = (line).split("\t");
                    // extract first two tokens as co-ordinates
                    PointDouble coords = new  PointDouble(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                    if (tokens.length == 3) {
                        // extract third token as name of defence
                        String defenceName = trimQuotes(tokens[2].trim());
                        defenceNameHolder = defenceName;
                    }
                    System.out.println(defenceNameHolder);
                    Defence defence = new Defence(Model.getNewId(), coords, defenceNameHolder);
                    //System.out.println(defence.getOrdinate().getX() + ", " + defence.getOrdinate().getY());
                    defences.add(defence);
                }
            }

            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("input-data") + properties.get("defences-data")).replace(".txt", ".json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(defences, new FileWriter( outfile ));
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        final String APPLICATION_DIRECTORY = System.getProperty("user.home");
        final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";
        properties = Utilities.loadPropertiesFile(PROPERTIES_FILEPATH);
        System.out.println(PROPERTIES_FILEPATH);
        DefencesTxt2Json();
    }
}
