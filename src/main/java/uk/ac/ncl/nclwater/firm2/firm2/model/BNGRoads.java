package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A road defined using British Network Grid co-ordinates
 */
public class BNGRoads {

    @SerializedName("roads")
    @Expose
    private List<BNGRoad> roads = new ArrayList<>();

    public List<BNGRoad> getRoads() {
        return roads;
    }

    public void setRoads(List<BNGRoad> roads) {
        this.roads = roads;
    }

    public void add(BNGRoad road) {
        roads.add(road);
    }


}
