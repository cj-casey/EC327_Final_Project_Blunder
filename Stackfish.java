import Piece;
import java.io;
import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.introcs.*;
import java.util.Map;
import PVS;
import java.util.HashMap;



public class Stackfish{

private HashMap<String, Integer> transpositionTable = new HashMap<>(); //Hashmap for the transposition table. --> Will be used to store the values of the board states that have already been evaluated. 


 int checkingmate = 100000000; //Checkmate --> given an impossible value for other pieces; Best move that can possibly be made in the game.
 int checked = 5000; //Check --> Value given to a unit that puts a king in check, high priority to either block, or take that unit. 

 int pawnmoveup = 50; //Value of pawn moving one square (Turned into a variable in case changed later)


 //^^ Assign values for each piece and certain moves, so that the algorithm can make smarter and better decisions. ^^

    private static final int MAX_DEPTH = 6; // placeholder maximum number for the depth of the sort --> 6.


     public int pawnmove(char board[][], int x, int y){
        static int pawnval;

        if (Movement(char board[][], int x, int y, List<Piece> enemyPieces)){
            pawnval = pawnval + pawnmoveup;
        }

        return pawnval;
     }


    public List<Piece> aliveUnits(){ //Gives back an array of every living unit in the board. This way, the algorithm can know what is and isn't alive on the board at that moment.

        int counter = 0; //Counter for the arraylist
        List<Piece> livingUnits = new ArrayList<>();

            for (int i = 0; i < 8; i++){ //Rows
                for (int j = 0; j < 8; j++){//Columns

                        if (board[i][j].isOccupied()){ 
                            livingUnits.add(board[i][j]); // If the board space is occupied by some unit, then it gets added to the list of living units.
                        }
                }

            }

        return livingUnits;
    }


    
//THE NEXT TWO FUNCTIONS ARE FROM THE PERSPECTIVE OF THE ALGORTIHM, NOT THE PLAYER. --> The algorithm is white, and the player is black.)

    public List<Piece> aliveteamUnits(List<Piece> livingUnits){ //Sorts the living units into friends

        int sizeliving = livingUnits.size();
        List<Piece> friendsteam = new ArrayList<>();
        for (int p = 0; p < sizeliving; p++){

            
            if (!livingUnits.get(p).team()){
                friendsteam.add(livingUnits.get(p)); //If the living unit in the list is black, put it in the friends team for the algorithm.
            }
        }
        return friendsteam;
    }



    public List<Piece> aliveEteamUnits(List<Piece> livingUnits){ //Sorts the living units into enemies

        int sizeliving = livingUnits.size();
        List<Piece> enemysteam = new ArrayList<>();
        for (int p = 0; p < sizeliving; p++){

        
            if (livingUnits.get(p).team()){
                enemysteam.add(livingUnits.get(p)); //If the living unit in the list is white, put it in the enemy team for the algorithm.
            }
        }
        return enemysteam;

    }



    public String getBoardConfig() {
        StringBuilder sb = new StringBuilder();
    
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char piece = board[i][j];
                sb.append(piece);
            }
        }
    
        return sb.toString();
    }

    

    
    // Transposition table to store previously computed positions
    private static Map<String, Integer> transpositionTable = new HashMap<>();

    public int negamax(char[][] board, List<Piece> whitePieces, List<Piece> blackPieces, boolean team_turn, int depth, int alpha, int beta) {
        String positionKey = generatePositionKey(board, whitePieces, blackPieces, team_turn);

        // Check if the position is already in the transposition table
        if (transpositionTable.containsKey(positionKey)) {
            return transpositionTable.get(positionKey);
        }

        // Check if the game has ended or reached the maximum depth
        int gameState = gameState(board, whitePieces, blackPieces, team_turn);
        if (gameState != 0 || depth == 0) {
            int score = evaluatePosition(board, whitePieces, blackPieces, team_turn);
            transpositionTable.put(positionKey, score);
            return score;
        }

        int maxScore = Integer.MIN_VALUE;
        List<Piece> moves = team_turn ? whitePieces : blackPieces;

        for (Piece piece : moves) {
            List<Piece> possibleMoves = piece.possibleMoves(board, team_turn);

            for (Piece move : possibleMoves) {
                char[][] newBoard = copyBoard(board);
                List<Piece> newWhitePieces = copyPieces(whitePieces);
                List<Piece> newBlackPieces = copyPieces(blackPieces);

                move.handleMovement(newBoard, move.getPosX(), move.getPosY(), newWhitePieces, newBlackPieces, new Piece());

                int score = -negamax(newBoard, newWhitePieces, newBlackPieces, !team_turn, depth - 1, -beta, -alpha);

                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);

                if (alpha >= beta) {
                    break; // Beta cutoff
                }
            }
        }

        transpositionTable.put(positionKey, maxScore);
        return maxScore;
    }

    private String generatePositionKey(char[][] board, List<Piece> whitePieces, List<Piece> blackPieces, boolean team_turn) {
        StringBuilder keyBuilder = new StringBuilder();

        // Append the board state
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                keyBuilder.append(board[i][j]);
            }
        }

        // Append the piece positions
        for (Piece piece : whitePieces) {
            keyBuilder.append(piece.getPosX()).append(piece.getPosY());
        }
        for (Piece piece : blackPieces) {
            keyBuilder.append(piece.getPosX()).append(piece.getPosY());
        }

        // Append the team_turn value
        keyBuilder.append(team_turn);

        return keyBuilder.toString();
    }



    
    private int evaluatePosition(char[][] board, List<Piece> whitePieces, List<Piece> blackPieces, boolean team_turn) {
        int whiteScore = 0;
        int blackScore = 0;

        // Evaluate piece values
        for (Piece piece : whitePieces) {
            whiteScore += getPieceValue(piece.getPiece());
        }
        for (Piece piece : blackPieces) {
            blackScore += getPieceValue(piece.getPiece());
        }

        // Evaluate piece mobility
        int whiteMobility = calculateMobility(whitePieces, board);
        int blackMobility = calculateMobility(blackPieces, board);

        // Calculate final score based on piece values and mobility
        int score = (team_turn ? 1 : -1) * (whiteScore - blackScore + whiteMobility - blackMobility);

        return score;
    }

    private int getPieceValue(char piece) {
        // Assign values to different pieces
        switch (piece) {
            case 'P':
            case 'p':
                return 1;
            case 'N':
            case 'n':
                return 3;
            case 'B':
            case 'b':
                return 3;
            case 'R':
            case 'r':
                return 5;
            case 'Q':
            case 'q':
                return 9;
            case 'K':
            case 'k':
                return 100;
            default:
                return 0;
        }
    }

    private int calculateMobility(List<Piece> pieces, char[][] board) {
        int mobility = 0;

        for (Piece piece : pieces) {
            List<Piece> possibleMoves = piece.possibleMoves(board, piece.getPiece() >= 'a');

            mobility += possibleMoves.size();
        }

        return mobility;
    }

    private char[][] copyBoard(char[][] board) {
        char[][] copy = new char[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = board[i][j];
            }
        }

        return copy;
    }

    private List<Piece> copyPieces(List<Piece> pieces) {
        List<Piece> copy = new ArrayList<>();

        for (Piece piece : pieces) {
            Piece copiedPiece = new Piece(piece.getPiece(), piece.getPosX(), piece.getPosY());
            copy.add(copiedPiece);
        }

        return copy;
    }


}