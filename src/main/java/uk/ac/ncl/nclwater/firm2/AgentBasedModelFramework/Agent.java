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

import java.awt.*;

/**
 * An abstract class that needs to be inherited by all agents
 */
public abstract class Agent {

    /**
     * A unique id for each agent that comes into existence
     */
    public String agent_id = "0";

    /**
     * The age of this agent in ticks
     */
    public int tickAge = 0;
    /**
     * A colour for use if model is visualised
     */
    public Color colour = new Color(0);


    /**
     * Each instance of Agent should have a unique ID. Use this method to set the id
     *
     * @param agent_id
     */
    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    /**
     * Method to get the agent's id
     *
     * @return the agent_id
     */
    public String getAgent_id() {
        return this.agent_id;
    }

    /**
     * To keep track of the age of the agent increment 'tickAge' on every tick
     */
    public void incTickAge() {
        tickAge++;
    }

    /**
     * Get the tick age of the agent
     * @return
     */
    public int getTickAge() {
        return tickAge;
    }

    /**
     * Get the colour of the agent for visualisation purposes
     * @return The colour as class Color
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Set the colour of the agent for visualisation purposes
     * @param colour
     */
    public void setColour(Color colour) {
        this.colour = colour;
    }

    public String toString() {
        return "Agent Id: " + agent_id + "\n";
    }
}
