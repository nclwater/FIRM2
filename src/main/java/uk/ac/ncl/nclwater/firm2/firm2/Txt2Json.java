package uk.ac.ncl.nclwater.firm2.firm2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import uk.ac.ncl.nclwater.firm2.model.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

/**
 * Class for converting old txt format files to json
 */
public class Txt2Json {
    private static Properties properties;

    /**
     * The global variable which are currently stored in the first six lines
     * of the terrain.txt file
     */
    public static void Globals2Json() {
        GlobalVariables globalVariables;
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("terrain-data")));
            String[] lines = new String[6];
            for (int i = 0; i < 5; i++) {
                lines[i] = Utilities.trimBrackets(sc.nextLine().trim()).trim().split("\t")[1];
            }
            globalVariables = new GlobalVariables(Integer.parseInt(lines[0]),
                    Integer.parseInt(lines[1]),
                    Float.parseFloat(lines[2]),
                    Float.parseFloat(lines[3]),
                    Integer.parseInt(lines[4]));
            System.out.println(globalVariables.asString());
            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("input-data") + "globals.json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(globalVariables, fileWriter);
            System.out.println( gson.toJson(globalVariables));
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
            Writer fileWriter = new FileWriter(outfile);
            gson.toJson(buildings, fileWriter);
            fileWriter.flush();
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Read original defences.txt file and convert it to json as defences.json
     */
    public static void DefencesTxt2Json() {
        Defences defences = new Defences();
        String defenceNameHolder = "";
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("defences-data")));
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                // skip all lines that start with ;;
                if (!line.startsWith(";;") && !line.trim().isEmpty()) {
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
                    Defence defence = new Defence(Model.getNewId(), coords, defenceNameHolder);
                    //System.out.println(defence.getOrdinate().getX() + ", " + defence.getOrdinate().getY());
                    defences.add(defence);
                }
            }

            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("input-data") + properties.get("defences-data")).replace(".txt", ".json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(defences, fileWriter);
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read original roads.txt file and convert it to json as roads.json
     */
    public static void RoadsTxt2Json() {
        Roads roads = new Roads();
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("roads-data")));
            Origins origins = new Origins();
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                // skip all lines that start with ;;
                if (!line.startsWith(";;") && !line.trim().isEmpty()) {
                    // trim all brackets of read line
                    line = trimBrackets(line.trim().strip());

                    int firstBracket = line.indexOf('[');
                    String topHalf = line.trim().substring(0, firstBracket);
                    String bottomHalf = trimBrackets(line.trim().substring(firstBracket));
                    // tokenise line
                    // extract first three tokens as roadIDs
                    String[] roadIDs = new String[3];
                    roadIDs[0] = topHalf.split(" ")[0].replace('"', ' ').trim();
                    roadIDs[1] = topHalf.split(" ")[1].replace('"', ' ').trim();
                    roadIDs[2] = topHalf.split(" ")[2].replace('"', ' ').trim();
                    String[] tokens = trimQuotes(topHalf.replace("\" \""," ")).split(" ");
                    long roadLength = Integer.parseInt(tokens[3]);
                    String type = trimQuotes(tokens[4]);
                    type = trimQuotes(topHalf.trim().substring(topHalf.substring(0,topHalf.trim().length()-1).lastIndexOf('"'),
                            topHalf.trim().lastIndexOf('"')));
                    String match = "] \\[";
                    String[] coordinates = trimBrackets(bottomHalf).split(match);
                    ArrayList<PointDouble> roadPoints = new ArrayList<>();
                    for (String coordinate : coordinates) {
                        String[] xy = (coordinate).split(" ");
                        PointDouble coords = new PointDouble(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
                        coords.setY(origins.getModelHeight() - 1 - coords.getY()); // flip horizontally
                        roadPoints.add(coords);
                    }
                    Road road = new Road(roadLength, type, roadPoints, roadIDs);
                    //System.out.println(defence.getOrdinate().getX() + ", " + defence.getOrdinate().getY());
                    roads.add(road);
                }
            }
            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("input-data") + properties.get("roads-data")).replace(".txt", ".json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(roads, fileWriter);
            fileWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * REMEMBER: There is an error on line 54 - it did not have a type - I added "other"
     */
    public static void CodesTxt2Json() {
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("codes-data")));
            BuildingTypes buildingTypes = new BuildingTypes();
            // Read first 6 lines for building-type-codes
            int lineIndex = 1;
            while (lineIndex < 7) {
                String[] tokens = trimBrackets(sc.nextLine()).split(" ");
                BuildingType buildingType = new BuildingType(Integer.parseInt(tokens[0]), trimQuotes(tokens[1]));
                buildingTypes.add(buildingType);
                lineIndex++;
            }
            System.out.println("Building types: " + buildingTypes.getBuildingTypeList().size());
            Gson gson1 = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile1 = (properties.getProperty("input-data") + "buildingtypes.json");
            FileWriter fileWriter1 = new FileWriter(outfile1);
            gson1.toJson(buildingTypes, new FileWriter( outfile1 ));
            fileWriter1.close();

            BuildingCodes buildingCodes = new BuildingCodes();
            while (sc.hasNext()) {
                String line = sc.nextLine();
                if (!(line.isEmpty() || line.startsWith(";;"))) {
                    System.out.println(line);
                    String[] tokens = trimBrackets(line).split(" ");
                    BuildingCode buildingCode = new BuildingCode(Integer.parseInt(tokens[0]), trimQuotes(tokens[1]),
                            buildingTypes.findBuildingType(trimQuotes(tokens[2])));
                    buildingCodes.add(buildingCode);
                }
            }
            System.out.println("Building Codes: " + buildingCodes.getBuildingCodes().size());
            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("input-data") + properties.get("codes-data")).replace(".txt", ".json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(buildingCodes, fileWriter);
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        final String APPLICATION_DIRECTORY = System.getProperty("user.home");
        final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";
        properties = Utilities.loadPropertiesFile(PROPERTIES_FILEPATH);
        String input = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        while (!input.equals("x")) {
            // Reading data using readLinef
            System.out.println("1. Roads\n2. Codes\n3. Defences\n4. Buildings\n5. Globals\nx. Exit");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch (input) {
                case "1":RoadsTxt2Json();break;
                case "2":CodesTxt2Json();break;
                case "3":DefencesTxt2Json();break;
                case "4":BuildingsTxt2Json();break;
                case "5":Globals2Json();break;
            }
        }
        //
        //CodesTxt2Json();
        //Globals2Json();
    }
}

