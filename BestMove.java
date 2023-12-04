package com.example.onechess;


    public class BestMove{
        int x, y;
        int score;
        char piece;

        public BestMove(int score,char[][]board,int x,int y){
            this.piece=board[x][y];
            this.x = x;
            this.y = y;
            this.score=score;
        } //This will save the best move for the algorithm, as well as the score that it would provide.

    }

