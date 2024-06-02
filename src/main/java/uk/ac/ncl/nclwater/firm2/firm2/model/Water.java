package uk.ac.ncl.nclwater.firm2.firm2.model;

import uk.ac.ncl.nclwater.firm2.model.Agent;
import java.awt.*;

public class Water extends Agent {
    float waterLevel = 0;
    boolean isOcean = false;

    /**
     * A water type agent. If the agent is part of the ocean set isOcean to true
     * @param agent_id
     * @param waterLevel
     * @param isOcean
     */
    public Water(int agent_id, float waterLevel, boolean isOcean) {
        this.waterLevel = waterLevel;
        this.isOcean = isOcean;
        setAgent_id(agent_id);
        setColour(new Color(0x00, 117, 0x99, (isOcean ? 0xFF : 0x00)));
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = waterLevel;
    }

    @Override
    public void setColour(Color color) {
        super.setColour(color);
    }

    public boolean isOcean() {
        return isOcean;
    }

    public void setOcean(boolean ocean) {
        isOcean = ocean;
    }
}
