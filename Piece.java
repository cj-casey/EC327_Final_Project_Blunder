package Piece;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    // Piece Class, includes all piece movement functionality and takes the chess
    // board as a input for most functions
    private char piece;
    private int pos_x;
    private int pos_y;

    private int turnCount;

    public Piece(char[][] board, int x, int y) {
        // constructor, self-explanatory
        this.piece = board[x][y];
        this.pos_x = x;
        this.pos_y = y;
        this.turnCount = 0;
    }

    public String toString() {
        return piece + " " + pos_x + " " + pos_y; // returns components as string
    }

    public boolean equals(Piece piece1) { //checks if two pieces are equal
        if ((this.piece == piece1.piece) && (this.pos_x == piece1.pos_x) && (this.pos_y == piece1.pos_y)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean team() {
        // returns false if its black
        return (64 < piece) && (piece < 96); // returns true if its white
    }

    public void takePiece(char[][] board, int x, int y) {
        // moves pieces across board and updates their xy

        Piece takenPiece = new Piece(board, x, y);
        boolean sameTeam = (this.team() == takenPiece.team());
        // piece1.team() == piece2.team()
        if (!sameTeam) { // sets char of new space to the moving piece changes pos values and sets old
                         // spot to " "
            board[x][y] = this.piece;

            board[this.pos_x][this.pos_y] = ' ';
            this.pos_x = x;
            this.pos_y = y;
        }
    }

    public boolean isOccupied() {
        // checks if a space is occupied
        return this.piece != ' ';
    }

    public boolean kingsTooClose(Piece enemyKing) {
        // returns the difference between two pieces on the chessboard
        int distance = Math.max(Math.abs(enemyKing.pos_x - this.pos_x), Math.abs(enemyKing.pos_y - this.pos_y));

        return distance <= 1;
    }

    public List<Piece> checkX(char[][] board) { // returns all possible movements for X shaped movement (ie bishop,
                                                // queen)
        List<Piece> allMovements = new ArrayList<>();
        int topPathLength = 7 - pos_y;
        int btmPathLength = pos_y;
        int rigPathLength = 7 - pos_x;
        int lefPathLength = pos_x;
        int trPathLength, brPathLength, blPathLength, tlPathLength;
        ///
        trPathLength = Math.min(topPathLength, rigPathLength);
        ///
        brPathLength = Math.min(btmPathLength, rigPathLength);
        ///
        blPathLength = Math.min(topPathLength, lefPathLength);

        tlPathLength = Math.min(topPathLength, lefPathLength);
        // checks top right path

        int x, y;
        trLoop: {
            for (int i = 1; i <= trPathLength; i++) {
                x = pos_x + i;
                y = pos_y + i;
                if ((x > -1) && (x < 8) && (y > -1) && (y < 8)) {
                    Piece space = new Piece(board, pos_x + i, pos_y + i);

                    if (!space.isOccupied()) { // not occupied, adds it
                        allMovements.add(space);
                    } else if (space.team() != this.team()) { // if its an enemy, add it, then break loop
                        allMovements.add(space);
                        break trLoop;
                    } else if (space.team() == this.team()) {// if its a friend, add it, then break loop
                        break trLoop;
                    }
                }
            }
        }
        // checks bottom right path
        brLoop: {
            for (int i = 1; i <= brPathLength; i++) {
                x = pos_x + i;
                y = pos_y - i;
                if ((x > -1) && (x < 8) && (y > -1) && (y < 8)) {
                    Piece space = new Piece(board, pos_x + i, pos_y - i);

                    if (!space.isOccupied()) {
                        allMovements.add(space);
                    } else if (space.team() != this.team()) {
                        allMovements.add(space);
                        break brLoop;
                    } else if (space.team() == this.team()) {
                        break brLoop;
                    }
                }
            }
        }
        // checks bottom left path
        blLoop: {
            for (int i = 1; i <= blPathLength; i++) {
                x = pos_x - i;
                y = pos_y - i;
                if ((x > -1) && (x < 8) && (y > -1) && (y < 8)) {
                    Piece space = new Piece(board, pos_x - i, pos_y - i);
                    if (!space.isOccupied()) {
                        allMovements.add(space);
                    } else if (space.team() != this.team()) {
                        allMovements.add(space);
                        break blLoop;
                    } else if (space.team() == this.team()) {
                        break blLoop;
                    }
                }
            }
        }
        // checks top left path
        tlLoop: {
            for (int i = 1; i <= tlPathLength; i++) {
                x = pos_x - i;
                y = pos_y + i;
                if ((x > -1) && (x < 8) && (y > -1) && (y < 8)) {
                    Piece space = new Piece(board, pos_x - i, pos_y + i);
                    if (!space.isOccupied()) {
                        allMovements.add(space);
                    } else if (space.team() != this.team()) {
                        allMovements.add(space);
                        break tlLoop;
                    } else if (space.team() == this.team()) {
                        break tlLoop;
                    }
                }
            }
        }
        return allMovements;
    }

    public List<Piece> checkKing(char[][] board, List<Piece> pieceList) { //returns a list of all possible moves for the king
        List<Piece> unCheckedList = new ArrayList<>();
        List<Piece> movementList = new ArrayList<>();
        boolean found = false;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0)) // doesnt check itself
                {
                    if (((this.pos_x + i) > 0 && (this.pos_x + i) < 8)
                            && ((this.pos_y + j) > 0 && (this.pos_y + j < 8))) // within bounds
                    {
                        Piece space = new Piece(board, this.pos_x + i, this.pos_y + j);
                        unCheckedList.add(space);
                    }

                }
            }
        }

        for (int i = 0; i < unCheckedList.size(); i++) // iterates through unchecked list for possible moves
        {
            if (!(unCheckedList.get(i).isOccupied()) || (unCheckedList.get(i).team() != this.team())) // if its not occupied or the opposite team
                                                                                                     
            {
                for (int j = 0; j < pieceList.size(); j++) // checks if possible moves are currently under attack and
                                                           // thus not valid
                {
                    List<Piece> enemyMoveList = new ArrayList<>();
                    enemyMoveList = pieceList.get(j).possibleMoves(board);

                    if (unCheckedList.get(i).inMovelist(enemyMoveList)) // if its in there, then NO
                    {
                        if(enemyMoveList.get(j).piece == 'k' || enemyMoveList.get(j).piece == 'K') //if checking the enemy king, check to see if the kings are far enough away
                        {
                            if(unCheckedList.get(i).kingsTooClose(enemyMoveList.get(j)))
                            {
                                found = true;
                            }
                        }
                        else
                        {
                            found = true;
                        }
                    }
                }

                if (!found) {
                    movementList.add(unCheckedList.get(i)); // adds happy valid move to list
                }
            }

        }
        // iterate through all Pieces in Piece list and check to see which of the King's
        // possible moves are in those, if all of them ar
        return movementList;
    }

    public List<Piece> checkCross(char[][] board) { // checks all possible movement spaces for a piece that moves in a
                                                    // cross formation(ie rook queen)
        List<Piece> allMovements = new ArrayList<>();
        int topPathLength = 7 - pos_y;
        int btmPathLength = pos_y;
        int rigPathLength = 7 - pos_x;
        int lefPathLength = pos_x;
        // checks top path
        topLoop: {
            for (int i = 1; i <= topPathLength; i++) {
                Piece space = new Piece(board, pos_x, pos_y + i);

                if (!(space.isOccupied())) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break topLoop;
                } else if (space.team() == this.team()) {
                    break topLoop;
                }
            }
        }
        // checks right path
        rightLoop: {
            for (int i = 1; i <= rigPathLength; i++) {
                Piece space = new Piece(board, pos_x + i, pos_y);

                if (!space.isOccupied()) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break rightLoop;
                } else if (space.team() == this.team()) {
                    break rightLoop;
                }
            }
        }
        // checks bottom path
        btmLoop: {
            for (int i = 1; i <= btmPathLength; i++) {
                Piece space = new Piece(board, pos_x, pos_y - i);
                if (!space.isOccupied()) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break btmLoop;
                } else if (space.team() == this.team()) {
                    break btmLoop;
                }

            }
        }
        // checks left path
        leftLoop: {
            for (int i = 1; i <= lefPathLength; i++) {
                Piece space = new Piece(board, pos_x - i, pos_y);
                if (!space.isOccupied()) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break leftLoop;
                } else if (space.team() == this.team()) {
                    break leftLoop;
                }
            }
        }
        return allMovements;
    }

    public List<Piece> checkL(char[][] board) { // checks all possible movements for pieces that move in an L shape
        List<Piece> allMovements = new ArrayList<>();
        int longue = 2, shorte = 1, xmod = 1, ymod = 1;
        // goes around all 8 possibilites
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1:
                    xmod = 1;
                    ymod = -1;
                    break;
                case 2:
                    xmod = -1;
                    ymod = -1;

                    break;
                case 3:
                    xmod = -1;
                    ymod = 1;
                    break;
            }

            // creates x and y values for the two moves with alternating short and long
            // sides
            int x1 = (pos_x + (longue * xmod));
            int x2 = (pos_x + (shorte * xmod));
            int y1 = (pos_x + (longue * ymod));
            int y2 = (pos_x + (shorte * ymod));

            // if within bounds it adds them to the list
            if ((x1 > -1) && (x1 < 8) && (y1 > -1) && (y1 < 8)) {
                Piece move1 = new Piece(board, x1, y1);

                if ((!move1.isOccupied() && (this.team() != move1.team()))) {
                    allMovements.add(move1);
                }
            }

            if ((x2 > -1) && (x2 < 8) && (y2 > -1) && (y2 < 8)) {
                Piece move2 = new Piece(board, x2, y2);

                if ((!move2.isOccupied() && (this.team() != move2.team()))) {
                    allMovements.add(move2);
                }
            }
        }

        return allMovements;
    }

    public List<Piece> checkPawn(char[][] board) { // checks all possible movements for pawns
        int direction;
        List<Piece> moveList = new ArrayList<>();

        if (this.team()) {
            direction = 1;
        } else {
            direction = -1;
        }

        if ((this.pos_y + (direction * 1) < 8) && ((this.pos_y + (direction * 1) > -1))) {
            Piece move = new Piece(board, this.pos_x,
                    this.pos_y + (direction * 1));
            if (!move.isOccupied()) {
                moveList.add(move);
            }
        }

        if ((this.pos_y + (direction * 2) < 8) && ((this.pos_y + (direction * 2) > -1))) { //initial move
            Piece moveInitial = new Piece(board, this.pos_x,
                    this.pos_y + (direction * 2));
            if ((turnCount == 0) && (!moveInitial.isOccupied())) {
                moveList.add(moveInitial);
            }
        }
        if ((this.pos_y + (direction * 1) < 8) && ((this.pos_y + (direction * 1) > -1))) { //taking enemy piece
            if ((this.pos_x + 1 < 8) && (this.pos_x + 1 > -1)) {

                Piece takeR = new Piece(board, this.pos_x + 1,
                        this.pos_y + (direction * 1));
                if (takeR.isOccupied() && (this.team() != takeR.team())) {
                    moveList.add(takeR);
                }
            }
        }

        if ((this.pos_y + (direction * 1) < 8) && ((this.pos_y + (direction * 1) > -1))) { //taking enemy piece
            if ((this.pos_x - 1 < 8) && (this.pos_x - 1 > -1)) {
                Piece takeL = new Piece(board, this.pos_x - 1,
                        this.pos_y + (direction * 1));

                if (takeL.isOccupied() && (this.team() != takeL.team())) {
                    moveList.add(takeL);
                }
            }
        }

        return moveList;
    }

    public boolean inMovelist(List<Piece> moveList) { // searches movelist to see if an attempted move exists
        for (int i = 0; i < moveList.size(); i++) {
            if ((this.piece == moveList.get(i).piece) && (this.pos_x == moveList.get(i).pos_x)
                    && (this.pos_y == moveList.get(i).pos_y)) {
                return true;
            }
        }
        return false;
    }

    public int findPiece(List<Piece> moveList) { // searches movelist to see if an attempted move is valid and returns the index, -1 if not found
        for (int i = 0; i < moveList.size(); i++) {
            if ((this.piece == moveList.get(i).piece) && (this.pos_x == moveList.get(i).pos_x)
                    && (this.pos_y == moveList.get(i).pos_y)) {
                return i;
            }
        }
        return -1;
    }

    public List<Piece> possibleMoves(char[][] board) { //generic possible moves function
        List<Piece> moveList = new ArrayList<>();

        switch (this.piece) {
            case 'q':
            case 'Q':
                moveList = checkX(board);
                moveList.addAll(checkCross(board));
                break;
            case 'b':
            case 'B':
                moveList = checkX(board);
                break;
            case 'n':
            case 'N':
                moveList = checkL(board);
                break;
            case 'r':
            case 'R':
                moveList = checkCross(board);
                break;
            case 'p':
            case 'P':
                moveList = checkPawn(board);
                break;
        }

        return moveList;
    }

    public void printMovelist(List<Piece> moveList) { //prints entire list
        for (int i = 0; i < moveList.size(); i++) {
            System.out.println(moveList.get(i));
        }
    }

    public void handleMovement(char[][] board, int x, int y, List<Piece> enemyPieces) { // moves pieces across board and takes pieces
        //prevents user/bot from picking off the board
        if (x > 7)
            x = 7;
        else if (x < 0)
            x = 0;

        if (y > 7)
            y = 7;
        else if (y < 0)
            y = 0;

        //creates piece object for the target spot
        Piece target = new Piece(board, x, y);

        char tempPiece = this.piece;
    if(target.piece != 'k' && target.piece != 'K' )
    {
        if ((64 < this.piece) && (this.piece < 92)) {
            tempPiece = Character.toLowerCase(this.piece);
        }

            if (tempPiece == 'k') { //king checking

                if (target.inMovelist(this.checkKing(board, enemyPieces))) { //
                        this.takePiece(board, x, y);
                        if (target.isOccupied()) { //if there was a piece, it finds and removes that piece from the enemy pieces list 
                            enemyPieces.remove(target.findPiece(enemyPieces));
                        }
                } else {
                    System.out.println("Invalid Move");
                    return;

                }
            } else { //generic pieces
                System.out.println("Target Piece");
                System.out.println(target);
                if (target.inMovelist(this.possibleMoves(board))) {
                    this.takePiece(board, x, y);
                    if (target.isOccupied()) {
                        enemyPieces.remove(target.findPiece(enemyPieces));
                    }
                } else {
                    System.out.println("Invalid Move");
                    return;
                }
            }
        }
            this.turnCount++; //increments turn per movement
    }

