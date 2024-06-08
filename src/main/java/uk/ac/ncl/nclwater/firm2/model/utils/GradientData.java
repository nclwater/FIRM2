package uk.ac.ncl.nclwater.firm2.model.utils;

import java.awt.*;

/**
 * A helper class to hold a colour gradient.
 */
class GradientData {
    public Color value;
    public float threshold;

    public GradientData(Color v, float t) {
        value = v;
        threshold = t;
    }
}