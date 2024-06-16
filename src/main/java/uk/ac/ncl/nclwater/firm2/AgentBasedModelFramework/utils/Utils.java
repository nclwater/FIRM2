package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.utils;

import java.awt.*;

public class Utils {

    /**
     * Calculate a colour gradient to display map heights
     *
     * @param height
     * @param height_min
     * @param height_max
     * @return the calculated colour
     */
    public static Color getHeightmapGradient(String type, float height, float height_min, float height_max) {
        GradientData[] gradient = new GradientData[]{
                new GradientData(new Color(0xe0, 0xce, 0xb5, 0xff), 0.0f),
                new GradientData(new Color(0x97, 0x70, 0x3c, 0xff), 0.5f),
                new GradientData(new Color(0x0B, 0x08, 0x04, 0xff), 1.0f)};
        if (type.equals("water")) {
            gradient = new GradientData[]{
                    new GradientData(new Color(0x00, 0x75, 0x99, 0x00), 0.0f),
                    new GradientData(new Color(0x27, 0x75, 0x99, 0x7F), 0.5f),
                    new GradientData(new Color(0x0B, 0x75, 0x99, 0xff), 1.0f),
                };
        }
        if (type.equals("terrain")) {
            gradient = new GradientData[]{
                    new GradientData(new Color(0xe0, 0xce, 0xb5, 0xff), 0.0f),
                    new GradientData(new Color(0x97, 0x70, 0x3c, 0xff), 0.5f),
                    new GradientData(new Color(0x0B, 0x08, 0x04, 0xff), 1.0f),
            };
        }

        float threshold = (height - height_min) / height_max;

        for (int i = 1; i < gradient.length; i++) {
            if (threshold <= gradient[i].threshold) {
                float t = (threshold - gradient[i - 1].threshold) / gradient[i].threshold;
                return new Color(
                        (gradient[i - 1].value.getRed() * (1.0f - t) + gradient[i].value.getRed() * t) / 255.0f,
                        (gradient[i - 1].value.getGreen() * (1.0f - t) + gradient[i].value.getGreen() * t) / 255.0f,
                        (gradient[i - 1].value.getBlue() * (1.0f - t) + gradient[i].value.getBlue() * t) / 255.0f,
                        (gradient[i - 1].value.getAlpha() * (1.0f - t) + gradient[i].value.getAlpha() * t) / 255.0f
                );
            }
        }
        return gradient[gradient.length - 1].value;
    }

}
