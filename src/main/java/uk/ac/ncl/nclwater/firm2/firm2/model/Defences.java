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

public class Defences {
    @SerializedName("defences")
    @Expose
    private List<Defence> defences = new ArrayList<>();

    public List<Defence> getDefences() {
        return defences;
    }

    public void setDefences(List<Defence> defences) {
        this.defences = defences;
    }

    public void add(Defence defence) {
        defences.add(defence);
    }
}
