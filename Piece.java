package com.example.onechess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Piece implements Serializable {
    // Piece Class, includes all piece movement functionality and takes the chess
    // board as a input for most functions
    private char piece;
    private int pos_x;
    private int pos_y;
    private int turnCount;

    private int previous_x;
    private int previous_y;

    public Piece(char[][] board, int x, int y) {
        // constructor, self-explanatory
        this.piece = board[x][y];
        this.pos_x = x;
        this.pos_y = y;
        this.turnCount = 0;
    }

    public static void drawBoard(char[][] board) {
        for (int j = 7; j > -1; j--) {
            for (int i = 0; i < 8; i++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Piece(char piece, int x, int y) {
        // constructor,self-explanatory
        this.piece = piece;
        this.pos_x = x;
        this.pos_y = y;
        this.turnCount = 0;
    }

    public String toString() {
        return piece + " " + pos_x + " " + pos_y; // returns components as string
    }

    public boolean equals(Piece piece1) { // checks if two pieces are equal
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

        board[x][y] = this.piece;
        board[this.pos_x][this.pos_y] = ' ';
        this.pos_x = x;
        this.pos_y = y;

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
        blPathLength = Math.min(btmPathLength, lefPathLength);

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
                    Piece space = new Piece(board, x, y);

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
                x = this.pos_x - i;
                y = this.pos_y - i;
                if ((x > -1) && (x < 8) && (y > -1) && (y < 8)) {
                    Piece space = new Piece(board, x, y);
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

    public List<Piece> checkKing(char[][] board, List<Piece> whitePieces, List<Piece> blackPieces) { // returns a list
                                                                                                     // of all possible
                                                                                                     // moves for the
                                                                                                     // king
        List<Piece> unCheckedList = new ArrayList<>();
        List<Piece> movementList = new ArrayList<>();

        char tempPiece;
        boolean found = false;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0)) // doesnt check itself
                {
                    if (((this.pos_x + i) > -1 && (this.pos_x + i) < 8)
                            && ((this.pos_y + j) > -1 && (this.pos_y + j < 8))) // within bounds
                    {
                        Piece space = new Piece(board, this.pos_x + i, this.pos_y + j);

                        if (!space.isOccupied() || space.team() != this.team()) {
                            unCheckedList.add(space);
                        }
                    }
                }
            }
        }

        List<Piece> pieceList = (this.team()) ? blackPieces : whitePieces;

        for (int i = 0; i < unCheckedList.size(); i++) // iterates through unchecked list for possible moves
        {
            found = false;
            if (!(unCheckedList.get(i).isOccupied()) || (unCheckedList.get(i).team() != this.team())) // if its not
                                                                                                      // occupied or the
                                                                                                      // opposite team

            {
                tempPiece = unCheckedList.get(i).piece;
                unCheckedList.get(i).piece = this.piece;

                board[unCheckedList.get(i).pos_x][unCheckedList.get(i).pos_y] = this.piece; // place temporary king
                for (int j = 0; j < pieceList.size(); j++) // checks if possible moves are currently under attack and
                // thus not valid
                {
                    List<Piece> enemyMoveList = new ArrayList<>();
                    enemyMoveList = pieceList.get(j).possibleMoves(board, !this.team());
                    if (unCheckedList.get(i).inMovelist(enemyMoveList)) // if its in there, then NO
                    {
                        if (pieceList.get(j).piece == 'k' || pieceList.get(j).piece == 'K') // if checking the enemy
                                                                                            // king, check to see if the
                                                                                            // kings are far enough away
                        {
                            if (unCheckedList.get(i).kingsTooClose(enemyMoveList.get(j))) {
                                found = true;
                            }
                        } else {
                            found = true;
                        }
                    }
                }
                board[unCheckedList.get(i).pos_x][unCheckedList.get(i).pos_y] = tempPiece;
                unCheckedList.get(i).piece = tempPiece;
                if (!found) {
                    movementList.add(unCheckedList.get(i)); // adds happy valid move to list
                }
            }

        }
        //drawBoard(board);
        // iterate through all Pieces in Piece list and check to see which of the King's
        // possible moves are in those, if all of them ar
        return movementList;
    }

    public List<Piece> findKings(char[][] board) {

        List<Piece> kings = new ArrayList<>();
        kings.add(new Piece(board, 0, 0));
        kings.add(new Piece(board, 0, 0));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'k') {
                    kings.get(1).setPiece('k');
                    kings.get(1).setPosX(i);
                    kings.get(1).setPosY(j);
                } else if (board[i][j] == 'K') {
                    kings.get(0).setPiece('K');
                    kings.get(0).setPosX(i);
                    kings.get(0).setPosY(j);
                }

            }

        }

        // System.out.println(kings.get(0));
        // System.out.println(kings.get(1));
        return kings;

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
        int longue = 2, shorte = 1, smod = -1, lmod = -1;
        // goes around all 8 possibilites
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1:
                    smod = -1;
                    lmod = 1;
                    break;
                case 2:
                    smod = 1;
                    lmod = -1;
                    break;
                case 3:
                    smod = 1;
                    lmod = 1;
                    break;
            }

            // creates x and y values for the two moves with alternating short and long
            // sides
            int lindex = (pos_x + (longue * lmod));
            int sindex = (pos_x + (shorte * smod));

            int lindey = (pos_y + (longue * lmod));
            int sindey = (pos_y + (shorte * smod));

            // if within bounds it adds them to the list
            if ((lindex > -1) && (lindex < 8) && (sindey > -1) && (sindey < 8)) {
                Piece move1 = new Piece(board, lindex, sindey);

                if ((!move1.isOccupied() || (this.team() != move1.team()))) {
                    allMovements.add(move1);
                }
            }

            if ((sindex > -1) && (sindex < 8) && (lindey > -1) && (lindey < 8)) {
                Piece move2 = new Piece(board, sindex, lindey);

                if ((!move2.isOccupied() || (this.team() != move2.team()))) {
                    allMovements.add(move2);
                }
            }
        }
        return allMovements;
    }

    public List<Piece> checkPawn(char[][] board, boolean team) { // checks all possible movements for pawns
        int direction;
        List<Piece> moveList = new ArrayList<>();

        if (this.team()) {
            direction = 1;
        } else {
            direction = -1;
        }

        if (this.team() == team) {

            if ((this.pos_y + (direction * 1) < 8) && ((this.pos_y + (direction * 1) > -1))) {
                Piece move = new Piece(board, this.pos_x,
                        this.pos_y + (direction * 1));
                if (!move.isOccupied()) {
                    moveList.add(move);
                }
            }

            if ((this.pos_y + (direction * 2) < 8) && ((this.pos_y + (direction * 2) > -1))) { // initial move
                Piece moveInitial = new Piece(board, this.pos_x,
                        this.pos_y + (direction * 2));
                if ((this.turnCount == 0) && (!moveInitial.isOccupied())) {
                    moveList.add(moveInitial);
                }
            }
        }

        if ((this.pos_y + (direction * 1) < 8) && ((this.pos_y + (direction * 1) > -1))) { // taking enemy piece
            if ((this.pos_x + 1 < 8) && (this.pos_x + 1 > -1)) {

                Piece takeR = new Piece(board, this.pos_x + 1,
                        this.pos_y + (direction * 1));
                if (takeR.isOccupied() && (this.team() != takeR.team())) {
                    moveList.add(takeR);
                }
            }
        }

        if ((this.pos_y + (direction * 1) < 8) && ((this.pos_y + (direction * 1) > -1))) { // taking enemy piece
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

    public boolean checkDiscoverValid(char[][] board, Piece move, List<Piece> whitePieces, List<Piece> blackPieces) { // Determines
                                                                                                                      // if
                                                                                                                      // a
                                                                                                                      // move
                                                                                                                      // is
                                                                                                                      // valid
                                                                                                                      // by
                                                                                                                      // checking
                                                                                                                      // if
                                                                                                                      // it
                                                                                                                      // cause
                                                                                                                      // the
                                                                                                                      // teams
                                                                                                                      // king
                                                                                                                      // to
                                                                                                                      // go
                                                                                                                      // into
                                                                                                                      // Check
        // true if valid, false if not

        // moves the piece and sets old place to blank for checking
        char tempPiece;
        boolean valid = true;
        int moveIndex = 0;
        List<Piece> enemyPieceList;
        List<Piece> ownPieceList;
        // sets index at 0 or 1 for findKings: {whiteKing,blackKing}
        int kingIndex = (this.team()) ? 0 : 1;
        Piece king;
        enemyPieceList = (this.team()) ? blackPieces : whitePieces;
        ownPieceList = (this.team()) ? whitePieces : blackPieces;
        // makes temporary move for inCheck
        if (move.isOccupied()) {
            moveIndex = move.findPiece(enemyPieceList);
            enemyPieceList.get(moveIndex).setPiece(this.piece);
        }
        tempPiece = move.getPiece();
        move.setPiece(this.piece);
        board[move.getPosX()][move.getPosY()] = this.piece;
        board[this.getPosX()][this.getPosY()] = ' ';
        this.setPiece(' ');
        // picks which is enemy and itself
        //drawBoard(board);
        // gets own teams king using kingindex
        king = findKings(board).get(kingIndex);
        // if king is in the movelist of any enemy, nonvalid
        for (int j = 0; j < enemyPieceList.size(); j++) {
            List<Piece> enemyMoveList = new ArrayList<>();
            enemyMoveList = enemyPieceList.get(j).possibleMoves(board, !this.team());
            if (king.inMovelist(enemyMoveList)) {
                valid = false;
            }
        }
        // if white moved and now white is in check, nonvalid

        // undoes move
        if (move.isOccupied()) {
            enemyPieceList.get(moveIndex).setPiece(tempPiece);
        }
        this.setPiece(move.getPiece());
        board[this.getPosX()][this.getPosY()] = this.piece;
        board[move.getPosX()][move.getPosY()] = tempPiece;
        move.setPiece(tempPiece);
        //System.out.println("DISCOVERYCHECK");
        //drawBoard(board);
        return valid;

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

    public int findPiece(List<Piece> moveList) { // searches movelist to see if an attempted move is valid and returns
                                                 // the index, -1 if not found
        for (int i = 0; i < moveList.size(); i++) {
            if ((this.piece == moveList.get(i).piece) && (this.pos_x == moveList.get(i).pos_x)
                    && (this.pos_y == moveList.get(i).pos_y)) {
                return i;
            }
        }
        return -1;
    }

    public List<Piece> possibleMoves(char[][] board, boolean team_turn) { // generic possible moves function
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
                moveList = checkPawn(board, team_turn);
                break;
        }

        return moveList;
    }

    public void printMovelist(List<Piece> moveList) { // prints entire list
        for (int i = 0; i < moveList.size(); i++) {
            System.out.println(moveList.get(i));
        }
    }

    public int stalemateOrCheckmate(char[][] board, List<Piece> whitePieces, List<Piece> blackPieces)
    { //returns 0 if neither, 1 if checkmate, -1 if stalemate
            // Check if the king is in check

    List<Piece> enemyPieceList = (this.team()) ? blackPieces : whitePieces;
    List<Piece> ownPieceList = (this.team()) ? whitePieces : blackPieces;
    boolean movesLeft = false;
    boolean kingInCheck = false;
    // checking if theres any valid moves for your team
    checkLoop:for(
    int i = 0;i<ownPieceList.size();i++)
    {
        List<Piece> unCheckedList = ownPieceList.get(i).possibleMoves(board, this.team());
        List<Piece> checkedList = new ArrayList<>();

        for (int j = 0; j < unCheckedList.size(); j++) {
            if (ownPieceList.get(i).checkDiscoverValid(board, unCheckedList.get(j), whitePieces, blackPieces)) { // if
                                                                                                                 // it
                                                                                                                 // prevents
                                                                                                                 // check
                                                                                                                 // then
                                                                                                                 // its
                                                                                                                 // valid
                checkedList.add(unCheckedList.get(j));
            }
        }
        // System.out.println("MOVES LEFT:" + ownPieceList.get(i));
        printMovelist(checkedList);
        if (checkedList.size() != 0) {
            movesLeft = true;
            break checkLoop;
        }

    }
    // if not, then checks if king is in check
    if(!movesLeft)
    {
        int kingIndex = (this.team()) ? 0 : 1;
        Piece king;
        king = findKings(board).get(kingIndex);
        for (int j = 0; j < enemyPieceList.size(); j++) {
            List<Piece> enemyMoveList = new ArrayList<>();
            enemyMoveList = enemyPieceList.get(j).possibleMoves(board, !this.team());
            if (king.inMovelist(enemyMoveList)) {
                kingInCheck = true;
            }
        }
    }System.out.println("Check?: "+kingInCheck);if(movesLeft)
    {
        return 0; // neither
    }else
    {
        if (kingInCheck) {
            return 1; // checkmate
        } else {
            return -1; // stalemate
        }
    }

    } 

    public boolean handleMovement(char[][] board, int x, int y, List<Piece> whitePieces, List<Piece> blackPieces, Piece TakenPiece) {
        // moves pieces across board and
        // takes pieces

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

        if(target.piece != 'k' && target.piece != 'K' && this.piece != ' ')
        {

            if (this.piece == 'k' || this.piece == 'K') { //king checking
                if (target.inMovelist(this.checkKing(board, whitePieces,blackPieces))) { //
                    System.out.println(target.inMovelist(this.checkKing(board, whitePieces,blackPieces)));
                    System.out.println("King's moves:");
                    System.out.println(this.checkKing(board,whitePieces,blackPieces));

                    if (target.isOccupied()) {

                        TakenPiece.setPiece(target.piece);
                        TakenPiece.setPosX(target.pos_x);
                        TakenPiece.setPosY(target.pos_y);

                        if(this.team()){
                            //if there was a piece, it finds and removes that piece from the enemy pieces list
                            blackPieces.remove(target.findPiece(blackPieces));
                        }
                        else{
                            whitePieces.remove(target.findPiece(whitePieces));
                        }
                    }
                    this.takePiece(board, x, y);

                } else {
                    System.out.println("Invalid Move");
                    return false;

                }
            } else { //generic pieces
    

                if (target.inMovelist(this.possibleMoves(board, this.team() ))) {
                    TakenPiece.setPiece(target.piece);
                    TakenPiece.setPosX(target.pos_x);
                    TakenPiece.setPosY(target.pos_y);
                    if(this.checkDiscoverValid(board,target,whitePieces,blackPieces)) {
                            this.takePiece(board, x, y);

                            if (target.isOccupied()) {
                                if (this.team()) {
                                    //if there was a piece, it finds and removes that piece from the enemy pieces list
                                    blackPieces.remove(target.findPiece(blackPieces));
                                } else {
                                    whitePieces.remove(target.findPiece(whitePieces));
                                }
                            }
                        }
                    else {
                        return false;
                    }
                } else {
                    System.out.println("Invalid Move");
                    printMovelist(this.possibleMoves(board,this.team()));
                    return false;
                }
            }
        }
        this.turnCount++; //increments turn per movement
        return true;
    }

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

    public int getTurnCount() {
        return turnCount;
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

    public void setTurnCount(int i) {
        this.turnCount = i;
    }

}
