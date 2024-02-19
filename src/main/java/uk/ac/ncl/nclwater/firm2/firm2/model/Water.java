package uk.ac.ncl.nclwater.firm2.firm2.model;

import uk.ac.ncl.nclwater.firm2.model.Agent;
import java.awt.*;

public class Water extends Agent {

    public Water(int agent_id) {
        setAgent_id(agent_id);
        setColour(new Color(0, 117, 0x99));
    }
}
