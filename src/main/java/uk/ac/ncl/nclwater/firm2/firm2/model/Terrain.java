package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;

import java.awt.*;

/**
 * The first layer. Each cell in the grid will have at its base a Terrain agent.
 */
public class Terrain extends Agent {
    @SerializedName("elevation")
    @Expose
    protected Float elevation; // Elevation in metres
    protected int nodata = -9999;

    //    protected Agent surfaceAgent = null;

    public Terrain(int id, float elevation) {
        this.agent_id = id;
        if (elevation == nodata) {
            colour = new Color(255, 255, 255);
        } else {
            colour = new Color(65, 62, 62);
            this.elevation = elevation;
        }
    }

    public Float getElevation() {
        return elevation;
    }

    public void setElevation(Float elevation) {
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

    @Override
    public Color getColour() {
        return colour;
    }

    @Override
    public void setColour(Color colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Elevation: ").append(elevation).append("\n");
        sb.append("Colour: ").append(getColour().toString()).append("\n");
        return sb.toString();
    }

//    public Agent getSurfaceAgent() {
//        return surfaceAgent;
//    }

//    public void setSurfaceAgent(Agent surfaceAgent) {
//        this.surfaceAgent = surfaceAgent;
//    }
}
