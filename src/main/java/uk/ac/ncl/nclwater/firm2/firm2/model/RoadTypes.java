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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RoadTypes {
    private static final Logger logger = LoggerFactory.getLogger(RoadTypes.class);

    @Expose
    @SerializedName("road-types")
    ArrayList<RoadType> roadTypes = new ArrayList<>();
    HashMap<String, Integer> roadTypeMap = new HashMap<>();

    public RoadTypes() {
        roadTypes.add(new RoadType("dc", "Dual Carriageway", 60));
        roadTypes.add(new RoadType("sc", "Single Carriageway", 30));
        roadTypes.add(new RoadType("tilj", "Traffic Island Link At Junction", 30));
        roadTypes.add(new RoadType("etal", "Enclosed Traffic Area Link", 30));
        roadTypes.add(new RoadType("sr", "Slip Road", 30));
        roadTypes.add(new RoadType("r", "Roundabout", 30));
        roadTypes.add(new RoadType("til", "Traffic Island Link", 30));
        roadTypes.forEach(rt -> {
            roadTypeMap.put(rt.getRoadType(), rt.getSpeedLimit());
        });
//        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
//        String filename = "DATA/inputs/road_types.json";
//        FileWriter fileWriter1 = null;
//        try {
//            fileWriter1 = new FileWriter(filename);
//            gson.toJson(roadTypes, fileWriter1);
//            fileWriter1.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public ArrayList<RoadType> getRoadTypes() {
        return roadTypes;
    }

    public void setRoadTypes(ArrayList<RoadType> roadTypes) {
        this.roadTypes = roadTypes;
    }

    public int getSpeed(String roadType) {

        return roadTypeMap.get(roadType);
    }
}
