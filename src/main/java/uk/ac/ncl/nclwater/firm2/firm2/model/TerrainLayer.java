package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class consists of an ArrayList. Each item in the ArrayList is an array of floats representing the elevations
 * of one line on a map
 */
public class TerrainLayer extends ArrayList<TerrainLine> {

    @Expose
    @SerializedName("terrain-layer")
    private TerrainLayer terrainLayer = this;


}
