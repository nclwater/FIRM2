package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleState implements iModelState {

    @SerializedName("time")
    @Expose
    String time;

    @SerializedName("code")
    @Expose
    int code;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("start-position")
    @Expose
    PointDouble startPosition;

    @SerializedName("end-position")
    @Expose
    PointDouble endPosition;

    public VehicleState(String time, int code, String description, PointDouble startPosition, PointDouble endPosition) {
        this.time = time;
        this.code = code;
        this.description = description;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PointDouble getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(PointDouble startPosition) {
        this.startPosition = startPosition;
    }

    public PointDouble getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(PointDouble endPosition) {
        this.endPosition = endPosition;
    }

    @Override
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String getTime() {
        return time;
    }
}
