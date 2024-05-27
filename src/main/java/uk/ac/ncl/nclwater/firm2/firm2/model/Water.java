package uk.ac.ncl.nclwater.firm2.firm2.model;

import uk.ac.ncl.nclwater.firm2.model.Agent;
import java.awt.*;

public class Water extends Agent {
    float waterLevel = 0;
    boolean isOcean = false;

    public Water(int agent_id, float waterLevel, boolean isOcean) {
        this.waterLevel = waterLevel;
        this.isOcean = isOcean;
        setAgent_id(agent_id);
        setColour(new Color(0, 117, 0x99, 0xFF));
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = waterLevel;
    }

    @Override
    public void setColour(Color color) {
        super.setColour(new Color(0, 117, 0x99, (waterLevel < 0.0001) ? 0 : 0xFF));
    }
}
