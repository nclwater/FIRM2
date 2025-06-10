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

/**
 * Override default model parameters to include water level
 * and PNG-on-tick
 */
public class FloodModelParameters extends uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.ModelParameters {
    float oceanDepth = 0;
    boolean pngOnTick = false;
    float vehicleFloodDepth = 0;


    public FloodModelParameters() {}

    public float getOceanDepth() {
        return oceanDepth;
    }

    public void setOceanDepth(float oceanDepth) {
        this.oceanDepth = oceanDepth;
    }

    public boolean isPngOnTick() {
        return pngOnTick;
    }

    public void setPngOnTick(boolean pngOnTick) {
        this.pngOnTick = pngOnTick;
    }

    public float getVehicleFloodDepth() {
        return vehicleFloodDepth;
    }

    public void setVehicleFloodDepth(float vehicleFloodDepth) {
        this.vehicleFloodDepth = vehicleFloodDepth;
    }
}
