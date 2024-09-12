package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoadTypes {

    @Expose
    @SerializedName("road-types")
    ArrayList<RoadType> roadTypes = new ArrayList();

    public RoadTypes() {
        roadTypes.add(new RoadType("dc", "Dual Carriageway", 60));
        roadTypes.add(new RoadType("sc", "Single Carriageway", 30));
        roadTypes.add(new RoadType("tilj", "Traffic Island Link At Junction", 30));
        roadTypes.add(new RoadType("etal", "Enclosed Traffic Area Link", 30));
        roadTypes.add(new RoadType("sr", "Slip Road", 30));
        roadTypes.add(new RoadType("r", "Roundabout", 30));
        roadTypes.add(new RoadType("til", "Traffic Island Link", 30));
    }

    public ArrayList<RoadType> getRoadTypes() {
        return roadTypes;
    }

    public void setRoadTypes(ArrayList<RoadType> roadTypes) {
        this.roadTypes = roadTypes;
    }
}
