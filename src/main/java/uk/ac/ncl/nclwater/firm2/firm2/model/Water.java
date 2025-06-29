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

public class Water extends Agent {
    @Expose
    @SerializedName("water-level")
    float waterLevel = 0;
    @Expose
    @SerializedName("isOcean")
    boolean isOcean = false;

    /**
     * A water type agent. If the agent is part of the ocean set isOcean to true
     * @param agent_id
     * @param waterLevel
     * @param isOcean
     */
    public Water(String agent_id, float waterLevel, boolean isOcean) {
        this.waterLevel = waterLevel;
        this.isOcean = isOcean;
        setAgent_id(agent_id);
        setColour(new Color(0x00, 117, 0x99, (isOcean ? 0xFF : 0x00)));
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = waterLevel;
    }

    @Override
    public void setColour(Color color) {
        super.setColour(color);
    }

    public boolean isOcean() {
        return isOcean;
    }

    public void setOcean(boolean ocean) {
        isOcean = ocean;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("waterLevel: ").append(waterLevel).append("\n");
        sb.append("isOcean: ").append(isOcean).append("\n");
        sb.append("Colour: ").append(getColour().toString()).append("\n");
        return sb.toString();
    }
}
