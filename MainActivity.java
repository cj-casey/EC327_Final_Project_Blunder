package com.example.onechess;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer music;

    private static List<MediaPlayer> activePlayers = new ArrayList<>(); //keeps it in a list so garbo collector doesnt feast

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Mongo.INSTANCE.ping();
        startMusic(music);
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ChessSetupActivity
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
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
        Log.d("MUSIC INDEX",String.valueOf(index));
        music = MediaPlayer.create(this, R.raw.main_music);
        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        music.setLooping(true);
        activePlayers.add(music);
        music.start();
    }
}
