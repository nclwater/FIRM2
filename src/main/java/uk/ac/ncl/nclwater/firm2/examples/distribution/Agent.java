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


package uk.ac.ncl.nclwater.firm2.examples.distribution;

import uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Agent {
    private int id;
    private long time;

    public Agent(int id, long time) {
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", time=" + Utilities.unixTimestampToDateString(time, "yyyy-MM-dd HH:mm:ss") + ", " + time +
                '}';
    }
}
