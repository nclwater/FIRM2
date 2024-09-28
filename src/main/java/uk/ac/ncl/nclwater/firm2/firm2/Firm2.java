package uk.ac.ncl.nclwater.firm2.firm2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.*;
import uk.ac.ncl.nclwater.firm2.firm2.controller.*;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.*;

public class Firm2 extends Model{

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static FloodModelParameters floodModelParameters;
    private static GlobalVariables globalVariables;
    private final Properties properties;
    private Long modelTimeStamp;
    private ModelState modelState = new ModelState();
    private int modelStateIndex = 0;
    private ModelStateChanges modelStateChanges;
    private Float maintainSeaLevel = null;
    private AStar aStar = null;
    private final Cars cars = new Cars();
    private final Cars drownedCars = new Cars();
    private final Cars destinationCars = new Cars();
    private final Cars strandedCars = new Cars();
    private Graph graph;

    /**
     * Initialise the model
     */
    @Override
    public void modelInit() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // Read global variable (eventually to be read from environment vars for DAFNI)
            globalVariables = gson.fromJson(new FileReader(
                            properties.getProperty("INPUT_DATA") + properties.getProperty("MODEL_PARAMETERS")),
                    GlobalVariables.class);

            floodModelParameters.setWidth(globalVariables.getColumns());
            floodModelParameters.setHeight(globalVariables.getRows());

            // Create and populate all grids
            SimpleGrid waterGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                    floodModelParameters.isToroidal(), "water");
            SimpleGrid terrainGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                    floodModelParameters.isToroidal(), "terrain");
            SimpleGrid roadsGrid = new SimpleGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                    floodModelParameters.isToroidal(), "roads");
            ComplexGrid carsGrid = new ComplexGrid(floodModelParameters.getWidth(), floodModelParameters.getHeight(),
                    floodModelParameters.isToroidal(), "cars");

            HashMap<String, ArrayList<Point>> roadHashMap = new HashMap<>();
            graph = new SingleGraph("Road Network");

            LoadWaterAndTerrainGrid.loadWaterAndTerrain(globalVariables, floodModelParameters, properties, terrainGrid,
                    waterGrid);
            LoadRoadsGrid.loadRoadsOld(floodModelParameters, globalVariables, properties, roadsGrid, roadHashMap);
            graph = LoadRoadsGrid.loadRoads(properties);

            aStar = new AStar(graph);
            grids.put("terrain", terrainGrid);
            grids.put("buildings", LoadBuildingsGrid.loadBuildings(globalVariables, floodModelParameters, properties));
            grids.put("defences", LoadDefencesGrid.loadDefences(globalVariables, floodModelParameters, properties));
            grids.put("water", waterGrid);
            grids.put("roads", roadsGrid);
            grids.put("cars", carsGrid);

            modelStateChanges = ModelStateChanges.readTimeLine(properties);
            modelState = modelStateChanges.getModelStates().get(modelStateIndex);

            if (floodModelParameters.isVisualise()) {
                visualisation = new Visualisation(this);
            }
