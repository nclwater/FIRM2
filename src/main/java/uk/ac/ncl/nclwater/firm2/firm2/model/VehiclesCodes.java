package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This class hold the vehicles types and descriptions of vehicle agents. Each vehicle type has a unique code
 */
public class VehiclesCodes {
    @SerializedName("vehicle-codes")
    @Expose
    ArrayList<VehicleCode> vehicleCodes = new ArrayList<>();

    public ArrayList<VehicleCode> getVehicleCodes() {
        return vehicleCodes;
    }

    public void setVehicleCodes(ArrayList<VehicleCode> vehicleCodes) {
        this.vehicleCodes = vehicleCodes;
    }
}
