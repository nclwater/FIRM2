package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuildingCode {
    @SerializedName("code")
    @Expose
    int code;
    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("building-type-code")
    @Expose
    int buildingTypeCode;

    public BuildingCode(int code, String description, int type) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuildingTypeCode() {
        return buildingTypeCode;
    }

    public void setBuildingTypeCode(int buildingTypeCode) {
        this.buildingTypeCode = buildingTypeCode;
    }
}
