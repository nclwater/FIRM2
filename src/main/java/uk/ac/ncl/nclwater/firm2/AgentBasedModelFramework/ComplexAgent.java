package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import java.awt.*;
import java.util.ArrayList;

public abstract class ComplexAgent extends Agent {
    /**
     * An index into the movement array - thus where this agent currently resides
     */
    int movementIndex = 0;
    /**
     * An ArrayList of pre-determined positions eg. a road.
     */
    ArrayList<Point> movements;

    public ComplexAgent() {

    }


    public int getMovementIndex() {
        return movementIndex;
    }

    public void setMovementIndex(int movementIndex) {
        this.movementIndex = movementIndex;
    }

    public ArrayList<Point> getMovements() {
        return movements;
    }

    public void setMovements(ArrayList<Point> movements) {
        this.movements = movements;
    }
}
