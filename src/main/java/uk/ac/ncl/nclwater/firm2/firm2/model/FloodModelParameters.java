package uk.ac.ncl.nclwater.firm2.firm2.model;

/**
 * Override default model parameters to include water level
 */
public class FloodModelParameters extends uk.ac.ncl.nclwater.firm2.model.ModelParameters {
    float oceanDepth = 0;

    public FloodModelParameters() {}

    public float getOceanDepth() {
        return oceanDepth;
    }

    public void setOceanDepth(float oceanDepth) {
        this.oceanDepth = oceanDepth;
    }

}
