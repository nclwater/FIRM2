package uk.ac.ncl.nclwater.firm2.examples.GsonTest;


// Timeline.java
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timeline {
    @SerializedName("model-states")
    private List<ModelState> modelStates;

    // Getters and Setters
    public List<ModelState> getModelStates() {
        return modelStates;
    }

    public void setModelStates(List<ModelState> modelStates) {
        this.modelStates = modelStates;
    }
}