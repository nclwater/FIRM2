package uk.ac.ncl.nclwater.firm2.examples.nagelschreckenberg;

import uk.ac.ncl.nclwater.firm2.utils.Agent;

import java.awt.*;

public class Car extends Agent {
    int velocity = 1;
    int maxVelocity = 3;

    public Car(int agentId) {
        this.agent_id = agentId;
        this.colour = Color.BLUE;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
