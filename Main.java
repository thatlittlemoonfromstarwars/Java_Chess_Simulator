// v 1.3
import java.util.ArrayList;
import Pieces.Piece;
import Pieces.Empty;
import Pieces.Coords;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Queen;
import Pieces.Rook;

class Main {
    public static Piece[][] board = new Piece[8][8]; // [row][column] - 2D Array storing all pieces on the board
    public static boolean turn = true; // false - lowercase, true - uppercase (uppercase goes first)
    public static int[] upPieces = new int[5];  // these arrays store how many pieces are still left on the board
    public static int[] lowPieces = new int[5]; // 0-queen, 1-bishop, 2-knight, 3-rook, 4-pawn
    public static boolean[] enPassantPossible = new boolean[8]; // makes sure that en passant can only happen the move after a pawn is moved
    public static void main(String[] args) {
        while(true){ // infinite loop
            
            setup();
            // testSetup();
            
            gameLoop:
            while(true){
                int currentSuit = 0;

                if(turn){
                    System.out.println("   ---------Uppercase--------");
                    currentSuit = 2;
                } else {
                    System.out.println("   ---------Lowercase--------");
                    currentSuit = 1;
                }
                updateScore(board);
                printBoard();

                int pieceRow;
                int pieceCol;
                int tarRow;
                int tarCol;

                switch(checkForCheck(board, turn)){
                    case(1):
                    // in check
                    System.out.println("You're in check!");
                    break;

                    case(2):
                    // checkmate
                    System.out.println("You lost!");
                    break gameLoop;
                    
                }

                turnLoop:
                while(true){ // to reselect piece to move
                    while(true){ // piece selection loop
                        System.out.println();
                        System.out.print("Piece to move(ex. A1):");
                        String pieceToMove = Library.wordscan.nextLine().toUpperCase();
                        if(pieceToMove.equals("RESIGN")) break gameLoop;
                        if(pieceToMove.length() == 0){
                            System.out.println("Please select a piece.");
                            continue;
                        }
                        pieceCol = findCol(pieceToMove.charAt(0));
                        pieceRow = findRow(pieceToMove.charAt(1));

                        if(pieceCol == 2 && pieceRow == 20){ // castle right
                            if(castle(board, turn, true)){
                                break turnLoop;
                            } else {
                                System.out.println("You cannot castle right now.");
                            }
                            
                        } else if(pieceCol == 2 && pieceRow == 10){ // castle left
                            if(castle(board, turn, false)){
                                break turnLoop;
                            } else {
                                System.out.println("You cannot castle right now.");
                            }
                            
                        } else if(pieceRow == -1 || pieceCol == -1 || pieceRow >= 8){
                            System.out.println("That is an invalid space. Please try again.");

                        } else if(board[pieceRow][pieceCol] instanceof Empty){
                            System.out.println("Please select a piece.");

                        } else if((board[pieceRow][pieceCol].getSuit() == 1 && !turn) || (board[pieceRow][pieceCol].getSuit() == 2 && turn)){
                            break;

                        } else {
                            System.out.println("That is not a piece of your suit. Please try again.");
                        }
                    } // end piece selection loop

                    movePiece:
                    while(true){
                        tarSelect:
                        while(true){
                            System.out.print("Target space(type \"back\" to go back):");
                            String targetSpace = Library.wordscan.nextLine().toUpperCase();
                            if(targetSpace.equalsIgnoreCase("back")) break movePiece;
                            if(targetSpace.length() == 0){
                                System.out.println( "That is an invalid space. Please try again.");
                                continue;
                            }

                            tarCol = findCol(targetSpace.charAt(0));
                            tarRow = findRow(targetSpace.charAt(1));

                            if(tarRow == -1 || tarCol == -1 || board[tarRow][tarCol].suit == currentSuit){
                                System.out.println( "That is an invalid space. Please try again.");
                            } else {
                                break tarSelect;
                            }
                        }
                        
                        ArrayList<Pieces.Coords> allLegalMoves = new ArrayList<>();
                        allLegalMoves.addAll(board[pieceRow][pieceCol].checkMove(board, pieceRow, pieceCol));

                        // checks for check by trying every legal move, and seeing if it puts the player in check
                        // then removes moves that put the player in check from the allLegalMoves arraylist
                        Piece[][] tempBoard = new Piece[8][8];
                        for(int i = 0; i < allLegalMoves.size(); i++){
                            for(int j = 0; j < 8; j++){
                                for(int k = 0; k < 8; k++){
                                    tempBoard[j][k] = board[j][k];
                                }
                            }
                            tempBoard[allLegalMoves.get(i).getRow()][allLegalMoves.get(i).getCol()] = tempBoard[pieceRow][pieceCol];
                            tempBoard[pieceRow][pieceCol] = new Empty();

                            int checkForCheckValue = checkForCheck(tempBoard, turn);
                            if(checkForCheckValue == 1 || checkForCheckValue == 2){
                                allLegalMoves.remove(i);
                                i--;
                            }
                        }
                        // check for en passant
                        if(board[pieceRow][pieceCol] instanceof Pawn && checkEnPassant(board, turn, pieceRow, pieceCol, tarRow, tarCol) && enPassantPossible[tarCol]){
                            // move the pawn and take opponent pawn
                            board[tarRow][tarCol] = board[pieceRow][pieceCol];
                            board[pieceRow][pieceCol] = new Empty();
                            if(turn){
                                board[tarRow - 1][tarCol] = new Empty();
                            } else {
                                board[tarRow + 1][tarCol] = new Empty();
                            }
                            // reset enPassantPossible to make sure en passant is only able to happen the move after a player moves their pawn
                            for(int i = 0; i < 8; i++){
                                enPassantPossible[i] = false;
                            }
                            break turnLoop;
                        }
                        // reset enPassantPossible to make sure en passant is only able to happen the move after a player moves their pawn
                        for(int i = 0; i < 8; i++){
                            enPassantPossible[i] = false;
                        }

                        for(int i = 0; i < allLegalMoves.size(); i++){
                            if(allLegalMoves.get(i).getRow() == tarRow && allLegalMoves.get(i).getCol() == tarCol){
                                // move piece
                                board[pieceRow][pieceCol].numMoves++;
                                
                                if(turn){
                                    if(pieceRow == 1 && tarRow == 3 && board[pieceRow][pieceCol] instanceof Pawn){
                                        enPassantPossible[pieceCol] = true;
                                    }
                                    // if pawn is in last row, choose a piece to promote to
                                    // if not, just move the piece
                                    if(tarRow == 7 && board[pieceRow][pieceCol] instanceof Pawn){
                                        board[tarRow][tarCol] = newPieceSelect(2);
                                    } else {
                                        board[tarRow][tarCol] = board[pieceRow][pieceCol];
                                    }
                                } else {
                                    if(pieceRow == 6 && tarRow == 4 && board[pieceRow][pieceCol] instanceof Pawn){
                                        enPassantPossible[pieceCol] = true;
                                    }
                                    if(tarRow == 0 && board[pieceRow][pieceCol] instanceof Pawn){
                                        board[tarRow][tarCol] = newPieceSelect(1);
                                    } else {
                                        board[tarRow][tarCol] = board[pieceRow][pieceCol];
                                    }
                                }
                                
                                
                                // make the previous space empty
                                board[pieceRow][pieceCol] = new Empty();
                                break turnLoop;
                            }
                        }
                        System.out.println("Illegal move. Please try again");
                    }
                } // end turnLoop

                // end of turn
                turn = !turn;
                System.out.println();

            } // end game loop

            // checks if player would like to play again
            System.out.println("Play again? (y/n)");
            char playAgain = Library.wordscan.nextLine().toLowerCase().charAt(0);
            if(playAgain == 'n'){
                return;
            }
            turn = true;
        } // end infinite loop

    } // end main

