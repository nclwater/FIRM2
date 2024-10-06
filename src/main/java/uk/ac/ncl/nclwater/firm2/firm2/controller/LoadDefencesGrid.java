package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer.getNewId;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.Ordinance2GridXY;

public class LoadDefencesGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadBuildingsGrid.class);

    /**
     * Read the defences.json file and populate the defences grid
     */
    public static SimpleGrid loadDefences(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                          Properties properties) {
        SimpleGrid defenceGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                floodModelParameters.isToroidal(), "defences");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("DEFENCES_DATA");
            Defences defences = gson.fromJson(new FileReader(filename), Defences.class);
            logger.info("Reading: {}", filename);
            // Check that building is in bounds before adding
            defences.getDefences().forEach(d -> {
                PointInteger coords = Ordinance2GridXY(globalVariables.getLowerLeftX(), globalVariables.getLowerLeftY(),
                        (float) d.getOrdinate().getX(), (float) d.getOrdinate().getY(), globalVariables.getCellSize());
                coords.setY(floodModelParameters.getHeight() - 1 - coords.getY()); // flip horizontally
                if (coords.getX() > 0 && coords.getX() < floodModelParameters.getWidth() && coords.getY() > 0
                        && coords.getY() < floodModelParameters.getHeight()) {
                    Defence defence = new Defence(Integer.toString(getNewId()), d.getOrdinate(), d.getName(), d.getHeight());
                    defenceGrid.setCell(coords.getX(), coords.getY(), defence);
                }
            });

            return defenceGrid;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
