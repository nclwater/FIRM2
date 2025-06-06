/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


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