//     public List<Piece> findKings(char[][] board){
        
//         List<Piece> kings = new ArrayList<>();
//         Piece blackKing = new Piece;
//         Piece whiteKing = new Piece;
        
//         for(int i = 0; i<8; i++){
//             for(int j = 0; j<8; j++){
//                     if(board[i][j] == 'k'){
//                     Piece blackKing = Piece(board,i,j);        
//                     }
//                     else if(board[i][j] == 'K'){
//                     Piece whiteKing = Piece(board,i,j);
//                     }
                
//                 }
            
//             }

//         kings.addAll(blackKing);
//         kings.addAll(whiteKing);

//         return kings;
        
//         }
        

//     public int inCheck(char[][] board, list<Piece> whitePieces, list<Piece> blackPieces){
//         //function creates a list of all the possible moves on the board, and if the king is a target in that movelist
//         //inCheck == 1 if white in check
//         //inCheck == -1 if black in check
//         //inCheck == 0 if no check
        
//         List<Piece> moveList = new ArrayList<>();
//         List<Piece> kings = new ArrayList<>();
//         kings = findKings(board);

//         movelist.addAll(kings.get(0).checkKing(board, blackPieces);
//         movelist.addAll(kings.get(1).checkKing(board, whitePieces);
        
//         for(int i = 0; i<whitePieces.size(); i++){
//             moveList.addAll(whitePieces.get(i).possibleMoves(board));
//         }
//         for(int i = 0; i<blackPieces.size(); i++){
//             movelist.addAll(blackPieces.get(i).possibleMoves(board));
//         }
        
