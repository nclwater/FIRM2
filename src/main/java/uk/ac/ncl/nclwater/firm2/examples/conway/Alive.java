package uk.ac.ncl.nclwater.firm2.examples.conway;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Agent;

/**
 * A (Conway Game of Life) cell that is alive
 */
public class Alive extends Agent {

    public Alive(String agent_id) {
        this.agent_id = agent_id;
    }

}
