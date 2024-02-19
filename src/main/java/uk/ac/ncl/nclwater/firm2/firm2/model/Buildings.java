package uk.ac.ncl.nclwater.firm2.firm2.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * FIRM 2 data schema - buildings
 *
 */
public class Buildings {

    @SerializedName("buildings")
    @Expose
    private List<Building> buildings = new ArrayList<>();

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public void add(Building building) {
        buildings.add(building);
    }

}