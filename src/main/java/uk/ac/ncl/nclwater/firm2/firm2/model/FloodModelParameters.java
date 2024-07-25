package uk.ac.ncl.nclwater.firm2.firm2.model;

/**
 * Override default model parameters to include water level
 * and PNG-on-tick
 */
public class FloodModelParameters extends uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.ModelParameters {
    float oceanDepth = 0;
    boolean pngOnTick = false;


    public FloodModelParameters() {}

    public float getOceanDepth() {
        return oceanDepth;
    }

    public void setOceanDepth(float oceanDepth) {
        this.oceanDepth = oceanDepth;
    }

    public boolean isPngOnTick() {
        return pngOnTick;
    }

    public void setPngOnTick(boolean pngOnTick) {
        this.pngOnTick = pngOnTick;
    }
}
