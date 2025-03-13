package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.animation.ObjectAnimator;


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

        playButton.setOnClickListener(v -> togglePlayPause(playButton));
        likeButton.setOnClickListener(v -> toggleLike(likeButton));
    }

    private void togglePlayPause(ImageView playPauseButton) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(playPauseButton, "alpha", 1f, 0f).setDuration(150);
        fadeOut.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isPlaying) {
                    playPauseButton.setImageResource(R.mipmap.play2);
                } else {
                    playPauseButton.setImageResource(R.mipmap.pause2);
                }

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(playPauseButton, "alpha", 0f, 1f).setDuration(150);
                fadeIn.start();
            }
        });

        isPlaying = !isPlaying; // Обновляем состояние
    }

    private void toggleLike(ImageView likeButton) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(likeButton, "alpha", 1f, 0f).setDuration(150);
        fadeOut.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isLiked) {
                    likeButton.setImageResource(R.mipmap.heart);
                } else {
                    likeButton.setImageResource(R.mipmap.heart_fill);
                }

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(likeButton, "alpha", 0f, 1f).setDuration(150);
                fadeIn.start();
            }
        });

        isLiked = !isLiked; // Обновляем состояние
    }
}
