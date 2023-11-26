package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

/**
 * An agent of type building
 */
public class Building extends Agent {

    private int type;
    public Building(int id, int type) {
        setAgent_id(id);
        this.type = type;
        if (type == 0) {
            setColour(new Color(170, 170, 170)); // Buildings default to gray
        } else {
            setColour(new Color(0xff, 0xff, 0x00)); // Residential defaults to yellow
        }
    }
}
