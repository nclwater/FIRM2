package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

public class Road extends Agent {
    int speedlimit = 30;

    String[] road_ids = new String[3];

    public Road(int agentId, String[] road_ids) {
        this.agent_id = agentId;
        this.road_ids[0] = road_ids[0];
        this.road_ids[1] = road_ids[1];
        this.road_ids[2] = road_ids[2];
        this.colour = Color.black;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Agent ID: " + agent_id + "\n");
        sb.append("Road ID 1: " + road_ids[0] + "\n");
        sb.append("Road ID 2: " + road_ids[1] + "\n");
        sb.append("Road ID 3: " + road_ids[2] + "\n");
        sb.append("Colour: " + this.colour.toString());
        return sb.toString();
    }

    public String[] getRoad_ids() {
        return road_ids;
    }

    public void setRoad_ids(String[] road_ids) {
        this.road_ids = road_ids;
    }
}
