package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * The travel plan for the car when it enters the model
 */
public class CarItinerary {

    @Expose
    @SerializedName("itinerary")
    private ArrayList<ItineraryItem> itinerary = new ArrayList<ItineraryItem>();

    public ArrayList<ItineraryItem> getItinerary() {
        return itinerary;
    }

    public void setItinerary(ArrayList<ItineraryItem> itinerary) {
        this.itinerary = itinerary;
    }
}
