package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefenceBreachState implements iModelState{

    @SerializedName("time")
    @Expose
    String time;

    @SerializedName("defences")
    @Expose
    private List<String> defences;

    public DefenceBreachState(String time, List<String> defences) {
        this.time = time;
        this.defences = defences;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getDefences() {
        return defences;
    }

    public void setDefences(List<String> defences) {
        this.defences = defences;
    }
}
