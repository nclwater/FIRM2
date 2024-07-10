package uk.ac.ncl.nclwater.firm2.examples.flooding;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Model;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Grid;

public class Flooding extends Model {
    @Override
    public void modelInit() {
        this.grids.put("water", new Grid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal(), "water"));

    }

    /**
     * Constructor
     */
    public Flooding() {
        modelParameters.setWidth(1000);
        modelParameters.setHeight(1000);
        modelParameters.setToroidal(false);
        modelParameters.setTicks(0);
        modelParameters.setVisualise(true);
        modelParameters.setCell_size(3);
        modelParameters.setChance(50);
        modelParameters.setTitle("Flooding");
        modelInit();
    }

    @Override
    public void tick() {

    }
}
