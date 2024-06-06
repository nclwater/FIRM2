package uk.ac.ncl.nclwater.firm2.firm2.model;

import uk.ac.ncl.nclwater.firm2.model.Agent;
import java.awt.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * An agent of type building
 */
public class Building extends Agent {

    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("ordinate")
    @Expose
    private PointDouble ordinate;
    @SerializedName("nearest-road")
    @Expose
    String nearestRoad_ID;

    /**
     * An agent of type Building
     * @param id a unique id for the agent
     * @param type the type of building
     */
    public Building(int id, int type) {
        setAgent_id(id);
        this.type = type;
        if (type == 0) {
            setColour(new Color(170, 170, 170)); // Buildings default to gray
        } else {
            setColour(new Color(0x00, 0xff, 0x00)); // Residential defaults to green
        }
    }


    /**
     * An agent of type Building
     * @param id a unique id for the agent
     * @param type the type of building
     * @param ordinate the ordinance map co-ordinates of the building
     */
    public Building(int id, int type, PointDouble ordinate, String nearestRoad_ID) {
        setAgent_id(id);
        this.type = type;
        this.ordinate = ordinate;

        if (type == 0) {
            setColour(new Color(170, 170, 170)); // Buildings default to gray
        } else {
            setColour(new Color(0x00, 0xff, 0x00)); // Residential defaults to green
        }
        this.nearestRoad_ID = nearestRoad_ID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PointDouble getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(PointDouble ordinate) {
        this.ordinate = ordinate;
    }

    public String getNearestRoad_ID() {
        return nearestRoad_ID;
    }

    public void setNearestRoad_ID(String nearestRoad_ID) {
        this.nearestRoad_ID = nearestRoad_ID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("type: ").append(type).append("\n");
        sb.append("ordinate: ").append(ordinate).append("\n");
        sb.append("nearestRoad_ID: ").append(nearestRoad_ID).append("\n");
        return sb.toString();
    }
}
