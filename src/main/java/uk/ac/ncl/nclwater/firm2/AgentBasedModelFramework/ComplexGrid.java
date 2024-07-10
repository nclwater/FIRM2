package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import java.util.ArrayList;

public class ComplexGrid {

    /**
     * An ArrayList of type Agent. This array list is an alternative to a grid. Rather than having a grid of type agent
     * if one has agents of a type where more than one agent can occupy the grid.
     */
    ArrayList<ComplexAgent> agents = new ArrayList<ComplexAgent>();
    /**
     * A keyword to be used in a hashmap describing the list of agents e.g. "cars"
     */
    String key;

    public ComplexGrid(ArrayList<ComplexAgent> agents, String key) {
        this.agents = agents;
        this.key = key;
    }

    public ArrayList<ComplexAgent> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<ComplexAgent> agents) {
        this.agents = agents;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
