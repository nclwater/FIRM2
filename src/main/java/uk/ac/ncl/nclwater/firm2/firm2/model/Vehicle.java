package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicle {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("dist")
    @Expose
    private String dist;
    @SerializedName("sd")
    @Expose
    private Integer sd;
    @SerializedName("qty")
    @Expose
    private Integer qty;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public Integer getSd() {
        return sd;
    }

    public void setSd(Integer sd) {
        this.sd = sd;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCode()).append("-").append(getDist()).append("-").append(getSd()).append("-").append(getQty()).
                append(getQty());
        return sb.toString();
    }

}