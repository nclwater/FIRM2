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

/**
 * The first layer. Each cell in the grid will have at its base a Terrain agent.
 */
public class Terrain extends Agent {
    @SerializedName("elevation")
    @Expose
    protected Float elevation; // Elevation in metres
    protected int nodata = -9999;

    //    protected Agent surfaceAgent = null;

    public Terrain(String id, float elevation) {
        this.agent_id = id;
        if (elevation == nodata) {
            colour = new Color(255, 255, 255);
        } else {
            colour = new Color(65, 62, 62);
            this.elevation = elevation;
        }
    }

    public Float getElevation() {
        return elevation;
    }

    public void setElevation(Float elevation) {
        this.elevation = elevation;
    }

    @Override
    public String getAgent_id() {
        return agent_id;
    }

    @Override
    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    @Override
    public Color getColour() {
        return colour;
    }

    @Override
    public void setColour(Color colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Elevation: ").append(elevation).append("\n");
        sb.append("Colour: ").append(getColour().toString()).append("\n");
        return sb.toString();
    }

//    public Agent getSurfaceAgent() {
//        return surfaceAgent;
//    }

//    public void setSurfaceAgent(Agent surfaceAgent) {
//        this.surfaceAgent = surfaceAgent;
//    }
}
