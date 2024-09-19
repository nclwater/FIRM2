package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import uk.ac.ncl.nclwater.firm2.firm2.controller.LoadRoadsGrid;

import java.awt.*;
import java.util.ArrayList;

public class BNGRoad extends Agent {
    private static final Logger logger = LoggerFactory.getLogger(BNGRoad.class);

    /**
     * Each road has three IDs associated with it. The first is the road id, the second is the ID of the
     * starting position and the third is the ID of the end position of the road.
     */
    @Expose
    @SerializedName("road_IDs")
    String[] roadIDs = new String[3];

    @Expose
    @SerializedName("road_length")
    long roadLength;

    @Expose
    @SerializedName("road_type")
    String roadType;

    /**
     * The nodes in the road - a list of BNG xy coordinates
     */
    @Expose
    @SerializedName("polyline_coordinates")
    ArrayList<PointDouble> polylineCoordinates = new ArrayList<>();

    int roadSpeedLimit = 0;


    /**
     * Constructor to create a new instance of a road
     * @param agentId A unique agent id
     * @param roadIDs The IDs of the road that make up this road (clarify)
     */
    public BNGRoad(String agentId, @org.jetbrains.annotations.NotNull String[] roadIDs) {
        this.agent_id = agentId;
        this.roadIDs[0] = roadIDs[0];
        this.roadIDs[1] = roadIDs[1];
        this.roadIDs[2] = roadIDs[2];
        this.colour = Color.black;
    }

    /**
     * Return the three IDs associated with this road. The first ID is the ID of the road
     * The second ID is the first node of the road
     * The third ID is the last node of the road
     * @return
     */
    public String getID() {
        return roadIDs[0];
    }

    public BNGRoad(long roadLength, String roadType, ArrayList<PointDouble> polylineCoordinates, String[] roadIDs) {
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
        sb.append("Road Type: ").append(roadType).append("\n");
        sb.append("Speed: ").append(roadSpeedLimit).append("\n");
//        sb.append("Colour: ").append(getColour().toString()).append("\n");
        return sb.toString();
    }

    public String[] getRoadIDs() {
        return roadIDs;
    }

    public void setRoadIDs(String[] roadIDs) {
        this.roadIDs = roadIDs;
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

    /**
     * Get the co-ordinates that comprise the road
     * @return The co-ordinates that comprise the road as an ArrayList of PointDouble
     */
    public ArrayList<PointDouble> getPolylineCoordinates() {
        return polylineCoordinates;
    }

    /**
     * Get the co-ordinates that comprise the road
     * @return The co-ordinates that comprise the road as an ArrayList of PointDouble
     */
    public ArrayList<PointInteger> getPolylineCoordinatesInt() {
        ArrayList<PointInteger> points = new ArrayList<>();
        polylineCoordinates.forEach(p -> {
            PointInteger newPoint = new PointInteger((int) (p.getX() * 1000), (int) (p.getX() * 1000));
            points.add(newPoint);
        });
        return points;
    }

    public void setPolylineCoordinates(ArrayList<PointDouble> polylineCoordinates) {
        this.polylineCoordinates = polylineCoordinates;
    }

    public void addCoordinates(PointDouble coordinates) {
        polylineCoordinates.add(coordinates);
    }

    public int getRoadSpeedLimit() {
        return roadSpeedLimit;
    }

    public void setRoadSpeedLimit(int roadSpeedLimit) {
        this.roadSpeedLimit = roadSpeedLimit;
    }
}