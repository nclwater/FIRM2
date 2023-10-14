package uk.ac.ncl.nclwater.firm2;

import uk.ac.ncl.nclwater.firm2.utils.Agent;

public class Dead implements Agent {

    int agent_id = 0;
    Dead(int agent_id) {
        this.agent_id++;
    }

    @Override
    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    @Override
    public int getAgent_id() {
        return this.agent_id;
    }

    @Override
    public void incTickAge() {

    }

    @Override
    public int getTickAge() {
        return 0;
    }
}
