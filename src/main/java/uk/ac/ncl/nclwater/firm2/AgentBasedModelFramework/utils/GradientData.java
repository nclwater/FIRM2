package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils;

import java.awt.*;

/**
 * A helper class to hold a colour gradient.
 */
class GradientData {
    public Color value;
    public float threshold;

    /**
     *
     * @param value a colour value
     * @param threshold
     */
    public GradientData(Color value, float threshold) {
        this.value = value;
        this.threshold = threshold;
    }
}