    public static int checkForCheck(Piece[][] board, boolean turn){
        // returns 0 if not in check, 1 if in check, and 2 if in checkmate
        int suit;
        if(turn){
            suit = 2;
        } else {
            suit = 1;
        }

        // find same suit king position
        int kingRow = -1;
        int kingCol = -1;
        rowScan:
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j].suit == suit){
                    if(board[i][j] instanceof King){
                        kingRow = i;
                        kingCol = j;
                        break rowScan;
                    }
                }
            }
        }
        
        if(inCheck(board, turn, kingRow, kingCol)){
            // check for checkmate
            ArrayList<Coords> allLegalMoves = new ArrayList<>();
            
            // scan all pieces of same suit on board and add their legal moves to an arraylist
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(board[i][j].suit == suit){
                        ArrayList<Coords> tempLegalMoves = new ArrayList<>();
                        tempLegalMoves.addAll(board[i][j].checkMove(board, i, j));
                        Piece[][] tempBoard = new Piece[8][8];
                        // cycle through the legal moves of each piece and see if it's in check
                        for(int k = 0; k < tempLegalMoves.size(); k++){
                            for(int l = 0; l < 8; l++){
                                for(int m = 0; m < 8; m++){
                                    // creates a new board to test moves on
                                    tempBoard[l][m] = board[l][m];
                                }
                            }
                            // move piece on temporary board
                            tempBoard[tempLegalMoves.get(k).getRow()][tempLegalMoves.get(k).getCol()] = tempBoard[i][j];
                            tempBoard[i][j] = new Empty();
                            // finds same suit king position to use in inCheck()
                            int tempKingRow = -1;
                            int tempKingCol = -1;
                            kingScan:
                            for(int l = 0; l < 8; l++){
                                for(int m = 0; m < 8; m++){
                                    if(tempBoard[l][m].suit == suit){
                                        if(tempBoard[l][m] instanceof King){
                                            tempKingRow = l;
                                            tempKingCol = m;
                                            break kingScan;
                                        }
                                    }
                                }
                            }
                            // into check, but only for some moves
                            if(inCheck(tempBoard, turn, tempKingRow, tempKingCol)){
                                // remove moves that put player in check
                                tempLegalMoves.remove(k);
                                k--;
                            }
                        }
                        allLegalMoves.addAll(tempLegalMoves);
                    }
                }
            }
            if(allLegalMoves.isEmpty()){
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    } // end checkForCheck()

    public static boolean inCheck(Piece[][] board, boolean turn, int kingRow, int kingCol){
        int suit;
        if(turn){
            suit = 2;
        } else {
            suit = 1;
        }

        // check if pawn can take king
        if(turn){
            // capital king
            try{
                if(board[kingRow + 1][kingCol - 1] instanceof Pawn && board[kingRow + 1][kingCol - 1].suit == 1){
                    return true;
                }
            } catch(ArrayIndexOutOfBoundsException E){}
            try{
                if(board[kingRow + 1][kingCol + 1] instanceof Pawn && board[kingRow + 1][kingCol + 1].suit == 1){
                    return true;
                }
            } catch(ArrayIndexOutOfBoundsException E){}
        } else {
            // lowercase king
            try{
                if(board[kingRow - 1][kingCol - 1] instanceof Pawn && board[kingRow - 1][kingCol - 1].suit == 2){
                    return true;
                }
            } catch(ArrayIndexOutOfBoundsException E){}
            try{
                if(board[kingRow - 1][kingCol + 1] instanceof Pawn && board[kingRow - 1][kingCol + 1].suit == 2){
                    return true;
                }
            } catch(ArrayIndexOutOfBoundsException E){}
        }

        // check if knight can take the king
        int tempRow = kingRow;
        int tempCol = kingCol;
        for(int i = 0; i < 8; i++){
            switch(i){
                case(0):
                tempRow = kingRow + 1;
                tempCol = kingCol - 2;
                break;

                case(1):
                tempRow = kingRow + 2;
                tempCol = kingCol - 1;
                break;

                case(2):
                tempRow = kingRow + 2;
                tempCol = kingCol + 1;
                break;

                case(3):
                tempRow = kingRow + 1;
                tempCol = kingCol + 2;
                break;

                case(4):
                tempRow = kingRow - 1;
                tempCol = kingCol + 2;
                break;

                case(5):
                tempRow = kingRow - 2;
                tempCol = kingCol + 1;
                break;

                case(6):
                tempRow = kingRow - 2;
                tempCol = kingCol - 1;
                break;

                case(7):
                tempRow = kingRow - 1;
                tempCol = kingCol - 2;
                break;
            }
            try{
                if(board[tempRow][tempCol] instanceof Knight && board[tempRow][tempCol].suit != suit){
                    return true;
                }
            } catch(ArrayIndexOutOfBoundsException E) {}
        }
        
        // **NOTE this only works if there are 2 kings in the game at once
        // if one side doesn't have a king it breaks the program

        // check if opposite king can take king
        int tempTarRow = 0;
        int tempTarCol = 0;
        for(int i = 0; i < 8; i++){
            switch(i){
                case(0):
                tempTarRow = kingRow - 1;
                tempTarCol = kingCol - 1;
                break;
                
                case(1):
                tempTarRow = kingRow - 1;
                tempTarCol = kingCol;
                break;

                case(2):
                tempTarRow = kingRow - 1;
                tempTarCol = kingCol + 1;
                break;

                case(3):
                tempTarRow = kingRow;
                tempTarCol = kingCol - 1;
                break;

                case(4):
                tempTarRow = kingRow;
                tempTarCol = kingCol + 1;
                break;

                case(5):
                tempTarRow = kingRow + 1;
                tempTarCol = kingCol - 1;
                break;

                case(6):
                tempTarRow = kingRow + 1;
                tempTarCol = kingCol;
                break;

                case(7):
                tempTarRow = kingRow + 1;
                tempTarCol = kingCol + 1;
                break;
            }
            
            try {
                if(board[tempTarRow][tempTarCol] instanceof King && board[tempTarRow][tempTarCol].suit != suit){
                    return true;
                }
            } catch (IndexOutOfBoundsException E) {}
        }

        // check if rook or queen can take the king
        scanUp:
        for(int i = kingRow + 1; i < 8; i++){
            try{
                if((board[i][kingCol] instanceof Rook || board[i][kingCol] instanceof Queen) && board[i][kingCol].suit != suit){
                    return true;
                } else if(!(board[i][kingCol] instanceof Empty)){
                    break scanUp;
                }
            } catch(ArrayIndexOutOfBoundsException E) {}
        }

        scanDown:
        for(int i = kingRow - 1; i >= 0; i--){
            try{
                if((board[i][kingCol] instanceof Rook || board[i][kingCol] instanceof Queen) && board[i][kingCol].suit != suit){
                    return true;
                } else if(!(board[i][kingCol] instanceof Empty)){
                    break scanDown;
                }
            } catch(ArrayIndexOutOfBoundsException E) {}
        }

        scanRight:
        for(int i = kingCol + 1; i < 8; i++){
            try{
                if((board[kingRow][i] instanceof Rook || board[kingRow][i] instanceof Queen) && board[kingRow][i].suit != suit){
                    return true;
                } else if(!(board[kingRow][i] instanceof Empty)){
                    break scanRight;
                }
            } catch(ArrayIndexOutOfBoundsException E) {}
        }

        scanLeft:
        for(int i = kingCol - 1; i >= 0; i--){
            try{
                if((board[kingRow][i] instanceof Rook || board[kingRow][i] instanceof Queen) && board[kingRow][i].suit != suit){
                    return true;
                } else if(!(board[kingRow][i] instanceof Empty)){
                    break scanLeft;
                }
            } catch(ArrayIndexOutOfBoundsException E) {}
        }
        

        // check if bishop or queen can take the king
        for(int j = 0; j < 4; j++){
            int i = 1;
            scanLoop:
            while(true){
                tempRow = 0;
                tempCol = 0;
                switch(j){
                    case(0): // up and right
                    tempRow = kingRow + i;
                    tempCol = kingCol + i;
                    break;

                    case(1): // up and left
                    tempRow = kingRow + i;
                    tempCol = kingCol - i;
                    break;

                    case(2): // down and right
                    tempRow = kingRow - i;
                    tempCol = kingCol + i;
                    break;

                    case(3): // down and left
                    tempRow = kingRow - i;
                    tempCol = kingCol - i;
                    break;
                }

                if(tempRow >= 8 || tempRow < 0 || tempCol >= 8 || tempCol < 0) break scanLoop;
                if(board[tempRow][tempCol] instanceof Empty){
                } else if((board[tempRow][tempCol] instanceof Bishop || board[tempRow][tempCol] instanceof Queen) && board[tempRow][tempCol].suit != suit){
                    return true;
                } else {
                    break scanLoop;
                }
                i++;
            }
        }
        return false;
    } // end inCheck
    
    public static Piece newPieceSelect(int suit){
        System.out.println("What do you want your pawn to become?");
        while(true){
            System.out.println("1. Queen");
            System.out.println("2. Bishop");
            System.out.println("3. Knight");
            System.out.println("4. Rook");
            int userChoice = Library.numscan.nextInt();
            switch(userChoice){

                case(1):
                if(suit == 2){
                    upPieces[0]++;
                } else {
                    lowPieces[0]++;
                }
                return new Queen(suit);
                
                case(2):
                if(suit == 2){
                    upPieces[1]++;
                } else {
                    lowPieces[1]++;
                }
                return new Bishop(suit);
                
                case(3):
                if(suit == 2){
                    upPieces[2]++;
                } else {
                    lowPieces[2]++;
                }
                return new Knight(suit);
                
                case(4):
                if(suit == 2){
                    upPieces[3]++;
                } else {
                    lowPieces[3]++;
                }
                return new Rook(suit);
                
                default:
                System.out.println("That is not a valid choice. Please try again");
                break;
            }
        }
    } // end newPieceSelect()

    public static boolean castle(Piece[][] board, boolean turn, boolean side){ // right is true, left is false
        int suit;
        if(turn){
            suit = 2;
        } else {
            suit = 1;
        }
        
        if(turn){
            // uppercase
            if(side){
                // right
                if(board[0][7] instanceof Rook && board[0][7].suit == suit && board[0][4] instanceof King && board[0][4].suit == suit){
                    ArrayList<Coords> legalMoves = new ArrayList<>();
                    legalMoves.addAll(board[0][7].checkMove(board, 0, 7));
                    if(board[0][5].suit == 0){
                        for(int i = 0; i < legalMoves.size(); i++){
                            if(legalMoves.get(i).getRow() == 0 && legalMoves.get(i).getCol() == 5){
                                // scans for check
                                for(int  j = 1; j < 3; j++){
                                    Piece[][] tempBoard = new Piece[8][8];
                                    for(int k = 0; k < 8; k++){
                                        for(int l = 0; l < 8; l++){
                                            tempBoard[k][l] = board[k][l];
                                        }
                                    }
                                    tempBoard[0][4 + i] = tempBoard[0][3];
                                    tempBoard[0][3] = new Empty();
                                    if(inCheck(tempBoard, turn, 0, 4 + i)) return false;
                                }
                                // moves king and rook if all spaces between them are empty
                                board[0][5] = board[0][7];
                                board[0][7] = new Empty();
                                board[0][6] = board[0][4];
                                board[0][4] = new Empty();
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                // left
                if(board[0][0] instanceof Rook && board[0][0].suit == suit && board[0][4] instanceof King && board[0][4].suit == suit){
                    ArrayList<Coords> legalMoves = new ArrayList<>();
                    legalMoves.addAll(board[0][0].checkMove(board, 0, 0));
                    if(board[0][3].suit == 0){
                        for(int i = 0; i < legalMoves.size(); i++){
                            if(legalMoves.get(i).getRow() == 0 && legalMoves.get(i).getCol() == 3){
                                // scans for check
                                for(int  j = 1; j < 4; j++){
                                    Piece[][] tempBoard = new Piece[8][8];
                                    for(int k = 0; k < 8; k++){
                                        for(int l = 0; l < 8; l++){
                                            tempBoard[k][l] = board[k][l];
                                        }
                                    }
                                    tempBoard[0][4 - i] = tempBoard[0][3];
                                    tempBoard[0][3] = new Empty();
                                    if(inCheck(tempBoard, turn, 0, 4 - i)) return false;
                                }
                                // moves king and rook if all spaces between them are empty
                                board[0][3] = board[0][0];
                                board[0][0] = new Empty();
                                board[0][2] = board[0][4];
                                board[0][4] = new Empty();
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            // lowercase
            if(side){
                // right
                if(board[7][0] instanceof Rook && board[7][0].suit == suit && board[7][4] instanceof King && board[7][4].suit == suit){
                    ArrayList<Coords> legalMoves = new ArrayList<>();
                    legalMoves.addAll(board[7][0].checkMove(board, 7, 0));
                    if(board[7][3].suit == 0){
                        for(int i = 0; i < legalMoves.size(); i++){
                            if(legalMoves.get(i).getRow() == 7 && legalMoves.get(i).getCol() == 3){
                                // scans for check
                                for(int  j = 1; j < 3; j++){
                                    Piece[][] tempBoard = new Piece[8][8];
                                    for(int k = 0; k < 8; k++){
                                        for(int l = 0; l < 8; l++){
                                            tempBoard[k][l] = board[k][l];
                                        }
                                    }
                                    tempBoard[7][4 + i] = tempBoard[7][3];
                                    tempBoard[7][3] = new Empty();
                                    if(inCheck(tempBoard, turn, 7, 4 + i)) return false;
                                }
                                // moves king and rook if all spaces between them are empty
                                board[7][3] = board[7][0];
                                board[7][0] = new Empty();
                                board[7][2] = board[7][4];
                                board[7][4] = new Empty();
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                // left
                if(board[7][7] instanceof Rook && board[7][7].suit == suit && board[7][4] instanceof King && board[7][4].suit == suit){
                    ArrayList<Coords> legalMoves = new ArrayList<>();
                    legalMoves.addAll(board[7][7].checkMove(board, 7, 7));
                    if(board[7][5].suit == 0){
                        for(int i = 0; i < legalMoves.size(); i++){
                            if(legalMoves.get(i).getRow() == 7 && legalMoves.get(i).getCol() == 5){
                                // scans for check
                                for(int  j = 1; j < 4; j++){
                                    Piece[][] tempBoard = new Piece[8][8];
                                    for(int k = 0; k < 8; k++){
                                        for(int l = 0; l < 8; l++){
                                            tempBoard[k][l] = board[k][l];
                                        }
                                    }
                                    tempBoard[7][4 - i] = tempBoard[7][3];
                                    tempBoard[7][3] = new Empty();
                                    if(inCheck(tempBoard, turn, 7, 4 - i)) return false;
                                }
                                // moves king and rook if all spaces between them are empty
                                board[7][5] = board[7][7];
                                board[7][7] = new Empty();
                                board[7][6] = board[7][4];
                                board[7][4] = new Empty();
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
    } // end castle

    public static boolean checkEnPassant(Piece[][] board, boolean turn, int pieceRow, int pieceCol, int tarRow, int tarCol){
        for(int i = 0; i < 2; i++){
            int col = 0;
            switch(i){
                case(0):
                col = pieceCol - 1;
                break;

                case(1):
                col = pieceCol + 1;
                break;
            }
            if(turn){
                // uppercase
                try{
                    if(board[pieceRow][col] instanceof Pawn && board[pieceRow][col].suit == 1
                    && board[pieceRow][col].getNumMoves() == 1 && pieceRow == 4){
                        if(board[pieceRow  + 1][col] instanceof Empty){
                            if(pieceRow + 1 == tarRow && col == tarCol){
                                return true;
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException E) {}

            } else {
                // lowercase
                try{
                    if(board[pieceRow][col] instanceof Pawn && board[pieceRow][col].suit == 2
                    && board[pieceRow][col].getNumMoves() == 1 && pieceRow == 3){
                        if(board[pieceRow - 1][col] instanceof Empty){
                            if(pieceRow - 1 == tarRow && col == tarCol){
                                return true;
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException E) {}
            }
        }
        return false;
    } // end checkEnPassant

    public static void updateScore(Piece[][] board){
        // reset scores
        for(int i = 0; i < 5; i++){
            upPieces[i] = 0;
            lowPieces[i] = 0;
        }

        // scans board for new scores
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                // uppercase
                if(board[i][j].suit == 2 && board[i][j] instanceof Queen){
                    upPieces[0]++;
                } else if(board[i][j].suit == 2 && board[i][j] instanceof Bishop){
                    upPieces[1]++;
                } else if(board[i][j].suit == 2 && board[i][j] instanceof Knight){
                    upPieces[2]++;
                } else if(board[i][j].suit == 2 && board[i][j] instanceof Rook){
                    upPieces[3]++;
                } else if(board[i][j].suit == 2 && board[i][j] instanceof Pawn){
                    upPieces[4]++;

                // lowercase
                } else if(board[i][j].suit == 1 && board[i][j] instanceof Queen){
                    lowPieces[0]++;
                } else if(board[i][j].suit == 1 && board[i][j] instanceof Bishop){
                    lowPieces[1]++;
                } else if(board[i][j].suit == 1 && board[i][j] instanceof Knight){
                    lowPieces[2]++;
                } else if(board[i][j].suit == 1 && board[i][j] instanceof Rook){
                    lowPieces[3]++;
                } else if(board[i][j].suit == 1 && board[i][j] instanceof Pawn){
                    lowPieces[4]++;
                }
            }
        }
    } // end updateScore

    public static void printBoard(){
        // for uppercase player's turn
        if(turn){
            System.out.println("     A  B  C  D  E  F  G  H\n");
            for(int i = 7; i >= 0; i--){
                System.out.print((i+1) + "  ");

                for(int j = 0; j < 8; j++){
                    if(board[i][j] instanceof Empty){
                        if((j - i % 2) % 2 == 0){
                            System.out.print("  " + "-");
                        } else {
                            System.out.print("  " + "~");
                        }
                    } else if(board[i][j].getSuit() == 1){ // suit = 1
                        System.out.print("  " + Library.PINK + board[i][j].getChar() + Library.RESET);
                        // System.out.print("  " + board[i][j].getChar());
                    } else { // suit = 2
                        System.out.print("  " + Library.CYAN + board[i][j].getChar() + Library.RESET);
                        // System.out.print("  " + board[i][j].getChar());
                    }
                }

                System.out.print("\t");
                switch(i){
                    case(7):
                    System.out.print("Legend:\t");
                    System.out.print("\tPieces Left:");
                    break;

                    case(6):
                    System.out.print("K/k - king");
                    System.out.print("\tYou");
                    System.out.print("\tOpponent");
                    break;
                    
                    case(5):
                    System.out.print("Q/q - queen");
                    System.out.print("\t " + upPieces[0]);
                    System.out.print("\t   " + lowPieces[0]);
                    break;
                    
                    case(4):
                    System.out.print("B/b - bishop");
                    System.out.print("\t " + upPieces[1]);
                    System.out.print("\t   " + lowPieces[1]);
                    break;
                    
                    case(3):
                    System.out.print("N/n - knight");
                    System.out.print("\t " + upPieces[2]);
                    System.out.print("\t   " + lowPieces[2]);
                    break;
                    
                    case(2):
                    System.out.print("R/r - rook");
                    System.out.print("\t " + upPieces[3]);
                    System.out.print("\t   " + lowPieces[3]);
                    break;
                    
                    case(1):
                    System.out.print("P/p - pawn");
                    System.out.print("\t " + upPieces[4]);
                    System.out.print("\t   " + lowPieces[4]);
                    break;
                }
                System.out.println();
            }
            // for lowercase player's turn
        } else {
            System.out.println("     H  G  F  E  D  C  B  A\n");
            for(int i = 0; i < 8; i++){
                System.out.print((i+1) + "  ");

                for(int j = 7; j >= 0; j--){
                    if(board[i][j] instanceof Empty){
                        if((j - i % 2) % 2 == 0){
                            System.out.print("  " + "-");
                        } else {
                            System.out.print("  " + "~");
                        }
                    } else if(board[i][j].getSuit() == 1){ // suit = 1
                        System.out.print("  " + Library.PINK + board[i][j].getChar() + Library.RESET);
                        // System.out.print("  " + board[i][j].getChar());
                    } else { // suit = 2
                        System.out.print("  " + Library.CYAN + board[i][j].getChar() + Library.RESET);
                        // System.out.print("  " + board[i][j].getChar());
                    }
                }

                System.out.print("\t");
                switch(i){
                    case(0):
                    System.out.print("Legend:\t");
                    System.out.print("\tPieces Left:");
                    break;

                    case(1):
                    System.out.print("K/k - king");
                    System.out.print("\tYou");
                    System.out.print("\tOpponent");
                    break;
                    
                    case(2):
                    System.out.print("Q/q - queen");
                    System.out.print("\t " + lowPieces[0]);
                    System.out.print("\t   " + upPieces[0]);
                    break;
                    
                    case(3):
                    System.out.print("B/b - bishop");
                    System.out.print("\t " + lowPieces[1]);
                    System.out.print("\t   " + upPieces[1]);
                    break;
                    
                    case(4):
                    System.out.print("N/n - knight");
                    System.out.print("\t " + lowPieces[2]);
                    System.out.print("\t   " + upPieces[2]);
                    break;
                    
                    case(5):
                    System.out.print("R/r - rook");
                    System.out.print("\t " + lowPieces[3]);
                    System.out.print("\t   " + upPieces[3]);
                    break;
                    
                    case(6):
                    System.out.print("P/p - pawn");
                    System.out.print("\t " + lowPieces[4]);
                    System.out.print("\t   " + upPieces[4]);
                    break;
                }
                System.out.println();
            }
        }
    } // end printBoard()

    public static void setup(){
        /*
        Representation of pieces:
        R/r - Rook
        N/n - Knight
        B/b - Bishop
        K/k - King
        Q/q - Queen
        P/p - Pawn
        */
        // instantiates uppercase pieces
        board[0][0] = new Rook(2);
        board[0][1] = new Knight(2);
        board[0][2] = new Bishop(2);
        board[0][3] = new Queen(2);
        board[0][4] = new King(2);
        board[0][5] = new Bishop(2);
        board[0][6] = new Knight(2);
        board[0][7] = new Rook(2);

        for(int i = 0; i < 8; i++){
            board[1][i] = new Pawn(2);
            board[6][i] = new Pawn(1);
        } // instantiates pawns

        for(int i = 2; i < 6; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = new Empty();
            }
        } // instantiates all empty spaces

        // instantiates lowercase pieces
        board[7][0] = new Rook(1);
        board[7][1] = new Knight(1);
        board[7][2] = new Bishop(1);
        board[7][3] = new Queen(1);
        board[7][4] = new King(1);
        board[7][5] = new Bishop(1);
        board[7][6] = new Knight(1);
        board[7][7] = new Rook(1);

    } // end setup

    public static void testSetup(){
        // FOR TESTING ONLY

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = new Empty();
            }
        }

        // *note a capital(suit = 2) must be included to accomodate the game loop
        // board[4][4] = new Rook(1); 
        // board[5][4] = new Rook(2);

        // board[3][2] = new Knight(1);
        // board[5][7] = new Knight(2);

        // board[4][4] = new Queen(1);
        // board[5][4] = new Queen(2);

        // board[0][1] = new Bishop(1);
        // board[7][0] = new Bishop(2);

        // board[0][0] = new King(1);
        // board[7][7] = new King(2);

        // board[1][2] = new Pawn(1);
        // board[6][3] = new Pawn(2);

        // in check
        // board[0][3] =  new King(1);
        // board[1][6] = new Rook(2);
        // board[2][5] = new Rook(2);

        // in check with bishop
        board[0][0] = new King(1);
        board[7][0] = new Rook(2);
        board[0][7] = new Rook(2);
        board[7][5] = new Bishop(2);
        board[7][4] = new King(2);
        
        // in checkmate
        // board[0][0] =  new King(2);
        // board[0][6] = new Rook(1);
        // board[1][5] = new Rook(1);

        // for castling
        // board[0][0] = new Rook(2);
        // board[0][4] = new King(2);
        // board[0][7] = new Rook(2);
        // board[7][0] = new Rook(1);
        // board[7][4] = new King(1);
        // board[7][7] = new Rook(1);

    } // end testSetup()

    public static int findCol(char in){
        switch(in){
            case('A'):
            return 0;
            case('B'):
            return 1;
            case('C'):
            return 2;
            case('D'):
            return 3;
            case('E'):
            return 4;
            case('F'):
            return 5;
            case('G'):
            return 6;
            case('H'):
            return 7;
            default:
            return -1;
        }
    } // end findCol()

    public static int findRow(char in){
        switch(in){
            case('1'):
            return 0;
            case('2'):
            return 1;
            case('3'):
            return 2;
            case('4'):
            return 3;
            case('5'):
            return 4;
            case('6'):
            return 5;
            case('7'):
            return 6;
            case('8'):
            return 7;
            case('R'):
            return 20;
            case('L'):
            return 10;
            default:
            return -1;
        }
    } // end findRow
} // end class