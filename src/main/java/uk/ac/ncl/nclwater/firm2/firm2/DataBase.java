package uk.ac.ncl.nclwater.firm2.firm2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer.getNewId;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.trimBrackets;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.trimQuotes;

public class DataBase {

    private static Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(Txt2Json.class);
    private static String url = "jdbc:sqlite:DATA/database.db";
    private static String sql_INSERT = "INSERT INTO buildings VALUES (?,?,?,?)";
    Connection conn = null;

    public static void InsertBuildings() {
        try {
            properties = Utilities.createPropertiesFile();
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") + "/original_textfile_data/preprocessed-buildings.txt"));

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
                        pstmt.setString(4, xy[3]);
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
    }

    public static void InsertCodes() {
        try {
            properties = Utilities.createPropertiesFile();
            Scanner sc = new Scanner(new File(properties.getProperty("INPUT_DATA") + "/original_textfile_data/codes.txt"));

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
                        pstmt.setString(4, xy[3]);
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
    }


    public static void main(String[] args) {
        InsertBuildings();
    }
}
