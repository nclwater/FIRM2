package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BuildingTypes {
    @SerializedName("building-type-list")
    @Expose
    private List<BuildingType> buildingTypeList = new ArrayList<>();

    public BuildingTypes() {
        this.buildingTypeList = buildingTypeList;
    }

    public List<BuildingType> getBuildingTypeList() {
        return buildingTypeList;
    }

    public void setBuildingTypeList(List<BuildingType> buildingTypeList) {
        this.buildingTypeList = buildingTypeList;
    }

    public void add(BuildingType buildingType) {
        buildingTypeList.add(buildingType);
    }

    /**
     * Find building type by description and return the code
     * -999 means the code was not found
     * @param typeDescription
     * @return the code of the type of building. -999 means it was not found
     */
    public int findBuildingType(String typeDescription) {
        for (BuildingType type:buildingTypeList) {
            if (type.getTypeDescription().equals(typeDescription))
                return type.getTypeCode();
        }
        return -999;
    }
}
