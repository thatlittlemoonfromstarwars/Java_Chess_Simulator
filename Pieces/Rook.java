package Pieces;
import java.util.ArrayList;

public class Rook extends Piece {
    
    public Rook(int suit){
        super(suit);
    } // end constructor

    // methods
    public ArrayList<Coords> checkMove(Piece[][] board, int pieceRow, int pieceCol){
        ArrayList<Coords> legalMoves = new ArrayList<>();
                
        // check grid movement
        scanUp:
        for(int i = pieceRow + 1; i < 8; i++){
            if(board[i][pieceCol] instanceof Empty){
                legalMoves.add(new Coords(i, pieceCol));

            } else if(board[i][pieceCol].suit != suit){
                legalMoves.add(new Coords(i, pieceCol));
                break scanUp;

            } else {
                break scanUp;
            }
        }

        scanDown:
        for(int i = pieceRow - 1; i >= 0; i--){
            if(board[i][pieceCol] instanceof Empty){
                legalMoves.add(new Coords(i, pieceCol));

            } else if(board[i][pieceCol].suit != suit){
                legalMoves.add(new Coords(i, pieceCol));
                break scanDown;

            } else {
                break scanDown;
            }
        }

        scanRight:
        for(int i = pieceCol + 1; i < 8; i++){
            if(board[pieceRow][i] instanceof Empty){
                legalMoves.add(new Coords(pieceRow, i));

            } else if(board[pieceRow][i].suit != this.suit){
                legalMoves.add(new Coords(pieceRow, i));
                break scanRight;

            } else {
                break scanRight;
            }
        }

        scanLeft:
        for(int i = pieceCol - 1; i >= 0; i--){
            if(board[pieceRow][i] instanceof Empty){
                legalMoves.add(new Coords(pieceRow, i));

            } else if(board[pieceRow][i].suit != this.suit){
                legalMoves.add(new Coords(pieceRow, i));
                break scanLeft;

            } else {
                break scanLeft;
            }
        }
        return legalMoves;
    } // end checkMove
    
    // getters
    public char getChar(){
        if(suit == 2){
            return 'R';
        } else if(suit == 1){
            return 'r';
        }
        return '!';
    } // end getChar
} // end class