package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class main extends AppCompatActivity {

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    // Уже на главном экране
                    return true;
                } else if (itemId == R.id.media) {
                    // Переход на экран медиа
                    Intent mediaIntent = new Intent(main.this, tracks.class);
                    startActivity(mediaIntent);
                    return true;
                } else if (itemId == R.id.plus) {
                    // Переход на экран добавления
                    Intent plusIntent = new Intent(main.this, add_new.class);
                    startActivity(plusIntent);
                    return true;
                }

                return false;
            }
        });

        // Обработчики для треков
        setupTrackClickListeners();

        // Обработчики для compact_player
        setupCompactPlayerListeners();
    }

    private void setupTrackClickListeners() {
        View track1 = findViewById(R.id.track1);
        View track2 = findViewById(R.id.track2);
        View track3 = findViewById(R.id.track3);

        if (track1 != null) {
            track1.setOnClickListener(v -> showCompactPlayer());
        }

        if (track2 != null) {
            track2.setOnClickListener(v -> showCompactPlayer());
        }

        if (track3 != null) {
            track3.setOnClickListener(v -> showCompactPlayer());
        }
    }

    private void showCompactPlayer() {
        // Находим compact_player в layout
        View compactPlayer = findViewById(R.id.compact_player);
        if (compactPlayer != null) {
            compactPlayer.setVisibility(View.VISIBLE);
        }
    }

    private void hideCompactPlayer() {
        View compactPlayer = findViewById(R.id.compact_player);
        if (compactPlayer != null) {
            compactPlayer.setVisibility(View.GONE);
        }
    }

    private void setupCompactPlayerListeners() {
        ImageView trackCover = findViewById(R.id.trackCover);
        if (trackCover != null) {
            trackCover.setOnClickListener(v -> openMusicPlayingActivity());
        }

        ImageButton playPauseButton = findViewById(R.id.playPauseButton);
        if (playPauseButton != null) {
            playPauseButton.setOnClickListener(v -> togglePlayPause(playPauseButton));
        }

        // Обработчик для кнопки закрытия
        ImageButton closeButton = findViewById(R.id.closeButton);
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> hideCompactPlayer());
        }
    }
    private void togglePlayPause(ImageButton playPauseButton) {
        if (isPlaying) {
            // Анимация перехода к иконке паузы
            ObjectAnimator.ofFloat(playPauseButton, "alpha", 1f, 0f).setDuration(150).start();
            playPauseButton.postDelayed(() -> {
                playPauseButton.setImageResource(R.mipmap.pause2);
                ObjectAnimator.ofFloat(playPauseButton, "alpha", 0f, 1f).setDuration(150).start();
            }, 150);
        } else {
            // Анимация перехода к иконке воспроизведения
            ObjectAnimator.ofFloat(playPauseButton, "alpha", 1f, 0f).setDuration(150).start();
            playPauseButton.postDelayed(() -> {
                playPauseButton.setImageResource(R.mipmap.play2);
                ObjectAnimator.ofFloat(playPauseButton, "alpha", 0f, 1f).setDuration(150).start();
            }, 150);
        }
        isPlaying = !isPlaying;
    }


    private void openMusicPlayingActivity() {
        Intent intent = new Intent(main.this, music_playing.class);
        startActivity(intent);
    }
}