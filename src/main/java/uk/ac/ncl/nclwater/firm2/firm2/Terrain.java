package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

/**
 * An agent of type Terrain. Terrain contains a surfaceAgent which is an agent that occupies the terrain agent.
 * The surface agent needs to be extende to an array of surface agents, eg. terrain can have a building on it or a road
 * but, it could also have people, cars or water on it
 */
public class Terrain extends Agent {
    protected float elevation; // Elevation in metres
    protected int nodata = -9999;

    //    protected Agent surfaceAgent = null;

    public Terrain(int id, float elevation) {
        this.agent_id = id;
        if (elevation == nodata) {
            colour = new Color(255, 255, 255);
        } else {
            colour = new Color(170, 170, 170);
        }
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    @Override
    public int getAgent_id() {
        return agent_id;
    }

    @Override
    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getNodata() {
        return nodata;
    }

    public void setNodata(int nodata) {
        this.nodata = nodata;
    }

    @Override
    public Color getColour() {
        return colour;
    }

    @Override
    public void setColour(Color colour) {
        this.colour = colour;
    }

//    public Agent getSurfaceAgent() {
//        return surfaceAgent;
//    }

//    public void setSurfaceAgent(Agent surfaceAgent) {
//        this.surfaceAgent = surfaceAgent;
//    }
}
