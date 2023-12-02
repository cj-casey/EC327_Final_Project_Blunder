package com.example.onechess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.onechess.Piece;

import java.io.Serializable;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        //run on start of activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        //initialize grid and pieces
        Intent intent = getIntent();
        // Get the list from the Intent
        List<Piece> playerPieces = (List<Piece>) intent.getSerializableExtra("pieceList");


        //creates shop and leaderboard buttons
        Button shopButton = findViewById(R.id.exitButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ChessSetupActivity
                Intent intent = new Intent(ShopActivity.this, GameActivity.class);
                intent.putExtra("pieceList", (Serializable) playerPieces);
                startActivity(intent);
            }
        });

    }
}
