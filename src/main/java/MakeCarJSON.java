import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.ncl.nclwater.firm2.firm2.model.Car;
import uk.ac.ncl.nclwater.firm2.firm2.model.Cars;
import uk.ac.ncl.nclwater.firm2.firm2.model.ItineraryItem;

import java.util.ArrayList;

public class MakeCarJSON {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        Cars cars = new Cars();
        ArrayList<ItineraryItem> itinerary = new ArrayList<ItineraryItem>();
        itinerary.add(new ItineraryItem("4000000012472821", "4000000012843295", 600));
        itinerary.add(new ItineraryItem("4000000012843295", "4000000012472821", 20));
        Car car = new Car("car0001", itinerary);
        Car car2 = new Car("car0002", itinerary);
        cars.addCar(car);
        cars.addCar(car2);
        System.out.println(gson.toJson(cars, Cars.class));
    }
}
