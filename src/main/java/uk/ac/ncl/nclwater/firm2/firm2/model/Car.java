package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;

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
    private PointDouble startCoordinates;
    /**
     * Co-ordinates where car exists the simulation
     */
    @Expose
    @SerializedName("endcoordinates")
    private PointDouble endCoordinates;
    /**
     * An ArrayList of pre-determined positions eg. a road.
     */
    ArrayList<PointDouble> routeNodes;

    // speed limit
    private int speedLimit;
    // the position (node) where the car is currently located
    private PointDouble currentCoordinates;
    // the target location (node) of the car
    private PointDouble targetPos;
    // the next position (node) in the cars journey (A* shortest path)
    private PointDouble intermediatePos;
    // the distance to the next node
    private double intermediateDistance;
    // the distance travelled from currentCoordinates so far
    private double currentDistance;
    // is the car still on the model? If true, yes, if false it has exited the model
    private boolean inPlay = true;

    /**
     *
     * @param id
     * @param route
     * @param startCoordinates
     * @param endCoordinates
     */
    public Car(int id, ArrayList<PointDouble> route, PointDouble startCoordinates, PointDouble endCoordinates) {
        super();
        this.startCoordinates = startCoordinates;
        this.endCoordinates = endCoordinates;
        this.currentCoordinates = new PointDouble(startCoordinates.getX(), startCoordinates.getY());
        setRouteNodes(route);
        setColour(Color.ORANGE);
        setAgent_id(id);
    }

    public Car(PointDouble startCoordinates, PointDouble endCoordinates) {
        this.startCoordinates = startCoordinates;
        this.endCoordinates = endCoordinates;
    }

    public void setRouteNodes(ArrayList<PointDouble> routeNodes) {
        this.routeNodes = routeNodes;
    }


    public PointDouble getStartCoordinates() {
        return startCoordinates;
    }

    public void setStartCoordinates(PointDouble startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    public PointDouble getEndCoordinates() {
        return endCoordinates;
    }

    public void setEndCoordinates(PointDouble endCoordinates) {
        this.endCoordinates = endCoordinates;
    }


    public PointDouble getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setCurrentCoordinates(PointDouble currentCoordinates) {
        this.currentCoordinates = currentCoordinates;
    }

    public double getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(float currentDistance) {
        this.currentDistance = currentDistance;
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
}
