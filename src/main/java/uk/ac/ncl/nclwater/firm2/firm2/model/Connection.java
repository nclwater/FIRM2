/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


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
