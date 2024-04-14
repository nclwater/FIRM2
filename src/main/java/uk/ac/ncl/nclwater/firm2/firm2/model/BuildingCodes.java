package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BuildingCodes {
    @SerializedName("building-codes")
    @Expose
    private List<BuildingCode> buildingCodes = new ArrayList<>();

    public List<BuildingCode> getBuildingCodes() {
        return buildingCodes;
    }

    public void setBuildingCodes(List<BuildingCode> buildingCodes) {
        this.buildingCodes = buildingCodes;
    }

    public void add(BuildingCode buildingCode) {
        buildingCodes.add(buildingCode);
    }
}
