package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.graphstream.graph.Path;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;

import java.awt.*;

public class Car extends Agent {

    @Expose
    @SerializedName("agent-id")
    private String agentId;

    @Expose
    @SerializedName("start-node")
    private String startNode;

    @Expose
    @SerializedName("end-node")
    private String endNode;

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
    private double coveredDistance;
    // is the car still on the model? If true, yes, if false it has exited the model
    private boolean inPlay = true;
    // the car has "drowned"
    private boolean isDrowned = false;

    /**
     *
     * @param id

     */
    public Car(String id, String startNode, String endNode) {
        super();
        this.startNode = startNode;
        this.endNode = endNode;
        setColour(Color.ORANGE);
        setAgent_id(id);
    }

    /**
     * The route that the car is following. Nodes are removed as the car reaches the next node in the list so
     * that node(0) is always the current position of the car.
     * @param routeNodes
     */
    public void setRouteNodes(Path routeNodes) {
        this.routeNodes = routeNodes;
    }

    public double getCoveredDistance() {
        return coveredDistance;
    }

    public void setCoveredDistance(double coveredDistance) {
        this.coveredDistance = coveredDistance;
    }

    /**
     * Increment the current distance with the speed travelled in one second
     * This should be called on every tick to move the car along.
     */
    public void incCurrentDistance() {
        coveredDistance += Utilities.distanceTravelled(speedLimit);
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

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
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

    public void setCurrentDistance(double currentDistance) {
        this.coveredDistance = currentDistance;
    }

    public boolean isDrowned() {
        return isDrowned;
    }

    public void setDrowned(boolean drowned) {
        isDrowned = drowned;
    }
}
