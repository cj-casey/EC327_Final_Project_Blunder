import java.util.ArrayList;
import java.util.List;

public class Piece {
    //Piece Class, includes all piece movement functionality and takes the chess board as a input for most functions
    private char piece;
    private int pos_x;
    private int pos_y;

    private int turnCount;

    public Piece(char piece, int x, int y) {
        //constructor, self-explanatory
        this.piece = piece;
        this.pos_x = x;
        this.pos_y = y;
        this.turnCount = 0;
    }

    public boolean team() {
        // returns false if its black
        return (64 < piece) && (piece < 96); //returns true if its white
    }

    public void takePiece(char[][] board, int x, int y) {
        //moves pieces across board and updates their xy
        Piece takenPiece = new Piece(board[x][y], x, y);
        boolean sameTeam = (this.team() == takenPiece.team());
        //piece1.team() == piece2.team()
        if (!sameTeam) { //sets char of new space to the moving piece changes pos values and sets old spot to " "
            board[x][y] = this.piece;
            board[this.pos_x][this.pos_y] = ' ';
            this.pos_x = x;
            this.pos_y = y;
        }
    }

    public boolean isOccupied(Piece space) {
        //checks if a space is occupied
        return space.piece != ' ';
    }

    public int[] returnDifference(int x, int y) {
        //returns the difference between the piece and a point on the chessboard
        int bigX, bigY, smallX, smallY;
        int[] xy = new int[2];

        if (this.pos_x > x) {
            bigX = pos_x;
            smallX = x;
        } else {
            bigX = x;
            smallX = pos_x;
        }

        if (this.pos_y > y) {
            bigY = pos_y;
            smallY = y;
        } else {
            bigY = y;
            smallY = pos_y;
        }

        xy[0] = bigX - smallX;
        xy[1] = bigY - smallY;

        return xy;
    }

