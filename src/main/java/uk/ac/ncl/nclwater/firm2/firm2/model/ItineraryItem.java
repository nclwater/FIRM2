package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItineraryItem {
    @Expose
    @SerializedName("start-node")
    private String startNode;
    @Expose
    @SerializedName("end-node")
    private String endNode;
    /**
     * The time that the car has to wait when it reaches its destination, before continuing onto
     * the next itinerary item.
     */
    @Expose
    @SerializedName("wait-time")
    private Integer waitTime; // in seconds

    public ItineraryItem(String startNode, String endNode, Integer waitTime) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.waitTime = waitTime;
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

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
