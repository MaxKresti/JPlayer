package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class tracks extends AppCompatActivity {
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.media); // Установите выбранный элемент

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    Intent homeIntent = new Intent(tracks.this, main.class);
                    startActivity(homeIntent);
                    return true; // Не вызываем finish()
                } else if (itemId == R.id.media) {
                    // Уже на экране медиа
                    return true;
                } else if (itemId == R.id.plus) {
                    Intent plusIntent = new Intent(tracks.this, add_new.class);
                    startActivity(plusIntent);
                    return true; // Не вызываем finish()
                }

                return false;
            }
        });

        Button tracksButton = findViewById(R.id.tracksButton);
        Button albumsButton = findViewById(R.id.albumsButton);
        Button playlistsButton = findViewById(R.id.playlistsButton);

        tracksButton.setOnClickListener(v -> {
            // Уже на экране треков
        });

        albumsButton.setOnClickListener(v -> {
            Intent intent = new Intent(tracks.this, albums.class);
            startActivity(intent);
        });

        playlistsButton.setOnClickListener(v -> {
            Intent intent = new Intent(tracks.this, playlists.class);
            startActivity(intent);
        });

        setupTrackClickListeners();
        setupCompactPlayerListeners(); // Добавляем обработчики для compact_player
    }

    private void setupTrackClickListeners() {
        View track1 = findViewById(R.id.track1);
        View track2 = findViewById(R.id.track2);
        View track3 = findViewById(R.id.track3);

        if (track1 != null) {
            View trackImage1 = track1.findViewById(R.id.trackImage);
            View trackName1 = track1.findViewById(R.id.trackName);
            View trackAuthor1 = track1.findViewById(R.id.trackAuthor);
            ImageView trackMenu1 = track1.findViewById(R.id.trackMenu);

            trackImage1.setOnClickListener(v -> showCompactPlayer());
            trackName1.setOnClickListener(v -> showCompactPlayer());
            trackAuthor1.setOnClickListener(v -> showCompactPlayer());

            trackMenu1.setOnClickListener(v -> showContextMenu(v));
        }

        if (track2 != null) {
            View trackImage2 = track2.findViewById(R.id.trackImage);
            View trackName2 = track2.findViewById(R.id.trackName);
            View trackAuthor2 = track2.findViewById(R.id.trackAuthor);
            ImageView trackMenu2 = track2.findViewById(R.id.trackMenu);

            trackImage2.setOnClickListener(v -> showCompactPlayer());
            trackName2.setOnClickListener(v -> showCompactPlayer());
            trackAuthor2.setOnClickListener(v -> showCompactPlayer());

            trackMenu2.setOnClickListener(v -> showContextMenu(v));
        }

        if (track3 != null) {
            View trackImage3 = track3.findViewById(R.id.trackImage);
            View trackName3 = track3.findViewById(R.id.trackName);
            View trackAuthor3 = track3.findViewById(R.id.trackAuthor);
            ImageView trackMenu3 = track3.findViewById(R.id.trackMenu);

            trackImage3.setOnClickListener(v -> showCompactPlayer());
            trackName3.setOnClickListener(v -> showCompactPlayer());
            trackAuthor3.setOnClickListener(v -> showCompactPlayer());

            trackMenu3.setOnClickListener(v -> showContextMenu(v));
        }
    }

    private void showCompactPlayer() {
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
        // Обработчик для обложки трека
        ImageView trackCover = findViewById(R.id.trackCover);
        if (trackCover != null) {
            trackCover.setOnClickListener(v -> openMusicPlayingActivity());
        }

        // Обработчик для кнопки воспроизведения/паузы
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
        Intent intent = new Intent(tracks.this, music_playing.class);
        startActivity(intent);
    }

    private void showContextMenu(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.context_menu_track, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
        popupWindow.setElevation(10);

        // Устанавливаем анимацию появления и исчезновения
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        LinearLayout addToPlaylist = popupView.findViewById(R.id.addToPlaylist);
        LinearLayout rename = popupView.findViewById(R.id.rename);
        LinearLayout delete = popupView.findViewById(R.id.delete);

        addToPlaylist.setOnClickListener(v -> {
            Toast.makeText(this, "Add to playlist clicked", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        rename.setOnClickListener(v -> {
            Toast.makeText(this, "Rename clicked", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        delete.setOnClickListener(v -> {
            Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        // Показываем контекстное меню снизу
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);

        // Закрытие меню при касании вне его области
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }
}