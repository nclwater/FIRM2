package uk.ac.ncl.nclwater.firm2.examples.nagelschreckenberg;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

public class Car extends Agent {
    int velocity = 1;
    int maxVelocity = 3;

    public Car(int agentId) {
        this.agent_id = agentId;
        this.colour = Color.BLUE;
    }

    public Car(int agentId, Color colour) {
        this.agent_id = agentId;
        this.colour = colour;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
}
