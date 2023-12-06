package com.example.onechess;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    final int[] score = new int[1];
    String username;

    private MediaPlayer music;

    private static List<MediaPlayer> activePlayers = new ArrayList<>();
    class Winner
    {
        String name;
        int score;
        public Winner(String name, int score) {
            // constructor, self-explanatory
            this.name = name;
            this.score = score;
        }


    }
    class ScoreComparator implements Comparator<Winner> {
        public int compare(Winner a, Winner b) {
            return a.score - b.score;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        //run on start of activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        //grabs data from previous activities
        Intent intent = getIntent();
        startMusic(music);
        if(intent.hasExtra("score")) {
            score[0] = intent.getIntExtra("score", 0);
        }
        else {
            score[0] = -1;
        }
        if(intent.hasExtra("username")) {
            username = intent.getStringExtra("username");
        }
        else {
            username = "Gringus";
        }


        List<Winner> leaderboard = new ArrayList<>();
        leaderboard.add(new Winner("Gringus",9998));
        leaderboard.add(new Winner("Connus",7000));
        leaderboard.add(new Winner("Bogdus",5000));
        leaderboard.add(new Winner("Logus",3000));
        leaderboard.add(new Winner("Amadus",1000));
        leaderboard.add(new Winner(username,score[0]));

        Collections.sort(leaderboard, new ScoreComparator());

        leaderboard.remove(0);

        for(int i = 0; i < leaderboard.size();i++)
        {
            switch (i)
            {
                case 0:
                    TextView firstText = findViewById(R.id.firstText);
                    TextView firstScore = findViewById(R.id.firstScore);
                    firstText.setText(leaderboard.get(4).name);
                    firstScore.setText(String.valueOf(leaderboard.get(4).score)+ " ");
                    break;
                case 1:
                    TextView secondText = findViewById(R.id.secondText);
                    TextView secondScore = findViewById(R.id.secondScore);
                    secondText.setText(leaderboard.get(3).name);
                    secondScore.setText(String.valueOf(leaderboard.get(3).score)+ " ");
                    break;
                case 2:
                    TextView thirdText = findViewById(R.id.thirdText);
                    TextView thirdScore = findViewById(R.id.thirdScore);
                    thirdText.setText(leaderboard.get(2).name);
                    thirdScore.setText(String.valueOf(leaderboard.get(2).score)+ " ");
                    break;
                case 3:
                    TextView fourthText = findViewById(R.id.fourthText);
                    TextView fourthScore = findViewById(R.id.fourthScore);
                    fourthText.setText(leaderboard.get(1).name);
                    fourthScore.setText(String.valueOf(leaderboard.get(1).score)+ " ");

                    break;
                case 4:
                    TextView fifthText = findViewById(R.id.fifthText);
                    TextView fifthScore = findViewById(R.id.fifthScore);
                    fifthText.setText(leaderboard.get(0).name);
                    fifthScore.setText(String.valueOf(leaderboard.get(0).score) + " ");
                    break;
            }
        }

        Button startButton = findViewById(R.id.returnButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ChessSetupActivity
                Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
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
        music = MediaPlayer.create(this, R.raw.leaderboard_music);

        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        music.setLooping(true);
        activePlayers.add(music);
        music.start();
    }
}
