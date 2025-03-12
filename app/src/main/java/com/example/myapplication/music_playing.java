package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class music_playing extends AppCompatActivity {
    private boolean isPlaying = false;
    private boolean isLiked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_playing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.back).setOnClickListener(v -> {
            onBackPressed();
        });

        ImageView playButton = findViewById(R.id.pause);
        ImageView likeButton = findViewById(R.id.like);

        playButton.setOnClickListener(v -> {
            if (isPlaying) {

                playButton.setImageResource(R.mipmap.play2);
                isPlaying = false;
            } else {

                playButton.setImageResource(R.mipmap.pause2);
                isPlaying = true;
            }
        });

        likeButton.setOnClickListener(v -> {
            if (isLiked) {

                likeButton.setImageResource(R.mipmap.heart);
                isLiked = false;
            } else {

                likeButton.setImageResource(R.mipmap.heart_fill);
                isLiked = true;
            }
        });
    }
}