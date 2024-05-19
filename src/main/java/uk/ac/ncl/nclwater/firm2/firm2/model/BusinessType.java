package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessTypes {
    @SerializedName("business-type-code")
    @Expose
    int businessTypeCode;
    @SerializedName("business-type")
    @Expose
    String businessType;

    public BusinessTypes(int businessTypeCode, String businessType) {
        this.businessTypeCode = businessTypeCode;
        this.businessType = businessType;
    }

    public int getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(int businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
