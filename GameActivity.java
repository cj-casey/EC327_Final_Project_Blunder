package com.example.onechess;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ImageView;
import com.google.gson.Gson;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

   private char[][] board = { //board initialization
            {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'},
            {'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n'},
            {'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b'},
            {'Q', 'P', ' ', ' ', ' ', ' ', 'p', 'q'},
            {'K', 'P', ' ', ' ', ' ', ' ', 'p', 'k'},
            {'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b'},
            {'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n'},
            {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'}};
   private int start_x = -1,start_y = -1,end_x = -1,end_y = -1; //placeholder values for movement

    private int turnCount = 0;

    //declaration of white and black pieces
    List<Piece> whitePieces = new ArrayList<>();
    List<Piece> blackPieces = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    /* sends the Score to the loss screen so it can be entered to the MongoDB database
    Intent intent = new Intent(this, DestinationActivity.class);
    intent.putExtra("key", "value"); // replace "key" with your actual key and "value" with actual value
    startActivity(intent);
     */

    //Multiple Intents are needed, one is the Score to send obviously, but with the game loop,
    //the lost pieces are also necessary, create a function that


    protected void onCreate(Bundle savedInstanceState) {
        //run on start of activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //initialize grid and pieces
        Intent intent = getIntent();

        /*if (intent.hasExtra("pieceList")) {
            // Get the list from the Intent
            whitePieces = (List<Piece>) intent.getSerializableExtra("pieceList");
        } else {
            // Initialize a new list
            initializePieceList(true);
        } */
        updateGrid();
        initializePieceList(true);

        //creates shop and leaderboard buttons
        Button shopButton = findViewById(R.id.quitButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ChessSetupActivity
                Intent intent = new Intent(GameActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

        Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ChessSetupActivity
                Intent intent = new Intent(GameActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateGrid() {
        //creates table layout
        TableLayout tableLayout = findViewById(R.id.imageGrid);
        //creates array of images to iterate through
        ImageView[][] pieceViews = new ImageView[8][8];

        for (int i = 7; i > -1; i--) {
            //defines a new row, per row
            TableRow tableRow = new TableRow(this);
            //xml garbo that makes it look nice
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(rowParams);
            for (int j = 0; j < 8; j++) {
                //grabs the char on the board for drawing correct image
                char piece = board[j][i];

                // Create an ImageView
                ImageView imageView = new ImageView(this);
                pieceViews[i][j] = imageView;
                // Set image resource based on the char value
                setImageResource(imageView, piece);

                // set layout parameters for the ImageView
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 14, 14, 10);  //spacing between pieces

                // Add the ImageView to the TableRow
                tableRow.addView(imageView, params); //adds it to screen

            }
            // Add the TableRow to the TableLayout
            tableLayout.addView(tableRow);
        }
        //creates click listeners for every image, and calls the handlePieceClick function
        for (int i = 7; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                final int x = j;
                final int y = i;

                pieceViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle the click event
                        System.out.println("Updated:" + x + y);
                        handlePieceClick(x, y);
                    }
                });
            }
        }
    }
    public void clearGrid() {
        //this function is identical to initialize grid, except it overrites the existing grid
        TableLayout tableLayout = findViewById(R.id.imageGrid);
        // Clear the existing TableLayout (remove all existing ImageViews)
        tableLayout.removeAllViews();
    }

    private void initializePieceList(boolean firstTime)
    { //self-explanatory
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8 ;j++) {
                Piece Space = new Piece(board,i,j);
                if (Space.team() && (Space.isOccupied()) && firstTime)
                {
                    whitePieces.add(Space);
                }
                else if (Space.isOccupied())
                {
                    blackPieces.add(Space);
                }
            }
        }
    }
        private void handlePieceClick(int x, int y) {
            boolean valid;
            // if the start was not chosen, set the values
            if(start_x == -1)
            {
                if(board[x][y] != ' ') {
                    start_x = x;
                    start_y = y;
                }
            }
            else
            { //if start was chosen, pick end values and call the handleMovement function
                end_x = x;
                end_y = y;

                Piece chosenPiece = new Piece(board,start_x,start_y);

                if((turnCount % 2 == 0 && chosenPiece.team()) || (turnCount % 2 != 0 && !chosenPiece.team())) {
                    //removes the old version from piece list for updating
                    if (chosenPiece.team()) {
                        whitePieces.remove(chosenPiece.findPiece(whitePieces));
                    } else {
                        blackPieces.remove(chosenPiece.findPiece(blackPieces));
                    }
                    valid = chosenPiece.handleMovement(board, end_x, end_y, whitePieces, blackPieces);
                    //re-adds new pieces
                    if (chosenPiece.team()) {
                        whitePieces.add(chosenPiece);
                    } else {
                        blackPieces.add(chosenPiece);
                    }
                    if(valid) {turnCount++;}
                }
                    //move is over, reset values, update grid
                    start_x = -1;
                    start_y = -1;
                    clearGrid();
                    updateGrid();

            }

            // Perform actions based on the clicked piece
        }

    private void setImageResource(ImageView imageView, char value) {
        // Set image resource based on the char value
        // You need to define the logic for mapping char values to image resources
        // For example, you can use a switch statement or a map
        switch (value) {
            case 'k':
                imageView.setImageResource(R.drawable.b_king);
                return;
            case 'K':
                imageView.setImageResource(R.drawable.w_king);
                return;
            case 'q':
                imageView.setImageResource(R.drawable.b_queen);
                return;
            case 'Q':
                imageView.setImageResource(R.drawable.w_queen);
                return;
            case 'b':
                imageView.setImageResource(R.drawable.b_bishop);
                return;
            case 'B':
                imageView.setImageResource(R.drawable.w_bishop);
                return;
            case 'n':
                imageView.setImageResource(R.drawable.b_knight);
                return;
            case 'N':
                imageView.setImageResource(R.drawable.w_knight);
                return;
            case 'r':
                imageView.setImageResource(R.drawable.b_rook);
                return;

            case 'R':
                imageView.setImageResource(R.drawable.w_rook);
                return;

            case 'p':
                imageView.setImageResource(R.drawable.b_pawn);
                return;
            case 'P':
                imageView.setImageResource(R.drawable.w_pawn);
                return;
            case ' ':
                imageView.setImageResource(R.drawable.blank);
        }
    }
}

