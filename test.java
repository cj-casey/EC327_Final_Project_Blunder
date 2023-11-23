import java.io.*;
import java.util.Map;
import java.util.HashMap;
import Piece;

public class test {

    public static void drawBoard(char[][] board)
    {
        for (int j = 7; j > -1; j--) {
            for (int i = 0; i < 8; i++) {
                System.out.print(board[j][i]);
            }
            System.out.println();
        }
    }

   

    public static void main(String args[]) {

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        Map<Character,Integer> xcoordinate = Map.of('A',0,'B',1,'C',2,'D',3,'E',4,'F',5,'G',6,'H',7);
        Map<Character,Integer> ycoordinate = Map.of('1',0,'2',1,'3',2,'4',3,'5',4,'6',5,'7',6,'8',7);

        char[] bbackRow = {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'};
        char[] bfrontRow = {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'};
        char[] wbackRow = {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};
        char[] wfrontRow = {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'};
        char[] emptyRow = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

        char[][] board = {wbackRow, wfrontRow, emptyRow, emptyRow, emptyRow, emptyRow, bfrontRow, bbackRow};

        drawBoard(board);

    
        String userInput;
        System.out.println("Enter in the coordinates of the Piece you want to move in the format A1:");
        try {
            // existing code for reading user input
        
        userInput = buffer.readLine();
        Piece chosenPiece = new Piece(board[ycoordinate.get(userInput.charAt(1))][xcoordinate.get(userInput.charAt(0))],ycoordinate.get(userInput.charAt(1)),xcoordinate.get(userInput.charAt(0)));
        System.out.println("Enter in the coordinates of the place you want to move in the format A1:");
       userInput = buffer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chosenPiece.handleMovement(board,ycoordinate.get(userInput.charAt(1)),xcoordinate.get(userInput.charAt(0)));
        drawBoard(board);

        

        

    }
}