package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

public class Defence extends Agent {

    @SerializedName("ordinate")
    @Expose
    PointDouble ordinate;
    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("height")
    @Expose
    float height;

    public Defence(int agent_id) {
        setAgent_id(agent_id);
        setColour(new Color(0x99, 0x00, 0x00));
    }

    public Defence(int agent_id, PointDouble ordinate, String defenceName, float height) {
        setAgent_id(agent_id);
        setOrdinate(ordinate);
        setName(defenceName);
        setHeight(height);
        setColour(new Color(0x99, 0x00, 0x00));
    }

    public PointDouble getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(PointDouble ordinate) {
        this.ordinate = ordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
