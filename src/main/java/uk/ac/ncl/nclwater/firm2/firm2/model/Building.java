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
    private Point ordinate = new Point();

    public Building(int id, int type) {
        setAgent_id(id);
        this.type = type;
        if (type == 0) {
            setColour(new Color(170, 170, 170)); // Buildings default to gray
        } else {
            setColour(new Color(0x00, 0xff, 0x00)); // Residential defaults to green
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Point getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(Point ordinate) {
        this.ordinate = ordinate;
    }
}
