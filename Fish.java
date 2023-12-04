package com.example.onechess;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.io.*;




public class Fish {
    int MAX_Depth = 2; //This is the maximum amount the algorithm will search through before ending runtime.

    int pawn = 1;
    int bishop = 3;
    int knight = 3;
    int rook = 5;
    int queen = 9;
    int king = 10000;
    int score = 0; //Algorithm is in a neural state as soon as it starts --> This allows it to make better decisions.

    boolean maximizing_Player;
    int bestscore = 0; //This integer will return the best possible score that is gotten.
    int depth = 3;

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

        return kings;
    }
/*
    public int addvalB(findKings.get(1), char[][] board, int x, int y){
        int numadd = y + x;

    }

 */
    public int eval(char[][] board, int x, int y,List<Piece> whitePieces){
        for(int i = 0; i < whitePieces.size(); i++){
            switch (board[x][y]){
                //If pieces are black(friendly to Fish), then they are given positive value
                case 'p':
                    score += pawn;
                case 'n':
                    score += knight;
                case 'b':
                    score += bishop;
                case 'r':
                    score += rook;
                case 'q':
                    score += queen;
                case 'k':
                    score += king;

                //If pieces are white(enemy of Fish), then they are given a negative value
                case 'P':
                    score += -(pawn);
                case 'N':
                    score += -(knight);
                case 'B':
                    score += -(bishop);
                case 'R':
                    score += -(rook);
                case 'Q':
                    score += -(queen);
                case 'K':
                    score += -(king);
            }
        }

        return score;
    }




    public BestMove negamax(char[][] board, int x, int y, int depth, double alpha, double beta, List<Piece> whitePieces, List<Piece>blackPieces,boolean checkBlack){
        List<Piece> pieceList = (checkBlack) ? blackPieces:whitePieces;
        BestMove bestMove = new BestMove(Integer.MIN_VALUE, board, 0, 0);
        List<Piece> allMovements = new ArrayList<>();
        for(int k = 0; k < pieceList.size();k++) {
            allMovements.addAll(pieceList.get(k).possibleMoves(board, !checkBlack));
        }
        if(depth == 0){
            bestMove.score = eval(board, x, y, whitePieces);
            bestMove.x = x;
            bestMove.y = y;
            Piece pieceTest = new Piece(board,x,y);
            Log.d("107 EVAL IN MOVE LIST",String.valueOf(pieceTest.inMovelist(allMovements)));
            return bestMove;
        }

            checkLoop:
            for (int j = 0; j < allMovements.size(); j++) {
                char[][] tempBoard = new char[board.length][];
                for (int i = 0; i < board.length; i++) {
                    tempBoard[i] = new char[board[i].length];
                    System.arraycopy(board[i], 0, tempBoard[i], 0, board[i].length);
                }
                int score = -negamax(tempBoard,allMovements.get(j).getPosX(),allMovements.get(j).getPosY(), depth-1,-beta,-alpha,whitePieces,blackPieces,!checkBlack).score;

                if(score > bestMove.score) {
                    bestMove.score = score;
                    bestMove.x = allMovements.get(j).getPosX();
                    bestMove.y = allMovements.get(j).getPosX();
                    bestMove.piece = allMovements.get(j).getPiece();
                }
                alpha = Math.max(alpha, score);
                if (alpha >= beta) {
                    break checkLoop;  // Beta cut-off
                }
            }

        return bestMove;
    }



}