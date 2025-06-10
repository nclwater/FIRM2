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

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleCode {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("building-type")
    @Expose
    private int buildingType;
    @SerializedName("nearest-road")
    @Expose
    private String nearestRoad;
    @SerializedName("start-ordinate")
    @Expose
    private PointFloat startOrdinate;
    @SerializedName("end-ordinate")
    @Expose
    private PointFloat endOrdinate;

    public VehicleCode(String code, String description, int buildingType, String nearestRoad, PointFloat startOrdinate, PointFloat endOrdinate) {
        this.code = code;
        this.description = description;
        this.buildingType = buildingType;
        this.nearestRoad = nearestRoad;
        this.startOrdinate = startOrdinate;
        this.endOrdinate = endOrdinate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    public String getNearestRoad() {
        return nearestRoad;
    }

    public void setNearestRoad(String nearestRoad) {
        this.nearestRoad = nearestRoad;
    }

    public PointFloat getStartOrdinate() {
        return startOrdinate;
    }

    public void setStartOrdinate(PointFloat startOrdinate) {
        this.startOrdinate = startOrdinate;
    }

    public PointFloat getEndOrdinate() {
        return endOrdinate;
    }

    public void setEndOrdinate(PointFloat endOrdinate) {
        this.endOrdinate = endOrdinate;
    }
}

