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
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import java.awt.*;

public class Defence extends Agent {

    @SerializedName("ordinate")
    @Expose
    PointDouble ordinate;
    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("height")
    @Expose
    float height;

    public Defence(String agent_id) {
        setAgent_id(agent_id);
        setColour(new Color(0x99, 0x00, 0x00));
    }

    public Defence(String agent_id, PointDouble ordinate, String defenceName, float height) {
        setAgent_id(agent_id);
        setOrdinate(ordinate);
        setName(defenceName);
        setHeight(height);
        setColour(new Color(0x99, 0x00, 0x00));
    }

    public PointDouble getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(PointDouble ordinate) {
        this.ordinate = ordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.ordinate).append("\n");
        sb.append("Name: ").append(this.name).append("\n");
        sb.append("Height: ").append(this.height).append("\n");
        sb.append("Colour: ").append(getColour().toString()).append("\n");
        return sb.toString();
    }

}
