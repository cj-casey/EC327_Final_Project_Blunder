package com.example.onechess;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LossActivity extends AppCompatActivity {

    final int[] score = new int[1];
    String username;

    private MediaPlayer music;

    private static List<MediaPlayer> activePlayers = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        //run on start of activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);
        //initialize grid and pieces
        Intent intent = getIntent();
        startMusic(music);
        if(intent.hasExtra("score")) {
            score[0] = intent.getIntExtra("score", 0);
        }
        else {
            score[0] = -1;
        }
        TextView scoreMessage = findViewById(R.id.scoreMessage);
        scoreMessage.setText("YOU SCORED " + score[0]+ " POINTS!");
        EditText editText = findViewById(R.id.enterUserInfo);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called to notify you that, within s, the count characters
                // beginning at start are about to be replaced by new text with length after.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called to notify you that, within s, the count characters
                // beginning at start have just replaced old text that had length before.
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called to notify you that, somewhere within s, the text has been changed.
                // You can add your code here that will be executed after the user finishes inputting.
                username = s.toString();

            }

        });
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ChessSetupActivity
                Intent intent = new Intent(LossActivity.this, LeaderboardActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("score", score[0]);
                if(activePlayers.size() != 0) {
                    activePlayers.get(0).stop();
                    activePlayers.get(0).release();
                    activePlayers.remove(0);
                }
                startActivity(intent);
                finish();
            }
        });



    }
    public void startMusic(MediaPlayer music)
    {
        int index = (int) (Math.random()*(2));
        music = MediaPlayer.create(this, R.raw.main_music);

        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        music.setLooping(true);
        activePlayers.add(music);
        music.start();
    }


}
