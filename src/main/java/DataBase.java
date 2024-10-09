import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.trimBrackets;

public class DataBase {

    private static Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);
    private static String url = "jdbc:sqlite:DATA/database.db";
    private static String sql_INSERT = null;

    public static void InsertBuildings() {
        try {
            properties = Utilities.createPropertiesFile();
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") + "/original_textfile_data/preprocessed-buildings.txt"));
            sql_INSERT =  "INSERT INTO buildings (class_id, x_coordinate, y_coordinate, nearest_node_code) VALUES (?,?,?,?)";
            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql_INSERT)) {
                while (sc.hasNext()) {
                    String line = sc.nextLine().trim();
                    // skip lines that start with ;;
                    if (!line.startsWith(";;") && !line.trim().isEmpty()) {
                        // trim any brackets of line
                        line = trimBrackets(line.trim());
                        // tokenise read line
                        String[] xy = (line).split(" ");
                        // extract co-ordinates which are the first two tokens
                        PointDouble coords = new PointDouble(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                        pstmt.setInt(1, Integer.parseInt(xy[2]));
                        pstmt.setFloat(2, Float.parseFloat(xy[0]));
                        pstmt.setFloat(3, Float.parseFloat(xy[1]));
                        pstmt.setString(4, Utilities.trimQuotes(xy[3]));
                        pstmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done");
    }

    public static void InsertClassification() {
        Hashtable<String, Integer> lookup = new Hashtable<>();
        lookup.put("residence", 0);
        lookup.put("shop", -2);
        lookup.put("work", -3);
        lookup.put("other", -4);
        lookup.put("recreation", -5);
        lookup.put("warehouse", -6);
        lookup.put("education", -7);
        lookup.put("evacuation", -8);
        try {
            properties = Utilities.createPropertiesFile();
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") + "/original_textfile_data/codes.txt"));
            sql_INSERT = "INSERT INTO classification VALUES(?, ?, ?)";
            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql_INSERT)) {
                while (sc.hasNext()) {
                    String line = sc.nextLine().trim();
                    // skip lines that start with ;;
                    if (!line.startsWith(";;") && !line.trim().isEmpty()) {
                        // trim any brackets of line
                        line = trimBrackets(line.trim());
                        // tokenise read line
                        String class_id = line.substring(0, line.indexOf(' '));
                        line = line.substring(line.indexOf('"') + 1);
                        String description = line.substring(0, line.indexOf('"'));
                        line = line.substring(line.indexOf('"')+3, line.lastIndexOf('"'));
                        pstmt.setInt(1, Integer.parseInt(class_id));
                        pstmt.setString(2, Utilities.trimQuotes(description));
                        System.out.println(description + " " + lookup.get(line));
                        pstmt.setInt(3, lookup.get(line));
                        pstmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done");
    }


    public static void main(String[] args) {
//        InsertBuildings();
        InsertClassification();
    }

}
