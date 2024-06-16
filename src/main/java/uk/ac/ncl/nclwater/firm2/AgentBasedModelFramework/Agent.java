package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import java.awt.*;

/**
 * An abstract class that needs to be inherited by all agents
 */
public abstract class Agent {
    public int agent_id = 0;
    public int tickAge = 0;
    public Color colour = new Color(0);


    /**
     * Each instance of Agent should have a unique ID. Use this method to set the id
     *
     * @param agent_id
     */
    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    /**
     * Method to get the agent's id
     *
     * @return the agent_id
     */
    public int getAgent_id() {
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
