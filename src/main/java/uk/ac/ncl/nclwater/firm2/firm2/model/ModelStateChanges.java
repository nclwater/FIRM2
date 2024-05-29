package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelStateChanges {
    @SerializedName("model-states")
    @Expose
    private List<ModelState> modelStates = new ArrayList<>();

    /**
     * Add a state change
     * @param modelState
     * @return
     */
    public boolean add(ModelState modelState) {
        return modelStates.add(modelState);
    }

    public ModelState get(int index) {
        if (index < modelStates.size()) {
            return modelStates.get(index);
        }
        return null;
    }
}
