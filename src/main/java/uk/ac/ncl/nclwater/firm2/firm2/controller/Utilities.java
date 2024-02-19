package uk.ac.ncl.nclwater.firm2.firm2.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utilities {

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
}