//            Timestamp mts = new Timestamp(floodModelParameters.getTimestamp() * 1000);
            // Do an initial tick
            tick();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Logic to happen on every tick
     */
    @Override
    public void tick() {
        // FYI https://www.unixtimestamp.com/
        // increment time: time = model start time + tick time value
        if (maintainSeaLevel == null) maintainSeaLevel = floodModelParameters.getOceanDepth();
        modelTimeStamp += floodModelParameters.getTickTimeValue() * 1000;
        long timestamp = 0;
        if (modelState != null) {
//            int hours = Integer.parseInt(modelState.getTime().split(":")[0]);
//            int minutes = Integer.parseInt(modelState.getTime().split(":")[1]);
//            int seconds = Integer.parseInt(modelState.getTime().split(":")[2]);
//            timestamp = ((floodModelParameters.getTimestamp() * 1000) + (hours * 3600000L) + (minutes * 60000L) + (seconds * 1000));
            timestamp = Utilities.timeToUnixTimestamp(floodModelParameters.getTimestamp(),
                    Integer.parseInt(modelState.getTime().split(":")[0]),
                    Integer.parseInt(modelState.getTime().split(":")[1]),
                    Integer.parseInt(modelState.getTime().split(":")[2])
            );

        }
        Timestamp mts = new Timestamp(modelTimeStamp);
        SimpleGrid waterGrid = (SimpleGrid) grids.get("water");
        SimpleGrid terrainGrid = (SimpleGrid) grids.get("terrain");
        SimpleGrid defenceGrid = (SimpleGrid) grids.get("defences");

        SimpleGrid newWaterGrid = new SimpleGrid(waterGrid.getWidth(), waterGrid.getHeight(), waterGrid.isIs_toroidal(), waterGrid.getGridName());
        // Initialise new grid to be the same as the old grid.
        for (int row = 0; row < waterGrid.getHeight(); row++) {
            for (int col = 0; col < waterGrid.getWidth(); col++) {
                newWaterGrid.setCell(col, row, new Water(waterGrid.getCell(col, row).getAgent_id(),
                        ((Water) waterGrid.getCell(col, row)).getWaterLevel(), ((Water) waterGrid.getCell(col, row)).isOcean()));
            }
        }

        // reset sea level for ocean cells on every tick
        setSeaLevel(waterGrid, maintainSeaLevel);
        // TIMELINE
        // If there is a timestamp in the timeline for current time, execute the state change
        //
        if (timestamp == modelTimeStamp) {
            logger.debug("STATE CHANGE {}:", mts);
            Float stateSeaLevel = modelState.getSeaLevel();
            modelStateIndex = (modelStateIndex + 1 < modelStateChanges.getModelStates().size())?modelStateIndex + 1:modelStateIndex;
            // get sealevel change
            if (stateSeaLevel != null) {
                logger.debug("sea level change: {}", stateSeaLevel);
                setSeaLevel(waterGrid, stateSeaLevel);
                maintainSeaLevel = stateSeaLevel;
            }
            // get defence breaches
            if (modelState.getDefenceBreach() != null) {
                ArrayList<String> defences = (ArrayList<String>) modelState.getDefenceBreach();
                logger.debug("defence breach: {}", defences);

                for (int row = 0; row < floodModelParameters.getHeight(); row++) {
                    for (int col = 0; col < floodModelParameters.getWidth(); col++) {

                        if (defences != null) {
                            Defence defenceCell = (Defence) defenceGrid.getCell(col, row);
                            if (defenceCell != null) {
                                for (String defence : defences) {
                                    if (defenceCell.getName().equals(defence)) {
                                        defenceGrid.setCell(col, row, null);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }
            }
            // Cars entering the model
            if (modelState.getCars() != null) {
                for (int c = 0; c < modelState.getCars().size(); c++) {
                    Car car = modelState.getCar(c);
//                    car.setAgent_id(car.getAgentId());
                    // cars are red (for now)
                    logger.debug("Car {}, leg {} of {}", car.getAgent_id(), 0, car.getCarItinerary().size());
                    car.setStartNode(car.getCarItinerary().get(0).getStartNode());
                    car.setEndNode(car.getCarItinerary().get(0).getEndNode());
                    car.setColour(Color.red);
                    // get the shortest path to endCoordinates
                    aStar.compute(car.getStartNode(), car.getEndNode());
                    Path shortestPath = aStar.getShortestPath();
                    car.setRouteNodes(shortestPath);
                    if (shortestPath != null) {
                        logger.debug("shortest path: {}", shortestPath);
                        // add the car to the cars grid
                        // Get the xy coordinates for the normal cell grid of the starting point
                        PointInteger xy = getXY(car);
                        cars.addCar(car);
                        ((ComplexGrid) grids.get("cars")).addCell(xy.getX(), xy.getY(), car);
                        logger.debug("Car {} loaded, shortest path start: {}, end: {}", car.getAgent_id(), car.getStartNode(), car.getEndNode());
                    } else {
                        logger.debug("Car {} is stranded, no route to destination", car.getAgent_id());
                        strandedCars.addCar(car);
                        car.setColour(Color.ORANGE);
                        car.setStranded(true);
                        //TODO:
                        cars.removeCar(car);
                    }
                }

            }
        }
        moveWater(waterGrid, terrainGrid, defenceGrid, newWaterGrid);
        moveCars();
        // read the next state change
        modelState = modelStateChanges.getModelStates().get(modelStateIndex);
        if (floodModelParameters.isVisualise()) {
            visualisation.getDrawPanel().repaint();
        }
    }

    /**
     * Get current Grid XY co-ordinates from Car objects
     * @param car The car whose position is to be determined
     * @return xy position of the car on the grid
     */
    private PointInteger getXY(Car car) {
        return getXY(car, 0);
    }


    /**
     * Return Grid XY co-ordinates from Car object for position 'index' in the route
     * @param car The car whose position is to be found
     * @param index The index in the route (0 for current position)
     * @return return Grid xy-co-ordinate
     */
    private PointInteger getXY(Car car, int index) {
        // Get grid xy co-ordinates from BNG co-ordinates
        Object[] xyz = (Object[])car.getRouteNodes().getNodePath().get(index).getAttribute("xyz");
        double x = (double)xyz[0];
        double y = (double)xyz[1];

        PointInteger xy  = Utilities.BNG2GridXY(globalVariables.getLowerLeftX(),
                globalVariables.getLowerLeftY(),
                (float)x, (float)y,
                globalVariables.getCellSize());
        // flip y horizontally
        xy.setY(floodModelParameters.getHeight() - 1 - xy.getY());
        return new PointInteger(xy.getX(), xy.getY());
    }


    /**
     * Helper method to determine whether a car drowned. If the car drowned return true else return false.
     * @param car Car to check drowning status of
     */
    private boolean drownCar(Car car) {
        PointInteger xy = getXY(car);
        // Get the water agent on xy co-ordinate
        Water w = (Water)(((SimpleGrid) grids.get("water")).getCell(xy.getX(), xy.getY()));
        // If >= to vehicle drowning level then mark car as drowned, remove from list of cars add to drowned list
        if (w.getWaterLevel() >= floodModelParameters.getVehicleFloodDepth()) {
            car.setDrowned(true);
            car.setColour(new Color(73,23,12)); // change drowned car to brown
            drownedCars.addCar(car);
            //TODO:
            cars.removeCar(car);
            logger.trace("Car {} removed", car.getAgent_id());
            logger.debug("Tick Car drowned: {}", car.getAgent_id());
            return true;
        }
        return false;
    }

    /**
     * Helper method to move vehicles along shortest path
     */
    private void moveCars() {
        //int speed; // TODO: fix this to read from road files
        for (int c = 0; c < cars.getCars().size(); c++) {
            Car car = cars.getCar(c);
            if (car.getRouteNodes() == null) {
                logger.debug("Car {} entering on flooded area", car.getAgent_id());
                car.setDrowned(true);
                cars.removeCar(car);
            } else {
                // If the car hasn't drowned, move it along
                if (!drownCar(car)) {
                    Path route = car.getRouteNodes();
                    // If there is only one node left in the path the car has reached its destination
                    if (route.getNodePath().size() == 1) {
                        logger.debug("Car {} reached its destination", car.getAgent_id());

                        // Get itinerary index
                        // Get itinerary item at index
                        // Get itinerary item wait time
                        // Increment car's itinerary index
                        // Get start and end nodes for new itinerary item
                        // Set car's new start and end nodes
                        // Create timeline entry for current time plus wait time

                        car.setAtDestination(true);
                        car.setColour(Color.magenta);
                        destinationCars.addCar(car);
                        //TODO:
                        cars.removeCar(car);

                    } else {
                        // check if current position is flooded
                        PointInteger startXY = getXY(car, 0);
                        if (((Water) ((SimpleGrid) grids.get("water")).getCell(startXY.getX(),
                                startXY.getY())).getWaterLevel() >= floodModelParameters.getVehicleFloodDepth()) {
                            logger.debug("Car {} can't enter, road {} already flooded.", car.getAgent_id(), car.getRouteNodes().getNodePath().get(0).getId());
                            cars.removeCar(car);
                            car.setDrowned(true);
                        } else {

                            Node firstNode = route.getNodePath().get(0);
                            Node nextNode = route.getNodePath().get(1);
                            PointInteger cell = getXY(car, 1);
                            // Check if the car's next position has been flooded and reroute if it is
                            if (((Water) ((SimpleGrid) grids.get("water")).getCell(cell.getX(),
                                    cell.getY())).getWaterLevel() >= floodModelParameters.getVehicleFloodDepth()) {
                                logger.debug("Ouch the road is flooded, car {} reroute from {} distance {}",
                                        car.getAgent_id(),
                                        car.getRouteNodes().getNodePath().get(0),
                                        car.getCoveredDistance());

                                graph.removeNode(nextNode);
                                car.setCoveredDistance(car.getCoveredDistance() * -1);
                                aStar.compute(firstNode.getId(), car.getEndNode());
                                Path newShortestPath = aStar.getShortestPath();
                                car.setRouteNodes(newShortestPath);
                                if (newShortestPath == null) {
                                    car.setStranded(true);
                                }
                                logger.trace("Calculate new shortest path for car {}, {}", car.getAgent_id(), car.getRouteNodes());

                            } else {
                                // TODO: Check that Richard is happy with this implementation
                                // Calculate car's next position
                                int speed = (firstNode.getAttribute("speed-limit") == null) ? 0 : (int) firstNode.getAttribute("speed-limit");

                                logger.trace("speed: {} on road {}", firstNode.getAttribute("speed-limit"), firstNode.getId());
                                float nextPosition = (float) (car.getCoveredDistance() + speed);
                                // Check if there is already a car on next position
                                AtomicBoolean spaceAllocated = new AtomicBoolean(false);
                                cars.getCars().forEach(cr -> {
                                    String anotherCarID = cr.getAgent_id();
                                    String anotherCarNodeID = cr.getRouteNodes().getNodePath().get(0).getId();
                                    double cr_coveredDistance = cr.getCoveredDistance();
                                    if (anotherCarNodeID.equals(car.getRouteNodes().getNodePath().get(0).getId()) &&
                                            !anotherCarID.equals(car.getAgent_id())) {
                                        if (car.getCoveredDistance() == cr_coveredDistance) {
                                            spaceAllocated.set(true);
                                        }
                                    }
                                });
                                // If there is another car in the car's next position, don't move, wait
                                if (spaceAllocated.get()) {
                                    // We are not reducing speed we just wait for the next tick.
                                    Object[] xyz = (Object[]) car.getRouteNodes().getNodePath().get(0).getAttribute("xyz");
                                    logger.debug("Car {} is waiting at {} from {}, {}", car.getAgent_id(),
                                            car.getCoveredDistance(), xyz[0], xyz[1]);
                                } else {
                                    // Car moves
                                    car.setCoveredDistance(nextPosition);

                                    // distance between current node and next node
                                    double interDist = Utilities.distanceBetweenNodes(firstNode, nextNode);
                                    if (car.getCoveredDistance() >= interDist) {
                                        PointInteger xy = getXY(car);
                                        ((ComplexGrid) grids.get("cars")).setCell(xy.getX(), xy.getY(), null);
                                        logger.trace("Car {} reached {}", car.getAgent_id(), car.getRouteNodes().getNodePath().get(0));
                                        // remove the first node since we have now reached the next node
                                        route.getNodePath().remove(0);
                                        // set distance
                                        car.setCurrentDistance(car.getCoveredDistance() - interDist);
                                        PointInteger xy2 = getXY(car);
                                        ((ComplexGrid) grids.get("cars")).addCell(xy2.getX(), xy2.getY(), car);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    logger.debug("Cars left: {} | Cars drowned {}", cars.getCars().size(), drownedCars.getCars().size());
                }
            }
        }
    }


    /**
     * Restore ocean to level
     * @param waterGrid the water grid
     * @param level the level to which the ocean cells have to be restored
     */
    private void setSeaLevel(SimpleGrid waterGrid, float level) {
        for (int row = 0; row < floodModelParameters.getHeight(); row++) {
            for (int col = 0; col < floodModelParameters.getWidth(); col++) {
                Water w = (Water) waterGrid.getCell(col, row);
                if (w.isOcean()) w.setWaterLevel(level);
            }
        }
    }

    /**
     * Helper method to simulate water movement
     * @param water The water grid
     * @param terrain The terrain grid
     * @param defence The defences grid
     * @param newWaterGrid The new water grid after the water moved
     */
    private void moveWater(SimpleGrid water, SimpleGrid terrain, SimpleGrid defence, SimpleGrid newWaterGrid) {
        // MOVE WATER
        for (int row = 0; row < floodModelParameters.getHeight(); row++) {
            for (int col = 0; col < floodModelParameters.getWidth(); col++) {
                // Get current cell
                Water currentWaterCell = (Water) water.getCell(col, row);
                // check if the water level has risen above sea level
                // Get water height of current cell
                float waterHeight = ((Water) water.getCell(col, row)).getWaterLevel();
                // Terrain height + plus water of the current cell
                float currentCellWaterHeight = waterHeight + ((Terrain) terrain.getCell(col, row)).getElevation();
                if (waterHeight > 0) {
                    // this will become the neighbour with the lowest elevation+water
                    Water targetNeighbour = null;
                    // This will become the new water level of the current cell
                    float targetHeight = currentCellWaterHeight;
                    // this will become the position of the neighbour with the lowest elevation+water
                    Point targetPos = null;
                    // find the von Neumann neighbours
                    Point[] neighbours = water.getVNNeighborhood(col, row);
                    // find the von Neumann neighbour with the lowest elevation+water
                    for (Point pos : neighbours) {
                        // retrieve the neighbour cell
                        Water neighbour = (Water) water.getCell(pos.x, pos.y);
                        // calculate the total height of the terrain plus the water level plus the height of the defence
                        float neighbourHeight = neighbour.getWaterLevel()
                                + ((Terrain) terrain.getCell(pos.x, pos.y)).getElevation()
                                + ((defence.getCell(col, row) == null) ? 0 : ((Defence) defence.getCell(col, row)).getHeight());
                        // if the height of the neighbour is less than that off the current cell
                        if (neighbourHeight < targetHeight) {
                            targetNeighbour = neighbour;
                            targetHeight = neighbourHeight;
                            targetPos = pos;
                        }
                    }
                    // if a neighbour was found with the lowest level lower than the current cell
                    if (targetNeighbour != null) {

                        Water newCell = (Water)newWaterGrid.getCell(col, row);
                        Water newTarget = (Water)newWaterGrid.getCell(targetPos.x, targetPos.y);
                        float difference = currentCellWaterHeight - targetHeight;
                        float portion = difference < 0.0000001f ? difference : difference / 2.0f;
                        float newHeight = newCell.getWaterLevel() - portion;
                        float newTargetHeight = newTarget.getWaterLevel() + portion;

                        if (!newCell.isOcean())
                            newCell.setWaterLevel(newHeight);
                        newTarget.setWaterLevel(newTargetHeight);
                    } else {
                        // if none of the neighbours were lower and the cell does not yet exist in the new grid
                        // create it
                        if (newWaterGrid.getCell(col, row) == null) {
                            newWaterGrid.setCell(col, row, new Water(currentWaterCell.getAgent_id(),
                                    currentWaterCell.getWaterLevel(), currentWaterCell.isOcean()));
                        }
                    }
                } else {
                    // if there is no water on the cell so just create it in the new grid
                    if (newWaterGrid.getCell(col, row) == null) {
                        newWaterGrid.setCell(col, row, new Water(currentWaterCell.getAgent_id(),
                                currentWaterCell.getWaterLevel(), currentWaterCell.isOcean()));
                    }
                }
                Water newWaterCell = (Water) newWaterGrid.getCell(col, row);
                // Set color based on water is in ocean or not
                if (newWaterCell.getWaterLevel() > 0) {
                    if (newWaterCell.isOcean()) {
                        newWaterCell.setColour(new Color(0x00, 0x00, 0xff));
                    } else {
                        newWaterCell.setColour(new Color(0x11, 0x55, 0xff));
                    }
                }
            }
        }
        grids.put("water", newWaterGrid);

        // Generate a one pixel per cell PNG image on tick
        if (floodModelParameters.isPngOnTick()) {
            ((SimpleGrid) grids.get("water")).createPNG(properties.getProperty("OUTPUT_DATA"), "water_" + modelTimeStamp);
            ((SimpleGrid) grids.get("terrain")).createPNG(properties.getProperty("OUTPUT_DATA"), "terrain_" + modelTimeStamp);
            ((SimpleGrid) grids.get("defences")).createPNG(properties.getProperty("OUTPUT_DATA"), "defences_" + modelTimeStamp);
        }
    }



    /**
     * Default constructor
     */
    public Firm2() {
        super(new FloodModelParameters());
        floodModelParameters = (FloodModelParameters) modelParameters;
        // load properties file or create one if it doesn't exist and add default values
        properties = createPropertiesFile();
        floodModelParameters.setToroidal(Boolean.parseBoolean(properties.getProperty("TOROIDAL")));
        floodModelParameters.setTicks(Integer.parseInt(properties.getProperty("TICKS")));
        floodModelParameters.setVisualise(Boolean.parseBoolean(properties.getProperty("VISUALISE")));
        floodModelParameters.setCell_size(Integer.parseInt(properties.getProperty("CELL_SIZE")));
        floodModelParameters.setChance(Integer.parseInt(properties.getProperty("CHANCE")));
        floodModelParameters.setTitle(String.valueOf(properties.get("APPLICATION_TITLE")));
        floodModelParameters.setOceanDepth(Float.parseFloat(properties.getProperty("OCEAN_DEPTH")));
        floodModelParameters.setTimestamp(Long.parseLong(properties.getProperty("TIME_STAMP")));
        floodModelParameters.setTickTimeValue(Long.parseLong(properties.getProperty("TICK_TIME_VALUE")));
        modelTimeStamp = floodModelParameters.getTimestamp() * 1000; // start time for model
        floodModelParameters.setSlowdown(Integer.parseInt(properties.getProperty("SLOWDOWN")));
        floodModelParameters.setTitle(properties.getProperty("APPLICATION_TITLE"));
        floodModelParameters.setPngOnTick(Boolean.parseBoolean(properties.getProperty("PNG_ON_TICK")));
        floodModelParameters.setRunOnStartUp(Boolean.parseBoolean(properties.getProperty("RUN_ON_STARTUP")));
        floodModelParameters.setVehicleFloodDepth(Float.parseFloat(properties.getProperty("VEHICLE_FLOOD_DEPTH")));
        modelInit();
        Thread modelthread = new Thread(this);
        setRun(floodModelParameters.isRunOnStartUp()); // don't start running on program startup
        modelthread.start();
    }



}