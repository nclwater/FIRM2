package uk.ac.ncl.nclwater.firm2.firm2.model;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleCode {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("building-type")
    @Expose
    private int buildingType;
    @SerializedName("nearest-road")
    @Expose
    private String nearestRoad;
    @SerializedName("start-ordinate")
    @Expose
    private PointFloat startOrdinate;
    @SerializedName("end-ordinate")
    @Expose
    private PointFloat endOrdinate;

    public VehicleCode(String code, String description, int buildingType, String nearestRoad, PointFloat startOrdinate, PointFloat endOrdinate) {
        this.code = code;
        this.description = description;
        this.buildingType = buildingType;
        this.nearestRoad = nearestRoad;
        this.startOrdinate = startOrdinate;
        this.endOrdinate = endOrdinate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    public String getNearestRoad() {
        return nearestRoad;
    }

    public void setNearestRoad(String nearestRoad) {
        this.nearestRoad = nearestRoad;
    }

    public PointFloat getStartOrdinate() {
        return startOrdinate;
    }

    public void setStartOrdinate(PointFloat startOrdinate) {
        this.startOrdinate = startOrdinate;
    }

    public PointFloat getEndOrdinate() {
        return endOrdinate;
    }

    public void setEndOrdinate(PointFloat endOrdinate) {
        this.endOrdinate = endOrdinate;
    }
}

