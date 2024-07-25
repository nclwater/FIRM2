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
