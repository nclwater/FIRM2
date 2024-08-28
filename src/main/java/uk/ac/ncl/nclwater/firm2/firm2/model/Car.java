package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.ComplexAgent;
import java.awt.*;
import java.util.ArrayList;

public class Car extends Agent {
    /**
     * An index into the movement array - thus where this agent currently resides
     */
    int movementIndex = 0;
    /**
     * Co-ordinates where car enters the simulation
     */
    @Expose
    @SerializedName("startcoordinates")
    private PointDouble startcoordinates;
    /**
     * Co-ordinates where car exists the simulation
     */
    @Expose
    @SerializedName("endcoordinates")
    private PointDouble endcoordinates;
    /**
     * An ArrayList of pre-determined positions eg. a road.
     */
    ArrayList<PointDouble> routeNodes;

    public Car(int id, ArrayList<PointDouble> route, PointDouble startcoordinates, PointDouble endcoordinates) {
        super();
        this.startcoordinates = startcoordinates;
        this.endcoordinates = endcoordinates;
        setRouteNodes(route);
        setColour(Color.ORANGE);
        setAgent_id(id);
    }

    public Car(PointDouble startcoordinates, PointDouble endcoordinates) {
        this.startcoordinates = startcoordinates;
        this.endcoordinates = endcoordinates;
    }

    public void setRouteNodes(ArrayList<PointDouble> routeNodes) {
        this.routeNodes = routeNodes;
    }


    public PointDouble getStartcoordinates() {
        return startcoordinates;
    }

    public void setStartcoordinates(PointDouble startcoordinates) {
        this.startcoordinates = startcoordinates;
    }

    public PointDouble getEndcoordinates() {
        return endcoordinates;
    }

    public void setEndcoordinates(PointDouble endcoordinates) {
        this.endcoordinates = endcoordinates;
    }
}
