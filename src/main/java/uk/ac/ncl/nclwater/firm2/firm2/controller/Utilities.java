package uk.ac.ncl.nclwater.firm2.firm2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.PointInteger;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;

public class Utilities {

    private static final String APPLICATION_DIRECTORY = System.getProperty("user.home");
    private static final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "/.firm2.properties";
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

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
                properties.setProperty("input-data", "data/inputs/");
                properties.setProperty("output-data", "data/outputs/");
                properties.setProperty("terrain-data", "terrain.json");
                properties.setProperty("roads-data", "roads.json");
                properties.setProperty("buildings-data", "buildings.json");
                properties.setProperty("defences-data", "defences.json");
                properties.setProperty("model-parameters", "globals.json");
                properties.setProperty("vehicles-data", "vehicles.json");
                properties.setProperty("ocean-depth", "4");
                properties.setProperty("slowdown", "0");
                properties.setProperty("time-stamp", "1719874800");
                properties.setProperty("tick-time-value", "60");
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
            logger.debug("Properties read from " + properties_filepath);
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
            points.addFirst(new Point(x, y));
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
     * @return return the grid co-ordinates as a Point Integer
     */
    public static PointInteger Ordinance2GridXY(float x_origin, float y_origin, float x, float y, int cellMeters) {
        int x_coord = Math.round((x - x_origin) / cellMeters);
        int y_coord = Math.round((y - y_origin) / cellMeters);
        PointInteger point = new PointInteger(x_coord, y_coord);
        return point;
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
    public static Point BNG2GridXY(float x_origin, float y_origin, float x, float y, int cellMeters) {
        int x_coord = Math.round((x - x_origin) / cellMeters);
        int y_coord = Math.round((y - y_origin) / cellMeters);
        Point point = new Point(x_coord, y_coord);
        return point;
    }

    /**
     * Helper method to trim brackets of a string
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

    /**
     * Converts a date string to a Unix timestamp
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long dateStringToUnixTimestamp(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * Converts a Unix timestamp to a date string
     *
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String unixTimestampToDateString(long timestamp, String pattern) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * Calculate the distance between two pairs of British National Grid co-ordinates
     * @param easting1 - first easting co-ordinate
     * @param northing1 - first northing co-ordinate
     * @param easting2 - second easting co-ordinate
     * @param northing2 - second northing co-ordinate
     * @return the difference between two pairs of co-ordinates
     */
    public static double calculateDistance(double easting1, double northing1, double easting2, double northing2) {
        double dE = easting2 - easting1;
        double dN = northing2 - northing1;
        return Math.sqrt(dE * dE + dN * dN);
    }

    /**
     * Convert British National Grid co-ordinates to longitude and latitude
     * @param easting - easting co-ordinate
     * @param northing - northing co-ordinate
     * @return the longitude and latitude as an array[2] of double
     */
    public static double[] BNGToLatLon(double easting, double northing) {
        // Define constants for the transformation
        final double a = 6377563.396;  // Airy 1830 major semi-axis
        final double b = 6356256.910;  // Airy 1830 minor semi-axis
        final double F0 = 0.9996012717;  // scale factor on the central meridian
        final double lat0 = 49 * Math.PI / 180;  // Latitude of true origin (radians)
        final double lon0 = -2 * Math.PI / 180;  // Longitude of true origin and central meridian (radians)
        final double N0 = -100000;  // Northing of true origin (m)
        final double E0 = 400000;  // Easting of true origin (m)
        final double e2 = 1 - (b * b) / (a * a);  // eccentricity squared
        final double n = (a - b) / (a + b);
        final double n2 = n * n;
        final double n3 = n * n * n;

        // Initial calculations
        double lat = lat0;
        double M = 0;

        while (Math.abs(northing - N0 - M) >= 0.00001) {  // Accuracy of < 0.01mm
            lat = (northing - N0 - M) / (a * F0) + lat;
            M = b * F0 * (
                    (1 + n + (5.0 / 4.0) * n2 + (5.0 / 4.0) * n3) * (lat - lat0)
                            - (3 * n + 3 * n2 + (21.0 / 8.0) * n3) * Math.sin(lat - lat0) * Math.cos(lat + lat0)
                            + ((15.0 / 8.0) * n2 + (15.0 / 8.0) * n3) * Math.sin(2 * (lat - lat0)) * Math.cos(2 * (lat + lat0))
                            - (35.0 / 24.0) * n3 * Math.sin(3 * (lat - lat0)) * Math.cos(3 * (lat + lat0))
            );
        }

        // Calculate longitude
        double cosLat = Math.cos(lat);
        double sinLat = Math.sin(lat);
        double nu = a * F0 / Math.sqrt(1 - e2 * sinLat * sinLat);
        double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * sinLat * sinLat, 1.5);
        double eta2 = nu / rho - 1;

        double tanLat = Math.tan(lat);
        double tan2lat = tanLat * tanLat;
        double tan4lat = tan2lat * tan2lat;
        double tan6lat = tan4lat * tan2lat;
        double secLat = 1 / cosLat;
        double nu3 = nu * nu * nu;
        double nu5 = nu3 * nu * nu;
        double nu7 = nu5 * nu * nu;

        double VII = tanLat / (2 * rho * nu);
        double VIII = tanLat / (24 * rho * nu3) * (5 + 3 * tan2lat + eta2 - 9 * eta2 * tan2lat);
        double IX = tanLat / (720 * rho * nu5) * (61 + 90 * tan2lat + 45 * tan4lat);
        double X = secLat / nu;
        double XI = secLat / (6 * nu3) * (nu / rho + 2 * tan2lat);
        double XII = secLat / (120 * nu5) * (5 + 28 * tan2lat + 24 * tan4lat);
        double XIIA = secLat / (5040 * nu7) * (61 + 662 * tan2lat + 1320 * tan4lat + 720 * tan6lat);

        double dE = easting - E0;
        double dE2 = dE * dE;
        double dE3 = dE2 * dE;
        double dE4 = dE3 * dE;
        double dE5 = dE4 * dE;
        double dE6 = dE5 * dE;
        double dE7 = dE6 * dE;

        lat = lat - VII * dE2 + VIII * dE4 - IX * dE6;
        double lon = lon0 + X * dE - XI * dE3 + XII * dE5 - XIIA * dE7;

        lat = lat * 180 / Math.PI;
        lon = lon * 180 / Math.PI;

        return new double[]{lat, lon};
    }

    /**
     * Converts speed from miles per hour to meters per second.
     *
     * @param mph Speed in miles per hour
     * @return Speed in meters per second
     */
    public static double convertMphToMps(double mph) {
        // 1 mile per hour is equal to 0.44704 meters per second
        final double conversionFactor = 0.44704;
        return mph * conversionFactor;
    }

    /**
     * Calculate the distance travelled in one second given a speed
     * @param speed The speed at which the object is moving
     * @return The distance travelled in one second
     */
    public static double distanceTravelled(double speed) {
        return 1000.0 / 3600.0 * speed;
    }

}
