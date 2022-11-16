package Pieces;
import java.util.ArrayList;

public class Queen extends Piece {
    
    public Queen(int suit){
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
        
        // check diagonal movement
        for(int j = 0; j < 4; j++){
            int i = 1;
            scanLoop:
            while(true){
                int tempRow = 0;
                int tempCol = 0;
                switch(j){
                    case(0): // up and right
                    tempRow = pieceRow + i;
                    tempCol = pieceCol + i;
                    break;

                    case(1): // up and left
                    tempRow = pieceRow + i;
                    tempCol = pieceCol - i;
                    break;

                    case(2): // down and right
                    tempRow = pieceRow - i;
                    tempCol = pieceCol + i;
                    break;

                    case(3): // down and left
                    tempRow = pieceRow - i;
                    tempCol = pieceCol - i;
                    break;
                }

                if(tempRow >= 8 || tempRow < 0 || tempCol >= 8 || tempCol < 0) break scanLoop;
                if(board[tempRow][tempCol] instanceof Empty){
                    legalMoves.add(new Coords(tempRow, tempCol));

                } else if(board[tempRow][tempCol].suit != suit){
                    legalMoves.add(new Coords(tempRow, tempCol));
                    break scanLoop;

                } else {
                    break scanLoop;
                }
                i++;
            }
        }
        return legalMoves;
    } // end checkMove

    // getters
    public char getChar(){
        if(suit == 2){
            return 'Q';
        } else if(suit == 1){
            return 'q';
        }
        return '!';
    } // end getChar
} // end class