package Pieces;
import java.util.ArrayList;

public class King extends Piece {
    
    public King(int suit){
        super(suit);
    } // end constructor
    
    // methods
    public ArrayList<Coords> checkMove(Piece[][] board, int pieceRow, int pieceCol){
        ArrayList<Coords> legalMoves = new ArrayList<>();

        // find the starting value to scan (accounts for edge cases)
        int startRowScan;
        if(pieceRow == 0){
            startRowScan = 0;
        } else {
            startRowScan = pieceRow - 1;
        }
        int endRowScan;
        if(pieceRow == 7){
            endRowScan = 7;
        } else {
            endRowScan = pieceRow + 1;
        }

        int startColScan;
        if(pieceCol == 0){
            startColScan = 0;
        } else {
            startColScan = pieceCol - 1;
        }
        int endColScan;
        if(pieceCol == 7){
            endColScan = 7;
        } else {
            endColScan = pieceCol + 1;
        }

        // scans through possible moves and identifies them as legal or not
        for(int i = startRowScan; i <= endRowScan && i < 8; i++){
            for(int j = startColScan; j <= endColScan && j < 8; j++){
                if(board[i][j].suit != suit){
                    legalMoves.add(new Coords(i, j));
                }
            }
        }
        return legalMoves;
    } // end checkMove

    // getters
    public char getChar(){
        if(suit == 2){
            return 'K';
        } else if(suit == 1){
            return 'k';
        }
        return '!';
    } // end getChar
} // end class