//             if (kings.get(0).findPiece(moveList) >= 0){  
//                 return -1;
//             }   
//             else if (kings.get(1).findPiece(moveList) >= 0){
//                 return 1;
//             }
//         }
//         return 0;
//     }
// public int gameState(char[][] board, List<Piece> whitePieces, List<Piece> blackPieces){
       
//        List<Piece> kings = new ArrayList<>();
//        kings = findKings(board);
    
//        List<Piece> blackMoves = new ArrayList<>();
//        List<Piece> whiteMoves = new ArrayList<>();
//        char[][] tempboard = new char[8][8];
       
//     for(int i = 0; i<8; i++){
//         for(int j = 0; j<8; j++){
//             tempboard[i][j] = board[i][j];
//         }
//     }

//         Piece tempblackKing = new Piece(tempboard, kings.get(0).getpos_x(0), kings.get(0).getpos_y(0));
//         Piece tempwhiteKing = new Piece(tempboard, kings.get(1).getpos_x(1), kings.get(1).getpos_y(1));
    
//        switch(inCheck(board, whitePieces, blackPieces)){
//            case 0:
//                for(int i = 0; i<whitePieces.size(); i++){
//                   whiteMoves.addAll(whitePieces.get(i).possibleMoves(board)); 
//                }
//                   if(whiteMoves.size() == 0){
//                     return -1; 
//                   }
//                for(int j = 0; j<blackPieces.size(); j++){
//                    blackMoves.addAll(blackPieces.get(j).possibleMoves(board));
//                }
//                    if(blackMoves.size() == 0){
//                     return -1;
//                    }  
//                return 0;
               
