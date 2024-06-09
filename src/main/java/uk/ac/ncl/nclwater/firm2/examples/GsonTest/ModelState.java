package uk.ac.ncl.nclwater.firm2.examples.GsonTest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// ModelState.java
public class ModelState {
    @SerializedName("time")
    private String time;
    @SerializedName("sea-level")
    private Integer seaLevel;
    @SerializedName("vehicles")
    private String vehicles;
    @SerializedName("defence-breach")
    private List<String> defenceBreach = new ArrayList<>();

    // Getters and Setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Integer seaLevel) {
        this.seaLevel = seaLevel;
    }

    public String getVehicles() {
        return vehicles;
    }

    public void setVehicles(String vehicles) {
        this.vehicles = vehicles;
    }

    public List<String> getDefenceBreach() {
        return defenceBreach;
    }

    public void setDefenceBreach(List<String> defenceBreach) {
        this.defenceBreach = defenceBreach;
    }
}

