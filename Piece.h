//
// Created by vindi on 11/17/2023.
//

#ifndef PIRATECHESS_PIECE_H
#define PIRATECHESS_PIECE_H

namespace Piece {

    class Piece
            {
            public:
                 char piece; //can be K, Q, B, R, N, P or k, q, b, r, n, p
                 int pos_x; //horizontal position
                 int pos_y; //vertical position
                 bool canBeTaken; //for kings
                 bool canBePromoted; //for pawns

                 Piece(char piece, int x, int y); //constructor
                 bool team(); //returns the true if white, false if black
                 void handleMovement(char board[8][8], int x, int y);
                 bool isOccupied(char board[8][8],int x, int y);
                 void takePiece(char board[8][8],int x, int y);
                 int* returnDifference(int x, int y);


    };

} // Piece

#endif //PIRATECHESS_PIECE_H
