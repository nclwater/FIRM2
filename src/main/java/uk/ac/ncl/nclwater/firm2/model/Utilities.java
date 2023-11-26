package uk.ac.ncl.nclwater.firm2.model;

import java.awt.*;
import java.util.ArrayList;

public class Utilities {

    /** Linear interpolation to draw a line between two given points in the grid
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
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
     * Helper method to rim quotes of a string
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
