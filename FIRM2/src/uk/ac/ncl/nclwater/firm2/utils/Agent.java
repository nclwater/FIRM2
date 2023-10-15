package uk.ac.ncl.nclwater.firm2.utils;

import java.awt.*;

public abstract class Agent {
    protected int agent_id = 0;
    protected int tickAge = 0;
    protected Color colour = new Color(0);


    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }


    public int getAgent_id() {
        return this.agent_id;
    }

    public void incTickAge() {
        tickAge++;
    }

    public int getTickAge() {
        return tickAge;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }
}
