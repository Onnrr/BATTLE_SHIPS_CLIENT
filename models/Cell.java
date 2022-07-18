package models;

import javafx.scene.control.Button;

/**
 * Extended class of javafx button
 * Has extra properties row and column as integers and isOccupied as boolean
 * Also getter and setter methods for these
 */
public class Cell extends Button {
    int row;
    int column;
    boolean occupied;

    /**
     * Initialises cell's properties
     * Cell is not occupied when first created
     * 
     * @param row    row value of cell to be created
     * @param column cloumn value of cell to be created
     */
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        occupied = false;
    }

    /**
     * Getter for row
     * 
     * @return row property of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for column
     * 
     * @return column propoperty
     */
    public int getColumn() {
        return column;
    }

    /**
     * Setter for occupied value
     * 
     * @param b boolean value to set the occupied accordingly
     */
    public void setOccupied(boolean b) {
        occupied = b;
    }

    /**
     * Getter for occupied
     * 
     * @return true if the cell is occupied
     */
    public boolean isOccupied() {
        return occupied;
    }
}
