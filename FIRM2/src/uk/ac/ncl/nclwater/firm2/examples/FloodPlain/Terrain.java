package uk.ac.ncl.nclwater.firm2.examples.FloodPlain;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

public class Terrain extends Agent {
    protected float elevation; // Elevation in metres
    protected int agent_id = 0;
    protected int nodata = -9999;
    protected Color colour;
    public Terrain(int id, float elevation) {
        this.agent_id = id;
        if (elevation == nodata) {
            colour = new Color(0, 0, 255);
        }
    }

}
