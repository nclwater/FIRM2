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

/**
 * An agent of type building
 */
public class Bldng extends Agent {

    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("ordinate")
    @Expose
    private PointDouble ordinate;

    /**
     * An agent of type Building
     * @param id a unique id for the agent
     * @param type the type of building
     */
    public Bldng(int id, int type) {
        this.type = type;

    }


    /**
     * An agent of type Building
     * @param id a unique id for the agent
     * @param type the type of building
     * @param ordinate the ordinance map co-ordinates of the building
     */
    public Bldng(int id, int type, PointDouble ordinate) {
        this.type = type;
        this.ordinate = ordinate;

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
}
