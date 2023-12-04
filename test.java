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

        char[][] board = { //board initialization
            {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'},
            {'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n'},
            {'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b'},
            {'Q', 'P', ' ', ' ', ' ', ' ', 'p', 'q'},
            {'K', 'P', ' ', ' ', ' ', ' ', ' ', 'k'},
            {'B', ' ', 'P', ' ', ' ', ' ', 'p', 'b'},
            {'N', 'P', ' ', 'P', ' ', ' ', 'p', 'n'},
            {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'}};

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

         /*   System.out.println("Black Pieces");
             for(int i = 0; i < blackPieces.size(); i++) {
                System.out.println(blackPieces.get(i));
            } 
            System.out.println("White Pieces");
             for(int i = 0; i < whitePieces.size(); i++) {
                System.out.println(whitePieces.get(i));
            } */
       // String userInput = null;
        //System.out.println("Enter in the coordinates of the Piece you want to move in the format A1:");

       
        Piece queen = new Piece(board,3,7);
        Piece test = new Piece(board,4,0);
        Piece test2 = new Piece(board,0,0);
        System.out.println(test.stalemateOrCheckmate(board, whitePieces, blackPieces));
        queen.handleMovement(board,7,3,whitePieces,blackPieces);
        System.out.println(test.stalemateOrCheckmate(board, whitePieces, blackPieces));

            
    
        
    }
}
