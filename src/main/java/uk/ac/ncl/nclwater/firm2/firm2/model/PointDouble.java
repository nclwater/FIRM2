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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.awt.*;
import java.awt.geom.Point2D;

public class PointDouble extends Point2D {
    @SerializedName("x")
    @Expose
    double x;
    @SerializedName("y")
    @Expose
    double y;

    public PointDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PointDouble(Point point) {
        this.x = point.x;
        this.y = point.y;
    }
    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("x: ").append(this.x).append(", ");
        sb.append("y: ").append(this.y);
        return sb.toString();
    }
}
