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


package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import uk.ac.ncl.nclwater.firm2.firm2.model.PointDouble;

import java.util.ArrayList;

public abstract class ComplexAgent extends Agent {
    /**
     * An index into the movement array - thus where this agent currently resides
     */
    int movementIndex = 0;
    /**
     * An ArrayList of pre-determined positions eg. a road.
     */
    ArrayList<PointDouble> routeNodes;

    /**
     * A value to indicate whether movement is forward (1) or in reverse (-1)
     */
    boolean directionForward = true;

    public ComplexAgent() {

    }

    public int getMovementIndex() {
        return movementIndex;
    }

    public void setMovementIndex(int movementIndex) {
        this.movementIndex = movementIndex;
    }

    public void incrementMovementIndex() {
        movementIndex = (movementIndex < routeNodes.size() - 1?movementIndex+1:movementIndex);
    }

    public ArrayList<PointDouble> getRouteNodes() {
        return routeNodes;
    }

    public void setRouteNodes(ArrayList<PointDouble> routeNodes) {
        this.routeNodes = routeNodes;
    }

    public boolean isDirectionForward() {
        return directionForward;
    }

    public void setDirectionForward(boolean directionForward) {
        this.directionForward = directionForward;
    }
}