    public List<Piece> checkX(char[][] board) { //returns all possible movements for X shaped movement (ie bishop, queen)
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
        if (btmPathLength < lefPathLength)
            blPathLength = topPathLength;
        else
            blPathLength = lefPathLength;

        tlPathLength = Math.min(topPathLength, lefPathLength);
        //checks top right path
        trLoop:
        {
            for (int i = 1; i < trPathLength; i++) {
                Piece space = new Piece(board[pos_x + i][pos_y + i], pos_x + i, pos_y + i);

                if (!isOccupied(space)) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break trLoop;
                } else if (space.team() == this.team()) {
                    break trLoop;
                }
            }
        }
        //checks bottom right path
        brLoop:
        {
            for (int i = 1; i < brPathLength; i++) {
                Piece space = new Piece(board[pos_x + i][pos_y - i], pos_x + i, pos_y - i);

                if (!isOccupied(space)) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break brLoop;
                } else if (space.team() == this.team()) {
                    break brLoop;
                }
            }
        }
        //checks bottom left path
        blLoop:
        {
            for (int i = 1; i < blPathLength; i++) {
                Piece space = new Piece(board[pos_x - i][pos_y - i], pos_x - i, pos_y - i);
                if (!isOccupied(space)) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break blLoop;
                } else if (space.team() == this.team()) {
                    break blLoop;
                }
            }
        }
        //checks top left path
        tlLoop:
        {
            for (int i = 1; i < tlPathLength; i++) {
                Piece space = new Piece(board[pos_x - i][pos_y + i], pos_x - i, pos_y + i);
                if (!isOccupied(space)) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break tlLoop;
                } else if (space.team() == this.team()) {
                    break tlLoop;
                }
            }
        }
        return allMovements;
    }

    public List<Piece> checkCross(char[][] board) { //checks all possible movement spaces for a piece that moves in a cross formation(ie rook queen)
        List<Piece> allMovements = new ArrayList<>();
        int topPathLength = 7 - pos_y;
        int btmPathLength = pos_y;
        int rigPathLength = 7 - pos_x;
        int lefPathLength = pos_x;
        //checks top path
        topLoop:
        {
            for (int i = 1; i < topPathLength; i++) {
                Piece space = new Piece(board[pos_x][pos_y + i], pos_x, pos_y + i);

                if (!isOccupied(space)) {
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
        rightLoop:
        {
        for (int i = 1; i < rigPathLength; i++) {
            Piece space = new Piece(board[pos_x + i][pos_y], pos_x + i, pos_y);

            if (!isOccupied(space)) {
                allMovements.add(space);
            } else if (space.team() != this.team()) {
                allMovements.add(space);
                break rightLoop;
            } else if (space.team() == this.team()) {
                break rightLoop;
            }
        }
    }
        //checks bottom path
        btmLoop:
        {
            for (int i = 1; i < btmPathLength; i++) {
                Piece space = new Piece(board[pos_x][pos_y - i], pos_x, pos_y - i);
                if (!isOccupied(space)) {
                    allMovements.add(space);
                } else if (space.team() != this.team()) {
                    allMovements.add(space);
                    break btmLoop;
                } else if (space.team() == this.team()) {
                    break btmLoop;
                }
            }
        }
    //checks left path
        leftLoop:
        {
            for (int i = 1; i < lefPathLength; i++) {
                Piece space = new Piece(board[pos_x - i][pos_y], pos_x - i, pos_y);
                if (!isOccupied(space)) {
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


    public List<Piece> checkL(char[][] board) { //checks all possible movements for pieces that move in an L shape
        List<Piece> allMovements = new ArrayList<>();
        int longue = 2, shorte = 1, xmod = 1, ymod = 1;
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
            Piece space1 = new Piece(board[pos_x + (longue * xmod)][pos_y + (shorte * ymod)], pos_x, pos_y);
            Piece space2 = new Piece(board[pos_x + (shorte * ymod)][pos_y + (longue * xmod)], pos_x, pos_y);

            if ((space1.pos_x > -1) && (space1.pos_y > -1) && (space1.pos_x < 8) && (space1.pos_y < 8)) {
                if (!(isOccupied(space1)) || (this.team() != space1.team())) {
                    allMovements.add(space1);
                }
            }

            if ((space2.pos_x > -1) && (space2.pos_y > -1) && (space2.pos_x < 8) && (space2.pos_y < 8)) {
                if (!(isOccupied(space2)) || (this.team() != space2.team())) {
                    allMovements.add(space2);
                }
            }

        }

        return allMovements;
    }

    public List<Piece> checkPawn(char[][] board) { //checks all possible movements for pawns
        int direction;
        List<Piece> moveList = new ArrayList<>();

        if (this.team()) {
            direction = 1;
        } else {
            direction = -1;
        }

        Piece move = new Piece(board[this.pos_x][this.pos_y + (direction * 1)], this.pos_x, this.pos_y);
        Piece moveInitial = new Piece(board[this.pos_x][this.pos_y + (direction * 2)], this.pos_x, this.pos_y);
        Piece takeR = new Piece(board[this.pos_x + 1][this.pos_y + (direction * 1)], this.pos_x, this.pos_y);
        Piece takeL = new Piece(board[this.pos_x - 1][this.pos_y + (direction * 1)], this.pos_x, this.pos_y);

        if (isOccupied(takeR) && (this.team() != takeR.team())) {
            moveList.add(takeR);
        }

        if (isOccupied(takeL) && (this.team() != takeL.team())) {
            moveList.add(takeL);
        }

        if ((turnCount == 0) && (!isOccupied(moveInitial))) {
            moveList.add(moveInitial);
        }

        if (!isOccupied(move)) {
            moveList.add(move);
        }


        return moveList;
    }

    public boolean inMovelist(List<Piece> moveList) { //searches movelist to see if an attempted move is valid
        for (int i = 0; i < moveList.size(); i++) {
            if ((this.piece == moveList.get(i).piece) && (this.pos_x == moveList.get(i).pos_x) && (this.pos_x == moveList.get(i).pos_y)) {
                return true;
            }
        }
        return false;
    }

    public void handleMovement(char[][] board, int x, int y) { //moves pieces across board and takes pieces
        if (x > 7)
            x = 7;
        else if (x < 0)
            x = 0;

        if (y > 7)
            y = 7;
        else if (y < 0)
            y = 0;

        Piece target = new Piece(board[x][y], x, y);

        int[] diff = this.returnDifference(x, y);

        char tempPiece = this.piece;

        if ((97 < this.piece) && (this.piece < 123)) {
            tempPiece = (char) (this.piece - 32);
        }

        switch (tempPiece) {
            case 'k': //king
                if ((diff[0] == 1) && (diff[1] == 1)) {
                    if (this.team() != target.team()) {
                        this.takePiece(board, x, y);
                    }
                }
                break;
            case 'q': //queen
                if (target.inMovelist(this.checkCross(board)) || target.inMovelist(this.checkX(board))) {
                    this.takePiece(board, x, y);
                }
                break;
            case 'b': //bishop
                if (target.inMovelist(this.checkX(board))) {
                    this.takePiece(board, x, y);
                }
                break;
            case 'r': //rook
                if (target.inMovelist(this.checkCross(board))) {
                    this.takePiece(board, x, y);
                }
                break;
            case 'n': //knight
                if (target.inMovelist(this.checkL(board))) {
                    this.takePiece(board, x, y);
                }
                break;
            case 'p'://pawn
                if (target.inMovelist(this.checkPawn(board))) {
                    this.takePiece(board, x, y);
                }
                break;
        }
        this.turnCount++;
    }
    //getters and setters
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
