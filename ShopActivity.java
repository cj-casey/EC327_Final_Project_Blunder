package com.example.onechess;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.onechess.Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    int pawnCount = 0;
    protected void onCreate(Bundle savedInstanceState) {
        //run on start of activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        //initialize grid and pieces
        Intent intent = getIntent();
        // Get the list from the Intent
        final int[] score = new int[1];

            List<Piece> playerPieces = (List<Piece>) intent.getSerializableExtra("pieceList");


        if(intent.hasExtra("score")) {
            score[0] = intent.getIntExtra("score", 0);
        }
        else {
            score[0] = -1;
        }
        updateScore(score[0]);

        //sets the values of the players inventory according to their surviving pieces
        TextView pawnCountTextDisplay = findViewById(R.id.pawnCountText);
        pawnCountTextDisplay.setText(String.valueOf(pieceCount('P',playerPieces)));
        TextView knightCountTextDisplay = findViewById(R.id.knightCountText);
        knightCountTextDisplay.setText(String.valueOf(pieceCount('N',playerPieces)));
        TextView bishopCountTextDisplay = findViewById(R.id.bishopCountText);
        bishopCountTextDisplay.setText(String.valueOf(pieceCount('B',playerPieces)));
        TextView rookCountTextDisplay = findViewById(R.id.rookCountText);
        rookCountTextDisplay.setText(String.valueOf(pieceCount('R',playerPieces)));
        TextView queenCountTextDisplay = findViewById(R.id.queenCountText);
        queenCountTextDisplay.setText(String.valueOf(pieceCount('Q',playerPieces)));


        //creates shop and leaderboard buttons
        Button shopButton = findViewById(R.id.exitButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ChessSetupActivity
                Intent intent = new Intent(ShopActivity.this, GameActivity.class);
                intent.putExtra("pieceList", (Serializable) playerPieces);
                intent.putExtra("score", score[0]);
                startActivity(intent);
                finish();
            }
        });
        ImageView pawn = findViewById(R.id.pawn);
        pawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Score", "Score: " + score[0]);
                int pawnNumber = pieceCount('P',playerPieces);
                if(pawnNumber < 8 && score[0] >= 100) {
                    String pawnCount = String.valueOf(pawnNumber + 1);
                    playerPieces.add(new Piece('P', 0, 0));
                    pawnCountTextDisplay.setText(pawnCount);
                    score[0]-=100;
                }

                updateScore(score[0]);
            }
        });

        ImageView knight = findViewById(R.id.knight);
        knight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int knightNumber = pieceCount('N',playerPieces);
                if(knightNumber < 2 && score[0] >= 300) {
                    String knightCount = String.valueOf(knightNumber + 1);
                    playerPieces.add(new Piece('N', 0, 0));
                    knightCountTextDisplay.setText(knightCount);
                    score[0]-=300;
                }

                updateScore(score[0]);
            }
        });

        ImageView bishop = findViewById(R.id.bishop);
        bishop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bishopNumber = pieceCount('B',playerPieces);
                if(bishopNumber < 2 && score[0] >= 300) {
                    String bishopCount = String.valueOf(bishopNumber + 1);
                    playerPieces.add(new Piece('B', 0, 0));
                    bishopCountTextDisplay.setText(bishopCount);
                    score[0]-=300;
                }
                updateScore(score[0]);
            }
        });

        ImageView rook = findViewById(R.id.rook);
        rook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rookNumber = pieceCount('R',playerPieces);
                if(rookNumber < 2 && score[0] >= 500) {
                    String rookCount = String.valueOf(rookNumber + 1);
                    playerPieces.add(new Piece('R', 0, 0));
                    rookCountTextDisplay.setText(rookCount);
                    score[0]-=500;
                }
                updateScore(score[0]);
            }
        });

        ImageView queen = findViewById(R.id.queen);
        queen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int queenNumber = pieceCount('Q',playerPieces);
                if(queenNumber < 1 && score[0] >= 900) {
                    String queenCount = String.valueOf(queenNumber + 1);
                    playerPieces.add(new Piece('Q', 0, 0));
                    queenCountTextDisplay.setText(queenCount);
                    score[0]-=300;
                }
                updateScore(score[0]);
            }
        });
    }

    public void updateScore(int score)
    {
        TextView scoreCount = findViewById(R.id.scoreCount);
        String scoreText = String.valueOf(score);
        scoreCount.setText(scoreText);
    }

    public int pieceCount(char piece, List<Piece> pieceList)
    {
        int count = 0;

        for(int i = 0; i < pieceList.size();i++)
        {
            if(pieceList.get(i).getPiece() == piece)
            {
                count++;
            }
        }
        return count;
    }
}
