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

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import java.awt.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * An agent of type building
 */
public class Building extends Agent {

    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("ordinate")
    @Expose
    private PointDouble ordinate;
    @SerializedName("nearest-road")
    @Expose
    String nearestRoad_ID;

    /**
     * An agent of type Building
     * @param id a unique id for the agent
     * @param type the type of building
     */
    public Building(String id, int type) {
        setAgent_id(id);
        this.type = type;
        if (type == 0) {
            setColour(new Color(170, 170, 170)); // Buildings default to gray
        } else {
            setColour(new Color(0x00, 0xff, 0x00)); // Residential defaults to green
        }
    }


    /**
     * An agent of type Building
     * @param id a unique id for the agent
     * @param type the type of building
     * @param ordinate the ordinance map co-ordinates of the building
     */
    public Building(String id, int type, PointDouble ordinate, String nearestRoad_ID) {
        setAgent_id(id);
        this.type = type;
        this.ordinate = ordinate;

        if (type == 0) {
            setColour(new Color(170, 170, 170)); // Buildings default to gray
        } else {
            setColour(new Color(0x00, 0xff, 0x00)); // Residential defaults to green
        }
        this.nearestRoad_ID = nearestRoad_ID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PointDouble getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(PointDouble ordinate) {
        this.ordinate = ordinate;
    }

    public String getNearestRoad_ID() {
        return nearestRoad_ID;
    }

    public void setNearestRoad_ID(String nearestRoad_ID) {
        this.nearestRoad_ID = nearestRoad_ID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("type: ").append(type).append("\n");
        sb.append("ordinate: ").append(ordinate).append("\n");
        sb.append("nearestRoad_ID: ").append(nearestRoad_ID).append("\n");
        sb.append("Colour:" ).append(getColour().toString()).append("\n");
        return sb.toString();
    }
}
