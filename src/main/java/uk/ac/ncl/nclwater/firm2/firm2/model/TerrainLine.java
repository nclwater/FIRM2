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

/**
 * This class represents one line of elevations on a map
 */
public class TerrainLine {
    @Expose
    @SerializedName("elevations")
    protected Float[] elevation; // Elevation in metres

    /**
     * Constructor taking an array of elevations as floats
     * @param elevation
     */
    public TerrainLine(Float[] elevation) {
        this.elevation = elevation;
    }

    /**
     * Constructor taking an array of elevations as strings and converts the strings to floats
     * @param str_elevation
     */
    public TerrainLine(String[] str_elevation) {
        this.elevation = new Float[str_elevation.length];
        for (int i = 0; i < str_elevation.length; i++) {
            if (str_elevation[i].equals("-9999"))
                this.elevation[i] = -9999F;
            else
                this.elevation[i] = Float.parseFloat(str_elevation[i]);
        }
    }

    public Float[] getElevation() {
        return elevation;
    }

    public void setElevation(Float[] elevation) {
        this.elevation = elevation;
    }
}
