package uk.ac.ncl.nclwater.firm2.firm2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Grid;
import uk.ac.ncl.nclwater.firm2.firm2.model.Defence;
import uk.ac.ncl.nclwater.firm2.firm2.model.Defences;
import uk.ac.ncl.nclwater.firm2.firm2.model.FloodModelParameters;
import uk.ac.ncl.nclwater.firm2.firm2.model.GlobalVariables;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import static uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils.AgentIDProducer.getNewId;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.Ordinance2GridXY;

public class LoadDefencesGrid {

    private static final Logger logger = LoggerFactory.getLogger(LoadDefencesGrid.class);

    /**
     * Read the defences.json file and populate the defences grid
     */
    public static Grid loadDefences(GlobalVariables globalVariables, FloodModelParameters floodModelParameters,
                                    Properties properties) {
        Grid defenceGrid = new Grid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                floodModelParameters.isToroidal(), "defences");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String filename = properties.getProperty("input-data") + properties.getProperty("defences-data");
            Defences defences = gson.fromJson(new FileReader(filename), Defences.class);
            logger.debug("Reading: {}", filename);
            defences.getDefences().forEach(d -> {
                Point coords = Ordinance2GridXY(globalVariables.getLowerLeftX(), globalVariables.getLowerLeftY(),
                        (float) d.getOrdinate().getX(), (float) d.getOrdinate().getY(), globalVariables.getCellSize());
                coords.y = floodModelParameters.getHeight() - 1 - coords.y; // flip horizontally
                if (coords.x > 0 && coords.x < floodModelParameters.getWidth() && coords.y > 0 && coords.y < floodModelParameters.getHeight()) {
                    Defence defence = new Defence(getNewId(), d.getOrdinate(), d.getName(), d.getHeight());
                    defenceGrid.setCell(coords.x, coords.y, defence);
                } else {
                    logger.trace("Building: " + coords.x + ", " + coords.y + " is out of bounds");
                }
            });

            return defenceGrid;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
