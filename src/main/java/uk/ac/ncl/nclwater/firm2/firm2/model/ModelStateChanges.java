package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.ac.ncl.nclwater.firm2.firm2.model.ModelState;

import java.util.List;

public class ModelStateChanges {
    @SerializedName("model-states")
    @Expose
    private List<ModelState> modelStates;

    public List<ModelState> getModelStates() {
        return modelStates;
    }

    public void setModelStates(List<ModelState> modelStates) {
        this.modelStates = modelStates;
    }

    @Override
    public String toString() {
        return "ModelStatesChanges{" +
                "modelStates=" + modelStates +
                '}';
    }
}