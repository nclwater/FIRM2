package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This class consists of an ArrayList. Each item in the ArrayList is an array of floats representing the elevations
 * of one line on a map
 */
public class TerrainLayer {

    @Expose
    @SerializedName("terrain-line")
    private ArrayList<TerrainLine> terrainLines = new ArrayList<>();

    /**
     * Return all lines of terrain elevations
     * @return
     */
    public ArrayList<TerrainLine> getTerrainLines() {
        return terrainLines;
    }

    public void setTerrainLines(ArrayList<TerrainLine> terrainLines) {
        this.terrainLines = terrainLines;
    }

    /**
     * Add a line of terrain elevations
     * @param terrainLine
     */
    public void add (TerrainLine terrainLine) {
        terrainLines.add(terrainLine);
    }
}
