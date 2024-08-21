package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Roads {

    @SerializedName("roads")
    @Expose
    private List<Road> roads = new ArrayList<>();

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public void add(Road road) {
        roads.add(road);
    }

}