//                break;
           
//            case -1:
//                for(int j = 0; j<blackPieces.size(); j++){
//                    blackMoves.addAll(blackPieces.get(j).possibleMoves(board));
//                }

//                for(int i = 0; i<blackMoves.size(); i++){
    
//                }

               
//                break;
           
//            case 1;
               
//                break;
//        }
    

    
//             //once its made, do the following
//                 //i should do a for loop, that iterates through the movelist, and simulates all of the movements of black/white pieces
//                 //if white is in check (inCheck == 1) for ALL possible white movements. checkmate, Black wins (bot) done. return 2;
//                 //if black is in check (inCheck == -1) for All possible black movements. checkmate, White wins (player wins). done. return 1;
//                 //if inCheck == 0 and (if player AND bot both have LEGAL MOVES). no checkmate, game on. return 0
//                 //if inCheck == 0 and either movelist is empty. stalemate, done. return -1

//                 // Piece temp_kingPiece = new Piece(tempboard, this.pos_x, this.pos_y);
//                 // handleMovement(tempboard, temp_kingPiece.pos_x, temp_kingPiece.pos_y, enemyPieces);




//                 }




//             }

    // getters and setters
    public int getPosX() {
        return pos_x;
    }

    public int getPosY() {
        return pos_y;
    }

    public char getPiece() {
        return piece;
    }

    public void setPosX(int x) {
        this.pos_x = x;
    }

    public void setPosY(int y) {
        this.pos_y = y;
    }

    public void setPiece(char p) {
        this.piece = p;
    }

}
