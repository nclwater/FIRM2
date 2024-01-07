package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

/**
 * The first layer. Each cell in the grid will have at its base a Terrain agent.
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
            colour = new Color(65, 62, 62);
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
