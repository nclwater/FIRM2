package uk.ac.ncl.nclwater.firm2.firm2.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.trimBrackets;

/**
 * Inner class used for reading origins of map which is required to interpret data from the old
 * format data files.
 */
public class Origins {
    private static final Logger logger = LogManager.getLogger(Origins.class);
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
            String filename = properties.getProperty("INPUT_DATA") + "/original_textfile_data/terrain.txt";
            sc = new Scanner(new File(filename));
            logger.debug("Reading file: " + filename);
            String line = sc.nextLine();     //model width
            logger.debug(line);
            setModelWidth(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();            //model height
            setModelHeight(Integer.parseInt(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            setX_origin(Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            setY_origin(Float.parseFloat(trimBrackets(line).split("\t")[1]));
            line = sc.nextLine();
            logger.debug("CellSize: {} {} {}", line, trimBrackets(line).split("\t")[0], trimBrackets(line).split("\t")[1]);
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