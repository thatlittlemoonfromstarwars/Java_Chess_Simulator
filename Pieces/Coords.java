package Pieces;
public class Coords {
    private int row;
    private int col;
    
    public Coords(int newRow, int newCol){
        this.row = newRow;
        this.col = newCol;
    } // end constructor

    public int getRow(){
        return this.row;
    } // end getRow

    public int getCol(){
        return this.col;
    } // end getCol
} // end class