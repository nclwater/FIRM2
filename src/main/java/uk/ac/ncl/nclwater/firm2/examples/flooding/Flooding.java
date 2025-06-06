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


package uk.ac.ncl.nclwater.firm2.examples.flooding;

import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.Model;
import uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework.SimpleGrid;

public class Flooding extends Model {
    @Override
    public void modelInit() {
        this.grids.put("water", new SimpleGrid(modelParameters.getWidth(), modelParameters.getHeight(), modelParameters.isToroidal(), "water"));

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
