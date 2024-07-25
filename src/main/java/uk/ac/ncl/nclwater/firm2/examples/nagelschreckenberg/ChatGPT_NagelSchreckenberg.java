package uk.ac.ncl.nclwater.firm2.examples.nagelschreckenberg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class NS_Car {
    int position;
    int speed;

    public NS_Car(int position) {
        this.position = position;
        this.speed = 0;
    }
}

public class ChatGPT_NagelSchreckenberg {
    private static final int ROAD_LENGTH = 100; // Length of the road
    private static final int MAX_SPEED = 5; // Maximum speed of the cars
    private static final int NUM_CARS = 10; // Number of cars
    private static final double P_SLOW = 0.3; // Probability of random slowdown

    private List<NS_Car> cars;
    private Random random;

    public ChatGPT_NagelSchreckenberg() {
        cars = new ArrayList<>();
        random = new Random();
        initializeCars();
    }

    private void initializeCars() {
        for (int i = 0; i < NUM_CARS; i++) {
            // Place cars at random positions on the road
            cars.add(new NS_Car(random.nextInt(ROAD_LENGTH)));
        }
    }

    public void simulateStep() {
        for (NS_Car car : cars) {
            accelerate(car);
            slowDown(car);
            randomSlow(car);
        }
        moveCars();
        printRoad();
    }

    private void accelerate(NS_Car car) {
        if (car.speed < MAX_SPEED) {
            car.speed++;
        }
    }

    private void slowDown(NS_Car car) {
        NS_Car nextCar = findNextCar(car);
        if (nextCar != null) {
            int distance = (nextCar.position - car.position + ROAD_LENGTH) % ROAD_LENGTH;
            if (distance <= car.speed) {
                car.speed = distance - 1;
            }
        }
    }

    private void randomSlow(NS_Car car) {
        if (random.nextDouble() < P_SLOW) {
            car.speed = Math.max(car.speed - 1, 0);
        }
    }

    private void moveCars() {
        for (NS_Car car : cars) {
            car.position = (car.position + car.speed) % ROAD_LENGTH;
        }
    }

    private NS_Car findNextCar(NS_Car car) {
        NS_Car nextCar = null;
        int minDistance = ROAD_LENGTH;

        for (NS_Car otherCar : cars) {
            if (otherCar != car) {
                int distance = (otherCar.position - car.position + ROAD_LENGTH) % ROAD_LENGTH;
                if (distance > 0 && distance < minDistance) {
                    minDistance = distance;
                    nextCar = otherCar;
                }
            }
        }
        return nextCar;
    }

    private void printRoad() {
        char[] road = new char[ROAD_LENGTH];
        Arrays.fill(road, '.');

        for (NS_Car car : cars) {
            road[car.position] = 'C';
        }

        System.out.println(new String(road));
    }

    public static void main(String[] args) {
        ChatGPT_NagelSchreckenberg simulation = new ChatGPT_NagelSchreckenberg();

        for (int i = 0; i < 100; i++) { // Run the simulation for 100 steps
            simulation.simulateStep();
            try {
                Thread.sleep(100); // Pause for a short time between steps to see the simulation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
