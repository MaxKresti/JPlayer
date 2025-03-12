package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class main extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Обработчики для кнопок навигации
        ImageView plusButton = findViewById(R.id.plus);
        ImageView mediaButton = findViewById(R.id.media);
        ImageView settingsButton = findViewById(R.id.setting);

        plusButton.setOnClickListener(v -> {
            Intent intent = new Intent(main.this, add_new.class);
            startActivity(intent);
        });

        mediaButton.setOnClickListener(v -> {
            Intent intent = new Intent(main.this, tracks.class);
            startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(main.this, settings.class);
            startActivity(intent);
        });

            View track1 = findViewById(R.id.track1);
            View track2 = findViewById(R.id.track2);
            View track3 = findViewById(R.id.track3);

            track1.setOnClickListener(v -> openMusicPlayingActivity());
            track2.setOnClickListener(v -> openMusicPlayingActivity());
            track3.setOnClickListener(v -> openMusicPlayingActivity());

        }

        private void openMusicPlayingActivity() {
            Intent intent = new Intent(main.this, music_playing.class);
            startActivity(intent);
        }
    }

