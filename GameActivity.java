package com.example.onechess;


import static java.sql.Types.DOUBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

   private String[] dialogue =
           {"You don't stand a chance....",
           "You're a total dweeb..",
           "You'll never win, just lose..",
           "You're just a failure...."    ,
           "You lack what it takes...."   ,
           "Gringus never blunders...."   ,
           "Well you blundered it now....",
           "I'm literally a wizard...."   ,
           "Do you have a brain? "      ,
           "This is just a side hustle...",
           "Can't wait to take your money"};



    private int start_x = -1,start_y = -1,end_x = -1,end_y = -1; //placeholder values for movement

    private int moveCount = 0;
    private int turnCount = 0;
    int gameState = 0; //0 if game ongoing, 1 if user won, -1 if user lost, 2 if stalemate

    //declaration of white and black pieces
    List<Piece> whitePieces = new ArrayList<>();
    List<Piece> blackPieces = new ArrayList<>();
    private MediaPlayer music;

    private static List<MediaPlayer> activePlayers = new ArrayList<>(); //keeps it in a list so garbo collector doesnt feast
    Fish fish = new Fish();
    int score;
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
        //if user came from shop, fill board and score based on that list, otherwise create new
        if (intent.hasExtra("pieceList")) {
            // Get the list from the Intent
            whitePieces = (List<Piece>) intent.getSerializableExtra("pieceList");
            initializePieceList(false);
            fillBoard();
        } else {
            // Initialize a new list
            initializePieceList(true);
        }
        if(intent.hasExtra("score"))
        {
            score = (Integer) intent.getIntExtra("score",0);
        }
        else {
            score = 0;
        }
        MediaPlayer music = new MediaPlayer();
        startMusic(music);
        updateGrid();

        //creates shop and leaderboard buttons
    }
    private void checkGamestate()
    {
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;
        if(whitePieces.size() != 0) {
            whiteKingAlive = whitePieces.get(0).findKings(board);
        }
        if(blackPieces.size() != 0) {
            blackKingAlive = blackPieces.get(0).findKings(board);
        }

        if(whiteKingAlive && blackKingAlive)
        {
            gameState = 0;
        }
        else if(whiteKingAlive)
        {
            gameState = 1;
        }
        else if(blackKingAlive)
        {
            gameState = -1;
        }


        if(gameState == 1)
        { //draw a round win thingy
            Intent intentWin = new Intent(GameActivity.this, ShopActivity.class);
            score = (int) ((((double) whitePieces.size()/(turnCount+1)*1200 + score)));
            intentWin.putExtra("score",score);
            intentWin.putExtra("pieceList",(Serializable) whitePieces);
            if(activePlayers.size() != 0) {
            activePlayers.get(0).stop();
            activePlayers.get(0).release();
            activePlayers.remove(0);
            }
            startActivity(intentWin);
            finish();
        }
        else if(gameState == -1)
        {
            Intent intentLoss = new Intent(GameActivity.this, LossActivity.class);
            intentLoss.putExtra("score", score);
            if(activePlayers.size() != 0) {
                activePlayers.get(0).stop();
                activePlayers.get(0).release();
                activePlayers.remove(0);
            }

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
            startActivity(intentLoss);
            finish();
                }
            }, 500);
        }
    }
    private void fillBoard()
    {//fills board with players pieces
        //clears default white pieces
        for(int i = 0; i < 8;i++)
        {
            for(int j = 0; j < 2; j++)
            {
                board[i][j] = ' ';
            }
        }
        int pawnCount = 0;
        int knightCount = 0;
        int bishopCount = 0;
        int rookCount = 0;
        //fills pieces left to right
        for(int i = 0; i < whitePieces.size(); i++)
        {
            switch(whitePieces.get(i).getPiece())
            {
                case 'K':
                    board[4][0] = 'K';
                    whitePieces.get(i).setPosX(4);
                    whitePieces.get(i).setPosY(0);
                    break;
                case 'Q':
                    board[3][0] = 'Q';
                    whitePieces.get(i).setPosX(3);
                    whitePieces.get(i).setPosY(0);
                    break;
                case 'R':
                    board[0+rookCount][0] = 'R';
                    whitePieces.get(i).setPosX(0+rookCount);
                    whitePieces.get(i).setPosY(0);
                    rookCount+=7;
                    break;
                case 'B':
                    board[2+bishopCount][0] = 'B';
                    whitePieces.get(i).setPosX(2+bishopCount);
                    whitePieces.get(i).setPosY(0);
                    bishopCount+=3;
                    break;
                case 'N':
                    board[1+knightCount][0] = 'N';
                    whitePieces.get(i).setPosX(1+knightCount);
                    whitePieces.get(i).setPosY(0);
                    knightCount+=5;
                    break;
                case 'P':
                    board[0+pawnCount][1] = 'P';
                    whitePieces.get(i).setPosX(0+pawnCount);
                    whitePieces.get(i).setPosY(1);
                    pawnCount++;
                    break;
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        whitePieces.clear();
        blackPieces.clear();

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    public void startMusic(MediaPlayer music)
    {
        int index = (int) (Math.random()*(2));

        switch(index) {
            case 0:
            music = MediaPlayer.create(this, R.raw.game_music_1);
            break;
            case 1:
            music = MediaPlayer.create(this, R.raw.game_music_2);
            break;
            case 2:
            music = MediaPlayer.create(this, R.raw.game_music_3);
        }
        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        music.setLooping(true);
        activePlayers.add(music);
        music.start();
    }
    private void updateGrid() {
        //creates table layout
        gringusText();
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
                        handlePieceClick(x, y);
                        checkGamestate();
                        //refresh click



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
                else if (Space.isOccupied() && !Space.team())
                {
                    blackPieces.add(Space);
                }
            }
        }
    }
        private void handlePieceClick(int x, int y) {
            boolean valid;

            // if the start was not chosen, set the values
                if (start_x == -1 && (turnCount % 2 == 0)) {
                    if (board[x][y] != ' ') { ////////
                        start_x = x;
                        start_y = y;
                    }
                } else if((turnCount % 2 == 0)) { //if start was chosen, pick end values and call the handleMovement function
                    end_x = x;
                    end_y = y;

                    Piece chosenPiece = new Piece(board, start_x, start_y);
                    if(chosenPiece.team()) {
                        List<Piece> teamPieces = (chosenPiece.team()) ? whitePieces : blackPieces;
                        chosenPiece.setTurnCount(teamPieces.get(chosenPiece.findPiece(teamPieces)).getTurnCount());
                        //removes the old version from piece list for updating
                        if (chosenPiece.team()) {
                            whitePieces.remove(chosenPiece.findPiece(whitePieces));
                        } else {
                            blackPieces.remove(chosenPiece.findPiece(blackPieces));
                        }
                        valid = chosenPiece.handleMovement(board, end_x, end_y, whitePieces, blackPieces);
                        moveCount = 0;
                        //re-adds new pieces
                        if (chosenPiece.team()) {
                            whitePieces.add(chosenPiece);
                        } else {
                            blackPieces.add(chosenPiece);
                        }
                        if (valid) {
                            turnCount++;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    // Code to execute after some delay
                                    botMove();
                                }
                            }, 500); // The delay in milliseconds, 5000ms = 5 seconds

                        }
                    }
                    //move is over, reset values, update grid
                    start_x = -1;
                    start_y = -1;
                    clearGrid();
                    updateGrid();
                }
            }

    public void gringusText()
    {
        int randomChoice =(int) (Math.random()*(dialogue.length-1));
        TextView gringusSpeech = findViewById(R.id.gringusSpeechGame);
        gringusSpeech.setText(dialogue[randomChoice]);
    }
            // Perform actions based on the clicked piece
    public void botMove()
    {
        boolean valid = false;
        if(turnCount % 2 != 0 && moveCount == 0) {
            Collections.shuffle(blackPieces);
            //if no possible moves, return false to end game as a stalemate
            //prioritize king
            for(int i = 0; i < blackPieces.size();i++)
            {
                List<Piece> kingMoveList;
                if(blackPieces.get(i).getPiece() == 'k')
                {
                    kingMoveList = blackPieces.get(i).checkKing(board,whitePieces,blackPieces);
                }
                else
                {
                    kingMoveList = blackPieces.get(i).possibleMoves(board, false);
                }
                    for(int j = 0; j < kingMoveList.size();j++)
                    {
                        if(kingMoveList.get(j).getPiece() == 'K')
                        {
                            blackPieces.get(i).handleMovement(board,kingMoveList.get(j).getPosX(),kingMoveList.get(j).getPosY(),whitePieces,blackPieces);
                            moveCount++;
                            turnCount++;
                            return;
                        }
                    }
                }
            //if king cant be taken, it will try to take a piece
            for(int i = 0; i < blackPieces.size();i++)
            {
                List<Piece> takeMoveList;
                if(blackPieces.get(i).getPiece() == 'k')
                {
                    takeMoveList = blackPieces.get(i).checkKing(board,whitePieces,blackPieces);
                }
                else
                {
                    takeMoveList = blackPieces.get(i).possibleMoves(board, false);
                }
                for(int j = 0; j < takeMoveList.size();j++)
                {
                    if(takeMoveList.get(j).isOccupied())
                    {
                        blackPieces.get(i).handleMovement(board,takeMoveList.get(j).getPosX(),takeMoveList.get(j).getPosY(),whitePieces,blackPieces);
                        moveCount++;
                        turnCount++;
                        return;
                    }
                }
            }
            //if it cant take a piece, it will move somewhere random
            while(!valid)
            {
                Piece tempPiece = blackPieces.get((int) Math.floor(Math.random()*(blackPieces.size()-1)));
                List<Piece> tempMoveList = tempPiece.possibleMoves(board,false);
                if(tempMoveList.size() != 0) {
                    Piece movePiece = tempMoveList.get((int) Math.floor(Math.random() * (tempMoveList.size()-1)));
                    valid = tempPiece.handleMovement(board, movePiece.getPosX(), movePiece.getPosY(), whitePieces, blackPieces);
                }
            }
            if (valid) {
                moveCount++;
                turnCount++;
                return;
            }
        }
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

