/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ModelStateChanges {

    private static final Logger logger = LoggerFactory.getLogger(ModelStateChanges.class);

    @SerializedName("model-states")
    @Expose
    private List<ModelState> modelStates = new ArrayList<ModelState>();

    public List<ModelState> getModelStates() {
        return modelStates;
    }

    public void setModelStates(List<ModelState> modelStates) {
        this.modelStates = modelStates;
    }

    /**
     * Read the json file containing the timeline for the model. The timeline is an arraylist stored in the
     * ModeStateChanges class. Each item in the array is an event at a specific time and is stored in a ModelState
     * class
     *
     * @return the timeline as a list (ModelStateChanges) of ModelStates
     */
    public static ModelStateChanges readTimeLine(Properties properties) {
        ModelStateChanges modelStateChanges;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // FQN used here because Path is redefined by the graphstream package
            java.nio.file.Path path = Paths.get(properties.getProperty("INPUT_DATA"), properties.getProperty("TIMELINE"));
            String filename = path.toString();
            modelStateChanges = gson.fromJson(new FileReader(filename), ModelStateChanges.class);
            logger.debug("Reading: {}", filename);
            String json = gson.toJson(modelStateChanges, ModelStateChanges.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return modelStateChanges;
    }

    public boolean insertModelState(ModelState modelState) {
        boolean ret = modelStates.add(modelState);

        return ret;
    }


    @Override
    public String toString() {
        return "ModelStatesChanges{" +
                "modelStates=" + modelStates +
                '}';
    }

}