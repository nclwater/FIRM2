package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class represents one line of elevations on a map
 */
public class TerrainLine {
    @Expose
    @SerializedName("elevations")
    protected Float[] elevation; // Elevation in metres

    /**
     * Constructor taking an array of elevations as floats
     * @param elevation
     */
    public TerrainLine(Float[] elevation) {
        this.elevation = elevation;
    }

    /**
     * Constructor taking an array of elevations as strings and converts the strings to floats
     * @param str_elevation
     */
    public TerrainLine(String[] str_elevation) {
        this.elevation = new Float[str_elevation.length];
        for (int i = 0; i < str_elevation.length; i++) {
            if (str_elevation[i].equals("-9999"))
                this.elevation[i] = -9999F;
            else
                this.elevation[i] = Float.parseFloat(str_elevation[i]);
        }
    }

    public Float[] getElevation() {
        return elevation;
    }

    public void setElevation(Float[] elevation) {
        this.elevation = elevation;
    }
}
