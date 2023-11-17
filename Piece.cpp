//
// Created by vindi on 11/17/2023.
//

#include "Piece.h"

namespace Piece {
    Piece::Piece(char piece, int x, int y)
    {
        this->piece = piece;
        this->pos_x = x;
        this->pos_y = y;
    }

    bool Piece::team()
    {
        if((64 < piece) && (piece < 96))
        {
            return true; //returns true if its white
        }
        else
            return false; // returns false if its black
    }
    void Piece::takePiece(char board[8][8],int x, int y)
    {
        Piece movingPiece(board[this->pos_x][this->pos_y], this->pos_x, this->pos_y);
        Piece takenPiece(board[x][y],x,y);

        bool sameTeam = (movingPiece.team() == takenPiece.team());
        //piece1.team() == piece2.team()
        if(!sameTeam)
        { //sets char of new space to the moving piece changes pos values and sets old spot to " "
            board[x][y] = this->piece;
            board[this->pos_x][this->pos_y] = ' ';
            this->pos_x = x;
            this-> pos_y = y;
        }
    }
    bool Piece::isOccupied(char board[8][8],int x, int y)
    {
        if(board[x][y] != ' ')
        {
            return true;
        }
        else
            return false;
    }

    int* Piece::returnDifference(int x, int y)
    {
        int bigX, bigY,smallX,smallY;
        int* xy = new int[2];

        if(this->pos_x > x) {
            bigX = pos_x;
            smallX = x;
        }
        else
        {
            bigX = x;
            smallX = pos_x;
        }

        if(this->pos_y > y) {
            bigY = pos_y;
            smallY = y;
        }
        else
        {
            bigY = y;
            smallY = pos_y;
        }

        xy[0] = bigX-smallX;
        xy[1] = bigY-smallY;

        return xy;
    }
    void Piece::handleMovement(char board[8][8], int x, int y)
    {
        Piece pieceTemp(piece,this->pos_x,this->pos_y);
        Piece target(board[x][y],x,y);

        int* diff = pieceTemp.returnDifference(x,y);

        if ((97 < pieceTemp.piece) && (pieceTemp.piece < 123))
        {
            pieceTemp.piece -= 32;
        }

        switch(pieceTemp.piece)
        {
            case 'k':
                if((diff[0] == 1) && (diff[1] == 1))
                {
                    if(pieceTemp.team() != target.team())
                    {
                        pieceTemp.takePiece(board,x,y);
                    };
                }
                break;
            case 'q':
                break;
            case 'b':
                break;
            case 'r':
                break;
            case 'n':
                break;
            case 'p':
                break;
        }

    }


} // Piece