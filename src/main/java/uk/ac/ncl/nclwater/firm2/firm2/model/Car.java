package uk.ac.ncl.nclwater.firm2.firm2.model;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;

import java.awt.*;

public class Car extends Agent {

    public Car(int id) {
        setColour(Color.ORANGE);
        setAgent_id(id);
    }
}
