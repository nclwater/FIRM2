package uk.ac.ncl.nclwater.firm2.firm2.model;

public abstract class State {

    /**
     * Time of the state change
     */
    Long timestamp;
    /**
     * Type of state change
     */
    String stateType;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }
}
