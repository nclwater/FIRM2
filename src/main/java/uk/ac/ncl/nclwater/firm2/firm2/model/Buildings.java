package uk.ac.ncl.nclwater.firm2.firm2.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * FIRM 2 data schema - building
 *
 */
public class Buildings {

    @SerializedName("buildings")
    @Expose
    private List<Buildings> buildings;
    @SerializedName("required")
    @Expose
    private Object required;

    public List<Buildings> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Buildings> buildings) {
        this.buildings = buildings;
    }

    public Object getRequired() {
        return required;
    }

    public void setRequired(Buildings required) {
        this.required = required;
    }

}