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
import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the state change for a given point in time. It should contain a state change for each grid
 * of the model. If the state change for a specif grid is null then there was no change at that point in time.
 */
public class ModelState implements Comparable<ModelState> {
    @SerializedName("time")
    @Expose
    private String time = null;
    @SerializedName("sea-level")
    @Expose
    private Float seaLevel = null;
    @SerializedName("defence-breach")
    @Expose
    private List<String> defenceBreach = new ArrayList<>();

    @SerializedName("cars")
    @Expose
    private ArrayList<Car> cars = new ArrayList<>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Float seaLevel) {
        this.seaLevel = seaLevel;
    }


    public List<String> getDefenceBreach() {
        return defenceBreach;
    }

    public void setDefenceBreach(List<String> defenceBreach) {
        this.defenceBreach = defenceBreach;
    }

    public Car getCar(int index) {
        return cars.get(index);
    }

    public void addCar(Car c) {
        cars.add(c);
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        StringBuilder vsb = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append("class ModelState {\n");
        sb.append("  time: ").append(time).append("\n");
        sb.append("  seaLevel: ").append(seaLevel).append("\n");
        sb.append("  defenceBreach: ").append(defenceBreach).append("\n");
        sb.append("  cars: ").append(cars).append("\n");
        sb.append("}\n");

        return sb.toString();
    }

    @Override
    public int compareTo(ModelState o) {
        long o_time = Utilities.timeStringToUnixTimestamp(0, o.getTime());
        long t_time = Utilities.timeStringToUnixTimestamp(0, time);
        return (Long.compare(t_time, o_time));
    }
}