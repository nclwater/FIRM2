package uk.ac.ncl.nclwater.firm2.firm2.model;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.ComplexAgent;

import java.awt.*;
import java.util.ArrayList;

public class Car extends ComplexAgent {

    public Car(int id, ArrayList<Point> movement) {
        super();
        setColour(Color.ORANGE);
        setAgent_id(id);
    }
}
