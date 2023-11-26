package uk.ac.ncl.nclwater.firm2.firm2;

import uk.ac.ncl.nclwater.firm2.model.Agent;

import java.awt.*;

public class Defence extends Agent {

    public Defence(int agent_id) {
        setAgent_id(agent_id);
        setColour(new Color(0x99, 0x00, 0x00));
    }
}
