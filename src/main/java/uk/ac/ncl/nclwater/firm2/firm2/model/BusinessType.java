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

public class BusinessType {
    @SerializedName("business-type-code")
    @Expose
    int businessTypeCode;
    @SerializedName("business-type")
    @Expose
    String businessType;

    public BusinessType(int businessTypeCode, String businessType) {
        this.businessTypeCode = businessTypeCode;
        this.businessType = businessType;
    }

    public int getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(int businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
