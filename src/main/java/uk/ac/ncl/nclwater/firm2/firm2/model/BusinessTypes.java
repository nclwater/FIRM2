package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BusinessTypes {

    @Expose
    @SerializedName("business-types")
    ArrayList<BusinessType> businessTypes = new ArrayList<>();

    public BusinessTypes(ArrayList<BusinessType> businessTypes) {
        this.businessTypes = businessTypes;
    }

    public ArrayList<BusinessType> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(ArrayList<BusinessType> businessTypes) {
        this.businessTypes = businessTypes;
    }
}
