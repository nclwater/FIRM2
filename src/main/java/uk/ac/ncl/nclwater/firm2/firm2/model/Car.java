package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.graphstream.graph.Path;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import java.awt.*;
import java.util.ArrayList;

public class Car extends Agent {

    @Expose
    @SerializedName("agent-id")
    String agent_id;

    @Expose
    @SerializedName("itinerary")
    ArrayList<ItineraryItem> carItinerary = new ArrayList<>();

    private String previousNode;
    private String startNode;
    private String endNode;
    private int itineraryIndex = 0;

    /**
     * An ArrayList of pre-determined positions eg. a road.
     */
    Path routeNodes;
    // speed limit
    private int speedLimit;
    // the target location (node) of the car
    private PointDouble targetPos;
    // the next position (node) in the cars journey (A* shortest path)
    private PointDouble intermediatePos;
    // the distance to the next node
    private double intermediateDistance;
    // the distance travelled from first node so far
    private double currentDistance;
    // is the car still on the model? If true, yes, if false it has exited the model
    private boolean inPlay = true;
    // the car has "drowned"
    private boolean isDrowned = false;
    private boolean isStranded = false;
    private boolean isAtDestination = false;

    /**
     *
     * @param id
     */
    public Car(String id, ArrayList<ItineraryItem> itinerary) {
        super();
        setColour(Color.ORANGE);
        setAgent_id(id);
        this.carItinerary = itinerary;
        this.startNode = itinerary.get(0).getStartNode();
        this.endNode = itinerary.get(0).getEndNode();
    }

    @Override
    public String toString() {
        return "Car- ID: " + getAgent_id() + ", drowned: " + isDrowned + ", stranded: " + isStranded;
    }

    /**
     * The route that the car is following. Nodes are removed as the car reaches the next node in the list so
     * that node(0) is always the current position of the car.
     * @param routeNodes
     */
    public void setRouteNodes(Path routeNodes) {
        this.routeNodes = null;
        this.routeNodes = routeNodes;
    }

    public double getCurrentDistance() {
        return currentDistance;
    }

    /**
     * Increment the current distance with the speed travelled in one second
     * This should be called on every tick to move the car along.
     */
    public void incCurrentDistance() {
        currentDistance += Utilities.distanceTravelled(speedLimit);
    }

    public double getIntermediateDistance() {
        return intermediateDistance;
    }

    public void setIntermediateDistance(float intermediateDistance) {
        this.intermediateDistance = intermediateDistance;
    }

    public PointDouble getIntermediatePos() {
        return intermediatePos;
    }

    public void setIntermediatePos(PointDouble intermediatePos) {
        this.intermediatePos = intermediatePos;
    }

    public PointDouble getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(PointDouble targetPos) {
        this.targetPos = targetPos;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }

    public String getStartNode() {
        return startNode;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public String getEndNode() {
        return endNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }

    public Path getRouteNodes() {
        return routeNodes;
    }

    public void setIntermediateDistance(double intermediateDistance) {
        this.intermediateDistance = intermediateDistance;
    }

    public void setCurrentDistance(double coveredDistance) {
        this.currentDistance = coveredDistance;
    }

    public boolean isDrowned() {
        return isDrowned;
    }

    public void setDrowned(boolean drowned) {
        isDrowned = drowned;
    }

    public boolean isStranded() {
        return isStranded;
    }

    public void setStranded(boolean stranded) {
        isStranded = stranded;
    }

    public boolean isAtDestination() {
        return isAtDestination;
    }

    public void setAtDestination(boolean atDestination) {
        isAtDestination = atDestination;
    }

    public ArrayList<ItineraryItem> getCarItinerary() {
        return carItinerary;
    }

    public void setCarItinerary(ArrayList<ItineraryItem> carItinerary) {
        this.carItinerary = carItinerary;
    }

    @Override
    public String getAgent_id() {
        return agent_id;
    }

    @Override
    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public int getItineraryIndex() {
        return itineraryIndex;
    }

    public void incItineraryIndex() {
        this.itineraryIndex++;
    }

    public String getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(String previousNode) {
        this.previousNode = previousNode;
    }
}
