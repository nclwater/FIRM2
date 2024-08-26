package uk.ac.ncl.nclwater.firm2.firm2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Model;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import uk.ac.ncl.nclwater.firm2.firm2.model.BNGRoads;
import uk.ac.ncl.nclwater.firm2.firm2.model.Origins;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

/**
 * Class for converting old txt format files to json
 */
public class Txt2Json {
    private static Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(Txt2Json.class);


    /**
     * The global variable which are currently stored in the first six lines
     * of the terrain.txt file
     */
    public static void Globals2Json() {
        GlobalVariables globalVariables;
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") + properties.getProperty("terrain-data")));
            String[] lines = new String[6];
            for (int i = 0; i < 5; i++) {
                lines[i] = Utilities.trimBrackets(sc.nextLine().trim()).trim().split("\t")[1];
            }
            globalVariables = new GlobalVariables(Integer.parseInt(lines[0]),
                    Integer.parseInt(lines[1]),
                    Float.parseFloat(lines[2]),
                    Float.parseFloat(lines[3]),
                    Integer.parseInt(lines[4]),
                    0, 0);
            logger.debug(globalVariables.asString());
            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("INPUT_DATA") + "globals.json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(globalVariables, fileWriter);
            logger.debug(gson.toJson(globalVariables));
            fileWriter.close();

        } catch (FileNotFoundException e) {
            logger.debug(e.getMessage());
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
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") + "preprocessed-buildings.txt"));
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                // skip lines that start with ;;
                if (!line.startsWith(";;") && !line.trim().equals("") && !(line == null)) {
                    // trim any brackets of line
                    line = trimBrackets(line.trim());
                    // tokenise read line
                    String[] xy = (line).split(" ");
                    // extract co-ordinates which are the first two tokens
                    PointDouble coords = new PointDouble(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                    // extract third token
                    int type = Integer.parseInt(xy[2]);
                    // extract the fourth token
                    String nearestRoad_ID = trimQuotes(xy[3]);
                    Building building = new Building(Model.getNewId(), type, coords, nearestRoad_ID);
                    System.out.println(building.getOrdinate().getX() + ", " + building.getOrdinate().getY() + ", " + nearestRoad_ID);
                    buildings.add(building);
                }
            }
            logger.debug(properties.getProperty("INPUT_DATA") + properties.getProperty("buildings-data"));

            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("INPUT_DATA") + properties.get("buildings-data")).replace(".txt", ".json");
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
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") +  "defences.txt"));
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                // skip all lines that start with ;;
                if (!line.startsWith(";;") && !line.trim().isEmpty()) {
                    // trim all brackets of read line
                    line = trimBrackets(line.trim().strip());
                    // tokenise line
                    String[] tokens = (line).split("\t");
                    System.out.println(line);
                    // extract first two tokens as co-ordinates
                    PointDouble coords = new PointDouble(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                    if (tokens.length == 3) {
                        // extract third token as name of defence
                        String defenceName = trimQuotes(tokens[2].trim());
                        defenceNameHolder = defenceName;
                    }
                    // use a default defence height of 10m

                    Defence defence = new Defence(Model.getNewId(), coords, defenceNameHolder, 10);
                    //System.out.println(defence.getOrdinate().getX() + ", " + defence.getOrdinate().getY());
                    defences.add(defence);
                }
            }

            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = (properties.getProperty("INPUT_DATA") + properties.get("defences-data")).replace(".txt", ".json");
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
     *
     * @param toBNG If true, export to BNG co-ordinates. If false, export to matrix x,y
     */
    public static void RoadsTxt2Json(boolean toBNG) {
        Roads roads = new Roads();
        BNGRoads bngRoads = new BNGRoads();
        try {
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") + "original_textfile_data/roads.txt"));
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
                    String[] tokens = trimQuotes(topHalf.replace("\" \"", " ")).split(" ");
                    long roadLength = Long.parseLong(tokens[3]);
                    String type = trimQuotes(tokens[4]);
                    type = trimQuotes(topHalf.trim().substring(topHalf.substring(0, topHalf.trim().length() - 1).lastIndexOf('"'),
                            topHalf.trim().lastIndexOf('"')));
                    String match = "] \\[";
                    String[] coordinates = trimBrackets(bottomHalf).split(match);
                    if (toBNG) {
                        ArrayList<PointDouble> roadPoints = new ArrayList<>();
                        for (String coord : coordinates) {
                            String[] xy = (coord).split(" ");
                            PointDouble coords = new PointDouble(Double.parseDouble(xy[0]) / 1000,
                                    Double.parseDouble(xy[1]) / 1000);
                            roadPoints.add(coords);
                        }
                        BNGRoad bngRoad = new BNGRoad(roadLength, type, roadPoints, roadIDs);
                        bngRoads.add(bngRoad);
                    } else {
                        ArrayList<PointInteger> roadPoints = new ArrayList<>();
                        for (String coordinate : coordinates) {
                            String[] xy = (coordinate).split(" ");
                            PointInteger coords = Utilities.Ordinance2GridXY(origins.getX_origin(), origins.getY_origin(),
                                    Float.parseFloat(xy[0]) / 1000, Float.parseFloat(xy[1]) / 1000, origins.getCellMeters());
                            coords.setY((origins.getModelHeight() - 1 - coords.getY())); // flip horizontally
                            roadPoints.add(coords);
                        }
                        Road road = new Road(roadLength, type, roadPoints, roadIDs);
                        //System.out.println(defence.getOrdinate().getX() + ", " + defence.getOrdinate().getY());
                        roads.add(road);
                    }
                }
            }
            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile;
            outfile = (properties.getProperty("INPUT_DATA") + "BNG_Roads.json");
            logger.debug("Write output file {}", properties.getProperty("INPUT_DATA")+ properties.getProperty("ROADS_DATA"));

