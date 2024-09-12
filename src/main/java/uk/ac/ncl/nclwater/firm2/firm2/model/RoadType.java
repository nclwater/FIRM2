package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoadType {

    @Expose
    @SerializedName("road-type")
    private String roadType;
    @Expose
    @SerializedName("road-type-description")
    private String roadTypeDescription;
    @Expose
    @SerializedName("speed-limit")
    int speedLimit;

    public RoadType(String roadType, String roadTypeDescription, int speedLimit) {
        this.roadType = roadType;
        this.roadTypeDescription = roadTypeDescription;
        this.speedLimit = speedLimit;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public String getRoadTypeDescription() {
        return roadTypeDescription;
    }

    public void setRoadTypeDescription(String roadTypeDescription) {
        this.roadTypeDescription = roadTypeDescription;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }
}
