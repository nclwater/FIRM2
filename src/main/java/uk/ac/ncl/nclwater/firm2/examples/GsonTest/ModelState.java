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


package uk.ac.ncl.nclwater.firm2.examples.GsonTest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// ModelState.java
public class ModelState {
    @SerializedName("time")
    private String time;
    @SerializedName("sea-level")
    private Integer seaLevel;
    @SerializedName("vehicles")
    private String vehicles;
    @SerializedName("defence-breach")
    private List<String> defenceBreach = new ArrayList<>();

    // Getters and Setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Integer seaLevel) {
        this.seaLevel = seaLevel;
    }

    public String getVehicles() {
        return vehicles;
    }

    public void setVehicles(String vehicles) {
        this.vehicles = vehicles;
    }

    public List<String> getDefenceBreach() {
        return defenceBreach;
    }

    public void setDefenceBreach(List<String> defenceBreach) {
        this.defenceBreach = defenceBreach;
    }
}

