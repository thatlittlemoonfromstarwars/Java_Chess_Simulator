package Pieces;
import java.util.ArrayList;

public class Knight extends Piece {
    
    public Knight(int suit){
        super(suit);
    } // end constructor
    
    // methods
    public ArrayList<Coords> checkMove(Piece[][] board, int pieceRow, int pieceCol){
        ArrayList<Coords> legalMoves = new ArrayList<>();

        // use the try method for every check to ensure no errors
        int tempRow = pieceCol;
        int tempCol = pieceRow;
        for(int i = 0; i < 8; i++){
            switch(i){
                case(0):
                tempRow = pieceRow + 1;
                tempCol = pieceCol - 2;
                break;

                case(1):
                tempRow = pieceRow + 2;
                tempCol = pieceCol - 1;
                break;

                case(2):
                tempRow = pieceRow + 2;
                tempCol = pieceCol + 1;
                break;

                case(3):
                tempRow = pieceRow + 1;
                tempCol = pieceCol + 2;
                break;

                case(4):
                tempRow = pieceRow - 1;
                tempCol = pieceCol + 2;
                break;

                case(5):
                tempRow = pieceRow - 2;
                tempCol = pieceCol + 1;
                break;

                case(6):
                tempRow = pieceRow - 2;
                tempCol = pieceCol - 1;
                break;

                case(7):
                tempRow = pieceRow - 1;
                tempCol = pieceCol - 2;
                break;
            }
            try{
                if(board[tempRow][tempCol].suit != suit){
                    legalMoves.add(new Coords(tempRow, tempCol));
                }
            } catch(ArrayIndexOutOfBoundsException E) {}
        }
        return legalMoves;
    } // end checkMove
    
    // getters
    public char getChar(){
        if(suit == 2){
            return 'N';
        } else if(suit == 1){
            return 'n';
        }
        return '!';
    } // getChar
} // end class