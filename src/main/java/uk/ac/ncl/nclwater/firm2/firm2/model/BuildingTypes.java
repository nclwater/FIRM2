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
import java.util.ArrayList;
import java.util.List;

public class BuildingTypes {
    @SerializedName("building-type-list")
    @Expose
    private List<BuildingType> buildingTypeList = new ArrayList<>();

    public BuildingTypes() {
        this.buildingTypeList = buildingTypeList;
    }

    public List<BuildingType> getBuildingTypeList() {
        return buildingTypeList;
    }

    public void setBuildingTypeList(List<BuildingType> buildingTypeList) {
        this.buildingTypeList = buildingTypeList;
    }

    public void add(BuildingType buildingType) {
        buildingTypeList.add(buildingType);
    }

    /**
     * Find building type by description and return the code
     * -999 means the code was not found
     * @param typeDescription
     * @return the code of the type of building. -999 means it was not found
     */
    public int findBuildingType(String typeDescription) {
        for (BuildingType type:buildingTypeList) {
            if (type.getTypeDescription().equals(typeDescription))
                return type.getTypeCode();
        }
        return -999;
    }
}
