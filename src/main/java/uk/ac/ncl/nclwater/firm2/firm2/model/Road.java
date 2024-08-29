package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;

import java.awt.*;
import java.util.ArrayList;

public class Road extends Agent {
    @Expose
    @SerializedName("road_IDs")
    String[] roadIDs = new String[3];

    @Expose
    @SerializedName("speed_limit")
    int speedLimit = 30;

    @Expose
    @SerializedName("road_length")
    long roadLength;

    @Expose
    @SerializedName("road_type")
    String roadType;

    @Expose
    @SerializedName("polyline_coordinates")
    ArrayList<PointInteger> polylineCoordinates = new ArrayList<>();


    /**
     * Constructor to create a new instance of a road
     * @param agentId A unique agent id
     * @param roadIDs The IDs of the road that make up this road (clarify)
     */
    public Road(int agentId, @org.jetbrains.annotations.NotNull String[] roadIDs) {
        this.agent_id = agentId;
        this.roadIDs[0] = roadIDs[0];
        this.roadIDs[1] = roadIDs[1];
        this.roadIDs[2] = roadIDs[2];
        this.colour = Color.black;
    }

    public Road(long roadLength, String roadType, ArrayList<PointInteger> polylineCoordinates, String[] roadIDs) {
        this.roadLength = roadLength;
        this.roadType = roadType;
        this.polylineCoordinates = polylineCoordinates;
        this.roadIDs = roadIDs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Agent ID: " + agent_id + "\n");
        sb.append("Road ID 1: ").append(roadIDs[0]).append("\n");
        sb.append("Road ID 2: ").append(roadIDs[1]).append("\n");
        sb.append("Road ID 3: ").append(roadIDs[2]).append("\n");
        sb.append("Road Length: ").append(roadLength).append("\n");
        sb.append("Road Type: ").append(roadType).append("\nPolyline Coordinates: \n");
        polylineCoordinates.forEach(p -> sb.append(" ").append(p.getX()).append(",").append(p.getY()).append("\n") );
        sb.append("Colour: ").append(getColour().toString()).append("\n");
        return sb.toString();
    }

    public String[] getRoadIDs() {
        return roadIDs;
    }

    public void setRoadIDs(String[] roadIDs) {
        this.roadIDs = roadIDs;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }


    public long getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(long roadLength) {
        this.roadLength = roadLength;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public ArrayList<PointInteger> getPolylineCoordinates() {
        return polylineCoordinates;
    }

    public void setPolylineCoordinates(ArrayList<PointInteger> polylineCoordinates) {
        this.polylineCoordinates = polylineCoordinates;
    }

    public void addCoordinates(PointInteger coordinates) {
        polylineCoordinates.add(coordinates);
    }
}