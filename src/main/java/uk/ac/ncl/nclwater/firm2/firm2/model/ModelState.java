package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the state change for a given point in time. It should contain a state change for each grid
 * of the model. If the state change for a specif grid is null then there was no change at that point in time.
 */
public class ModelState {
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("sea-level")
    @Expose
    private Float seaLevel;
    @SerializedName("vehicles")
    @Expose
    private ArrayList<Vehicle> vehicles;
    @SerializedName("defence-breach")
    @Expose
    private List<String> defenceBreach;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Float seaLevel) {
        this.seaLevel = seaLevel;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<VehicleCode> vehicleCodes) {
        this.vehicles = vehicles;
    }

    public List<String> getDefenceBreach() {
        return defenceBreach;
    }

    public void setDefenceBreach(List<String> defenceBreach) {
        this.defenceBreach = defenceBreach;
    }

    @Override
    public String toString() {
        StringBuilder vsb = new StringBuilder();
        if (vehicles != null) {
            for (int i = 0; i < vehicles.size(); i++) {
                Vehicle vehicle = vehicles.get(i);
                vsb.append(vehicle.getCode()).append(vehicle.getDist()).append(vehicle.getSd()).append(vehicle.getQty());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("class ModelState {\n");
        sb.append("  time: ").append(time).append("\n");
        sb.append("  seaLevel: ").append(seaLevel).append("\n");
        sb.append("  vehicles: ").append(vsb.toString()).append("\n");
        sb.append("  defenceBreach: ").append(defenceBreach).append("\n");
        sb.append("}\n");

        return sb.toString();
    }
}