package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaterLevelState implements iModelState{

    @SerializedName("time")
    @Expose
    String time;

    @SerializedName("water-level")
    @Expose
    int waterLevel;

    public WaterLevelState(String time, int waterLevel) {
        this.time = time;
        this.waterLevel = waterLevel;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public void setTime(String time) {
        this.time = time;
    }
}
