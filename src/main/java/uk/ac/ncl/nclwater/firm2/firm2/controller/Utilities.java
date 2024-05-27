package uk.ac.ncl.nclwater.firm2.firm2.controller;

import uk.ac.ncl.nclwater.firm2.firm2.model.PointFloat;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

public class Utilities {

    private static final String APPLICATION_DIRECTORY = System.getProperty("user.home");
    private static final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";

    /**
     * Create a new properties file and set default properties
     */
     public static Properties createPropertiesFile() {
        Properties properties = new Properties();
        File propertiesFile = new File(PROPERTIES_FILEPATH);
        try {
            if (!Files.exists(Paths.get(PROPERTIES_FILEPATH))) {
                OutputStream output = new FileOutputStream(propertiesFile);
                properties.setProperty("toroidal","false");
                properties.setProperty("ticks","30");
                properties.setProperty("visualise","TRUE");
                properties.setProperty("cell-size","3");
                properties.setProperty("chance","50");
                properties.setProperty("application-title","FIRM2");
                properties.setProperty("input-data", "/data/inputs/");
                properties.setProperty("output-data", "/data/outputs/");
                properties.setProperty("terrain-data", "terrain.txt");
                properties.setProperty("roads-data", "roads.txt");
                properties.setProperty("buildings-data", "buildings.txt");
                properties.setProperty("defences-data", "defences.txt");
                properties.setProperty("model-parameters", "globals.json");
                properties.setProperty("ocean-depth", "4");
                properties.store(output, null);
                System.out.println("File " + propertiesFile.getAbsolutePath() + " created");
            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Load properties from the properties file
     */
    public static Properties loadPropertiesFile(String properties_filepath) {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(properties_filepath)) {
            // load a properties file
            properties.load(input);
            System.out.println("Properties read from " + properties_filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    /**
     * Load properties from the properties file
     */
    public static Properties loadPropertiesFile() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILEPATH)) {
            // load a properties file
            properties.load(input);
            System.out.println("Properties read from " + PROPERTIES_FILEPATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }



    /** Linear interpolation to draw a line between two given points in the grid
     *
     * @param x1 first x co-ordinate
     * @param y1 first y co-ordinate
     * @param x2 second x co-ordinate
     * @param y2 second y co-ordinate
     * @return an array of points that needs to be plotted to draw a line
     */
    public static ArrayList<Point> interpolate(int x1, int y1, int x2, int y2) {
        ArrayList<Point> points = new ArrayList<>();

        int startX = Math.min(x1, x2);
        int startY = Math.min(y1, y2);

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int delta = Math.max(dx, dy);

        float stepX = (float)dx / (float)delta;
        float stepY = (float)dy / (float)delta;

        for (int i = 0; i < delta; i++) {
            int x = (int)((float)startX + ((float)i * stepX));
            int y = (int)((float)startY + ((float)i * stepY));
            points.add(new Point(x, y));
        }

        return points;
    }

    /**
     * Turn an ordinance survey co-ordinate into a grid co-ordinate
     * @param x_origin the ordinance survey x co-ordinate of the bottom left corner of the map
     * @param y_origin the ordinance survey y co-ordinate of the bottom left corner of the map
     * @param x the x-coordinate to be converted
     * @param y the y-coordinate to be converted
     * @param cellMeters the size, in square meters of one cell
     * @return return the grid co-ordinates as a Point
     */
    public static Point Ordinance2GridXY(float x_origin, float y_origin, float x, float y, int cellMeters) {
        int x_coord = Math.round((x - x_origin) / cellMeters);
        int y_coord = Math.round((y - y_origin) / cellMeters);
        Point point = new Point(x_coord, y_coord);
        return point;
    }



    /**
     * Helper method to rim brackets of a string
     * @param str The string to be trimmed
     * @return The trimmed string
     */
    public static String trimBrackets(String str) {
        str = str.trim().strip();
        str = (str.indexOf(';') == -1)?str:str.substring(0, str.indexOf(";")).trim().strip();
        if (str.charAt(0) == '[')
            str = str.substring(1);
        if (str.charAt(str.length() - 1) == ']')
            str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * Helper method to trim quotes of a string
     * @param str The string to be trimmed
     * @return The trimmed string
     */
    public static String trimQuotes(String str) {
        str = str.trim().strip();
        str = (str.indexOf(';') == -1)?str:str.substring(0, str.indexOf(";")).trim().strip();
        if (str.charAt(0) == '"')
            str = str.substring(1);
        if (str.charAt(str.length() - 1) == '"')
            str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * Normalise a range of values to a range on the grey scale
     */
    public static int normalise(float value, float min, float max, int low, int high) {
        // colour range
        return 255 - (int)(low + ( ((value - min) * high / (max - min))));
    }
}
