package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.ModelState;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

public class ModelStateChanges {

    private static final Logger logger = LoggerFactory.getLogger(ModelStateChanges.class);

    @SerializedName("model-states")
    @Expose
    private List<ModelState> modelStates;

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
            String filename = properties.getProperty("INPUT_DATA") + properties.getProperty("TIMELINE");
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