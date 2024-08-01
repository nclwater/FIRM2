package uk.ac.ncl.nclwater.firm2.firm2.model;

public class Connection {
    private String connectionID;
    private String ID1;
    private String ID2;
    private double distance;

    public Connection(String connectionID, String ID1, String ID2, double distance) {
        this.connectionID = connectionID;
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.distance = distance;
    }

    public String getID1() {
        return ID1;
    }

    public void setID1(String ID1) {
        this.ID1 = ID1;
    }

    public String getID2() {
        return ID2;
    }

    public void setID2(String ID2) {
        this.ID2 = ID2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(String connectionID) {
        this.connectionID = connectionID;
    }
}
