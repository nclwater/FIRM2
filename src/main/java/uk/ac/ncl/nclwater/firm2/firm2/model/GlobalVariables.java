package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalVariables {

    /**
     * The number of columns (cells on the x-axis)
     */
    @Expose
    @SerializedName("columns")
    private int columns;
    /**
     * The number of rows (cells on the y-axis
     */
    @Expose
    @SerializedName("rows")
    private int rows;
    /**
     * The ordinance survey x co-ordinate of the bottom left corner of the map
     */
    @SerializedName("LowerLeftX")
    @Expose
    private float lowerLeftX;
    /**
     * The ordinance survey y co-ordinate of the bottom left corner of the map
     */
    @SerializedName("LowerLeftY")
    @Expose
    private float lowerLeftY;
    @SerializedName("CellSize")
    @Expose
    private int cellSize;

    /**
     * Constructor
     * @param columns
     * @param rows
     * @param lowerLeftX
     * @param lowerLeftY
     * @param cellSize
     */
    public GlobalVariables(int columns, int rows, float lowerLeftX, float lowerLeftY, int cellSize) {
        this.columns = columns;
        this.rows = rows;
        this.lowerLeftX = lowerLeftX;
        this.lowerLeftY = lowerLeftY;
        this.cellSize = cellSize;
    }

    /**
     * Return the number of columns (cells on x-axis) in the model
     * @return
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Set the number of  columns (cells on x-axis) in the model
     * @param columns
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Return the number of rows (cells on x-axis) in the model
     * @return
     */
    public int getRows() {
        return rows;
    }

    /**
     * Set the number of  rows (cells on x-axis) in the model
     * @param rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    public float getLowerLeftX() {
        return lowerLeftX;
    }

    public void setLowerLeftX(float lowerLeftX) {
        this.lowerLeftX = lowerLeftX;
    }

    public float getLowerLeftY() {
        return lowerLeftY;
    }

    public void setLowerLeftY(float lowerLeftY) {
        this.lowerLeftY = lowerLeftY;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public String asString() {
        return columns + "\n" + rows + "\n" + lowerLeftX + "\n" + lowerLeftY + "\n" + cellSize;
    }
}
