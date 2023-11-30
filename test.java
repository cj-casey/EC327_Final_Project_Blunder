import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import Piece.Piece;

import java.util.HashMap;

public class test {

    public static void drawBoard(char[][] board)
    {
        for (int j = 7; j > -1; j--) {
            for (int i = 0; i < 8; i++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

   

    public static void main(String args[]) {

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        Map<Character,Integer> xcoordinate = Map.of('A',0,'B',1,'C',2,'D',3,'E',4,'F',5,'G',6,'H',7);
        Map<Character,Integer> ycoordinate = Map.of('1',0,'2',1,'3',2,'4',3,'5',4,'6',5,'7',6,'8',7);

        char[] rookCol = {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'};
        char[] rookCol2 = {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'};
        char[] knightCol = {'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n'};
        char[] knightCol2 = {'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n'};
        char[] bishopCol = {'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b'};
        char[] bishopCol2 = {'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b'};
        char[] queenCol = {'Q', 'P', ' ', ' ', ' ', ' ', 'p', 'q'};
        char[] kingCol = {'K', 'P', ' ', ' ', ' ', ' ', 'p', 'k'};
        char[][] board = {rookCol, knightCol, bishopCol, queenCol, kingCol, bishopCol2, knightCol2, rookCol2}; //fuck this thing

        List<Piece> whitePieces = new ArrayList<>();
        List<Piece> blackPieces = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8 ;j++) {
                Piece Space = new Piece(board,i,j);
                if (Space.team() && (Space.isOccupied()))
                {
                    whitePieces.add(Space);
                }
                else if (Space.isOccupied())
                {
                    blackPieces.add(Space);
                }
                }
            }
           /* System.out.println("Black Pieces");
             for(int i = 0; i < blackPieces.size(); i++) {
                System.out.println(blackPieces.get(i));
            } */
       // String userInput = null;
        //System.out.println("Enter in the coordinates of the Piece you want to move in the format A1:");

       
        drawBoard(board);

        Piece test = new Piece(board,4,0);
        test.handleMovement(board,5,1,blackPieces);
        drawBoard(board);
       
    }
}
