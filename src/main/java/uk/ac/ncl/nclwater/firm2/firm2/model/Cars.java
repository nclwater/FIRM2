package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Cars {

    @SerializedName("cars")
    @Expose
    private List<Car> cars = new ArrayList<>();

    public ArrayList<Car> getCars() {

        return (ArrayList<Car>) cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
