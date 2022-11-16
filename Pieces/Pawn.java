package Pieces;
import java.util.ArrayList;

public class Pawn extends Piece{
    
    public Pawn(int suit){
        super(suit);
    } // end constructor

    public ArrayList<Coords> checkMove(Piece[][] board, int pieceRow, int pieceCol){
        ArrayList<Coords> legalMoves = new ArrayList<>();

        if(suit == 1){ // starts at row 6
            // checks space in front of pawn
            if(board[pieceRow - 1][pieceCol] instanceof Empty){
                legalMoves.add(new Coords(pieceRow - 1, pieceCol));
                
                // checks if pawn can move two rows
                if(pieceRow == 6 && board[pieceRow - 2][pieceCol] instanceof Empty){
                    legalMoves.add(new Coords(pieceRow - 2, pieceCol));
                }
            }

            // check left
            if(pieceCol > 0 && pieceRow != 0){
                if(board[pieceRow - 1][pieceCol - 1].suit == 2){
                    legalMoves.add(new Coords(pieceRow - 1, pieceCol - 1));
                }
            }

            // check right
            if(pieceCol < 7 && pieceRow != 0){
                if(board[pieceRow - 1][pieceCol + 1].suit == 2){
                    legalMoves.add(new Coords(pieceRow - 1, pieceCol + 1));
                }
            }
        } else { // suit 2, starts at row 1
            // checks space in front of pawn
            if(board[pieceRow + 1][pieceCol] instanceof Empty){
                legalMoves.add(new Coords(pieceRow + 1, pieceCol));
                
                // checks if pawn can move two rows
                if(pieceRow == 1 && board[pieceRow + 2][pieceCol] instanceof Empty){
                    legalMoves.add(new Coords(pieceRow + 2, pieceCol));
                }
            }

            // check left
            if(pieceCol > 0 && pieceRow != 7){
                if(board[pieceRow + 1][pieceCol - 1].suit == 1){
                    legalMoves.add(new Coords(pieceRow + 1, pieceCol - 1));
                }
            }

            // check right
            if(pieceCol < 7 && pieceRow != 7){
                if(board[pieceRow + 1][pieceCol + 1].suit == 1){
                    legalMoves.add(new Coords(pieceRow + 1, pieceCol + 1));
                }
            }
        }
        return legalMoves;
    } // end checkMove
    
    // getters
    public char getChar(){
        if(suit == 2){
            return 'P';
        } else if(suit == 1){
            return 'p';
        }
        return '!';
    } // end getChar
} // end class