            FileWriter fileWriter = new FileWriter(outfile);
            if ((toBNG)) {
                gson.toJson(bngRoads, fileWriter);
            } else {
                gson.toJson(roads, fileWriter);
            }
            fileWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the road length by entering the road ID
     */
    public static void distances() {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String filename = properties.getProperty("INPUT_DATA") + "sample_roads.json";
        try {
            BNGRoads roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            HashMap<String, BNGRoad> hsh_roads = BNGlistToMap(roads.getRoads());
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            try {
                System.out.print("Enter road ID: " );
                String input = reader.readLine();
                BNGRoad bngRoad = hsh_roads.get(input);
                ArrayList<PointDouble> coords = bngRoad.getPolylineCoordinates();
                double totalDistance = 0;
                for (int i = 1; i < coords.size(); i++) {
                    totalDistance += calculateDistance(coords.get(i).getX(), coords.get(i).getY(),
                            coords.get(i - 1).getX(), coords.get(i - 1).getY());
                    System.out.println("Distance: " + calculateDistance(coords.get(i).getX(), coords.get(i).getY(),
                            coords.get(i - 1).getX(), coords.get(i - 1).getY()));
                }
                System.out.println("Total distance: " + totalDistance + "m : " + (bngRoad.getRoadLength() / 1000d) + "m");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Print a list of roads (the three IDs)
     */
    public static void listRoads() {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String filename = properties.getProperty("INPUT_DATA") + "BNG_roads.json";
        try {
            BNGRoads roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            roads.getRoads().forEach(bngroad -> {
                System.out.println(bngroad.getRoadIDs()[0] + " " + bngroad.getRoadIDs()[1] + " " + bngroad.getRoadIDs()[2]);
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a network of nodes and connections with the roads
     */
    public static void roadNetwork() {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        RoadNetwork roadNetwork = new RoadNetwork();

        String filename = properties.getProperty("INPUT_DATA") + "BNG_roads.json";
        try {
            BNGRoads roads = gson.fromJson(new FileReader(filename), BNGRoads.class);
            roads.getRoads().forEach(bngroad -> {
                roadNetwork.addNode(bngroad.getRoadIDs()[1]);
                roadNetwork.addNode(bngroad.getRoadIDs()[2]);
                Connection connection = new Connection(bngroad.getRoadIDs()[0],
                        bngroad.getRoadIDs()[1],
                        bngroad.getRoadIDs()[2],
                        bngroad.getRoadLength());
                roadNetwork.addConnection(connection);

            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, BNGRoad> BNGlistToMap(List<BNGRoad> list) {
        HashMap<String, BNGRoad> hashMap = new HashMap();
        list.forEach(r -> hashMap.put(r.getID(), r));
        return hashMap;
    }

    /**
     * REMEMBER: There is an error on line 54 - it did not have a type - I added "other"
     */
    public static void CodesTxt2Json() {
        try {
            Scanner sc = new Scanner(new File("DATA/inputs/codes.txt"));
            BuildingTypes buildingTypes = new BuildingTypes();
            // Read first 6 lines for building-type-codes
            int lineIndex = 1;
            while (lineIndex < 7) {
                String[] tokens = trimBrackets(sc.nextLine()).split("\t");
                BuildingType buildingType = new BuildingType(Integer.parseInt(tokens[0]), trimQuotes(tokens[1]));
                buildingTypes.add(buildingType);
                lineIndex++;
            }
            Gson gson1 = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile1 = ("DATA/inputs/buildingtypes.json");
            FileWriter fileWriter1 = new FileWriter(outfile1);
            gson1.toJson(buildingTypes, fileWriter1);
            fileWriter1.close();

            BuildingCodes buildingCodes = new BuildingCodes();
            while (sc.hasNext()) {
                String line = sc.nextLine();
                if (!(line.isEmpty() || line.startsWith(";;"))) {
                    System.out.println(line);
                    String[] tokens = trimBrackets(line).split("\t");
                    BuildingCode buildingCode = new BuildingCode(Integer.parseInt(tokens[0]), trimQuotes(tokens[1]),
                            buildingTypes.findBuildingType(trimQuotes(tokens[2])));
                    buildingCodes.add(buildingCode);
                }
            }
            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = ("DATA/inputs/codes.json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(buildingCodes, fileWriter);
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void TerrainTxt2Json() {
        try {
            Scanner sc = new Scanner(new File("DATA/inputs/terrain.txt"));
            System.out.println("Read file: " + properties.getProperty("terrain-data"));
            // Read and ignore the first 7 lines
            for (int i = 0; i < 7; i++) {
                sc.nextLine();
            }
            TerrainLayer terrainLayer = new TerrainLayer();
            float minHeight = 0F;
            float maxHeight = 0F;
            while (sc.hasNext()) {
                TerrainLine terrainLine = new TerrainLine(trimBrackets(sc.nextLine()).split("\t"));
                terrainLayer.add(terrainLine);
                float min = Collections.min(Arrays.asList(terrainLine.getElevation()));
                float max = Collections.max(Arrays.asList(terrainLine.getElevation()));
                minHeight = Math.min(minHeight, min);
                maxHeight = Math.max(maxHeight, max);
            }
            sc.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile = ("DATA/inputs/terrain.json");
            FileWriter fileWriter = new FileWriter(outfile);
            gson.toJson(terrainLayer, fileWriter);
            fileWriter.close();
            System.out.println("minHeight: " + minHeight + " maxHeight: " + maxHeight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void BusinessTypes2Json() {
        try {
            EnumBusinessType enumBusinessTypes[] = EnumBusinessType.values();
            ArrayList<BusinessType> bt = new ArrayList<>();
            for (EnumBusinessType enumBusinessType : enumBusinessTypes) {
                bt.add(new BusinessType(enumBusinessType.getCode(), enumBusinessType.getDescription()));
            }
            BusinessTypes businessTypes = new BusinessTypes(bt);
            Gson gson1 = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String outfile1 = (properties.getProperty("INPUT_DATA") + "businesstypes.json");
            FileWriter fileWriter1 = null;
            fileWriter1 = new FileWriter(outfile1);
            gson1.toJson(businessTypes, fileWriter1);
            fileWriter1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create an example timeline json file
     */
    public static void MakeATimeLine() {
        ModelStateChanges modelStateChanges = new ModelStateChanges();
        ModelState modelState = new ModelState();
        modelState.setTime("07:45");
        modelState.setSeaLevel(6f);
        modelStateChanges.getModelStates().add(modelState);
        modelState = new ModelState();
        modelState.setTime("08:45");
        modelState.setSeaLevel(4f);
        modelStateChanges.getModelStates().add(modelState);

        Gson gson1 = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String outfile1 = (properties.getProperty("INPUT_DATA") + "timeline.json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outfile1);
            gson1.toJson(modelStateChanges, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        final String APPLICATION_DIRECTORY = System.getProperty("user.home");
        final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";
        properties = Utilities.createPropertiesFile();
        String input = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        while (!input.equals("x")) {
            // Reading data using readLinef
            System.out.println("0. BNG Roads\t1. Roads\t2. Codes\t3. Defences\t4. Buildings\t5. Globals\t6. Terrain\n" +
                    "7. Business Types\t8. Example Timeline\t10. Distances\t11. List roads\t12. Create Road network\n" +
                    "x. Exit");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch (input) {
                case "0":
                    logger.debug("bng roads");
                    RoadsTxt2Json(true);
                    break;
                case "1":
                    RoadsTxt2Json(false);
                    break;
                case "2":
                    CodesTxt2Json();
                    break;
                case "3":
                    DefencesTxt2Json();
                    break;
                case "4":
                    BuildingsTxt2Json();
                    break;
                case "5":
                    Globals2Json();
                    break;
                case "6":
                    TerrainTxt2Json();
                    break;
                case "7":
                    BusinessTypes2Json();
                    break;
                case "8":
                    MakeATimeLine();
                    break;
                case "10":
                    distances();
                    break;
                case "11":
                    listRoads();
                    break;
                case "12":
                    roadNetwork();
                    break;
            }
        }
    }
}