/**
 * Inner class used for reading origins of map which is required to interpret data from the old
 * format data files.
 */
class Origins {
    private int modelWidth;
    private int modelHeight;
    private float x_origin;
    private float y_origin;
    private int cellMeters;
    private static Properties properties = new Properties();
    private static final String APPLICATION_DIRECTORY = System.getProperty("user.home");
    private static final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";

    public Origins() {
        Scanner sc = null;
        if (!Files.exists(Paths.get(PROPERTIES_FILEPATH))) {
            createPropertiesFile();
        }
        properties = loadPropertiesFile();
        try {
            sc = new Scanner(new File(properties.getProperty("input-data") + properties.getProperty("terrain-data")));
            String line = sc.nextLine();     //model width
            setModelWidth(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();            //model height
            setModelHeight(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            setX_origin(Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            setY_origin(Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            setCellMeters(Integer.parseInt(trimBrackets(line).split("\t")[1]));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public float getX_origin() {
        return x_origin;
    }

    public void setX_origin(float x_origin) {
        this.x_origin = x_origin;
    }

    public float getY_origin() {
        return y_origin;
    }

    public void setY_origin(float y_origin) {
        this.y_origin = y_origin;
    }

    public int getCellMeters() {
        return cellMeters;
    }

    public void setCellMeters(int cellMeters) {
        this.cellMeters = cellMeters;
    }

    public int getModelWidth() {
        return modelWidth;
    }

    public void setModelWidth(int modelWidth) {
        this.modelWidth = modelWidth;
    }

    public int getModelHeight() {
        return modelHeight;
    }

    public void setModelHeight(int modelHeight) {
        this.modelHeight = modelHeight;
    }
}