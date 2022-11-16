package Pieces;
import java.util.ArrayList;

public abstract class Piece {
    public int suit; // 0 - empty, 1 - lowercase, 2 - uppercase
    public int numMoves = 0;
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";
    
    // constructors
    public Piece (int suit){
        this.suit = suit;
    } // end constructor

    public Piece(){
        this.suit = 0;
    } // end constructor

    // methods
    public ArrayList<Coords> checkMove(Piece[][] board, int pieceRow, int pieceCol){
        ArrayList<Coords> empty = new ArrayList<>();
        return empty;
    } // end checkMove
    
    // getters
    public char getChar(){
        return '!';
    } // end getChar

    public int getSuit(){
        return this.suit;
    } // end getSuit

    public int getNumMoves(){
        return numMoves;
    } // end getNumMoves
} // end class