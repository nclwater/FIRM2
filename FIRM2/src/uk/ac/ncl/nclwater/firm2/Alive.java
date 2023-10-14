package uk.ac.ncl.nclwater.firm2;

import uk.ac.ncl.nclwater.firm2.utils.Agent;

import java.awt.*;

public class Alive implements Agent {

    int agent_id = 0;
    int tickAge = 0;

    Color colour = new Color(0);
    public Alive(int agent_id) {
        this.agent_id = agent_id;
    }

    @Override
    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    @Override
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
