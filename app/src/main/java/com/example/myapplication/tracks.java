package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class tracks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracks);

        // Настройка Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Обработчики для кнопок навигации
        findViewById(R.id.home).setOnClickListener(v -> {
            Intent intent = new Intent(tracks.this, main.class);
            startActivity(intent);
        });

        findViewById(R.id.media).setOnClickListener(v -> {
            Intent intent = new Intent(tracks.this, tracks.class);
            startActivity(intent);
        });

        findViewById(R.id.plus).setOnClickListener(v -> {
            Intent intent = new Intent(tracks.this, add_new.class);
            startActivity(intent);
        });

        // Обработчики для кнопок вкладок
        Button tracksButton = findViewById(R.id.tracksButton);
        Button albumsButton = findViewById(R.id.albumsButton);
        Button playlistsButton = findViewById(R.id.playlistsButton);

        tracksButton.setOnClickListener(v -> {
            // Уже находимся на странице tracks, поэтому ничего не делаем
        });

        albumsButton.setOnClickListener(v -> {
            Intent intent = new Intent(tracks.this, albums.class);
            startActivity(intent);
        });

        playlistsButton.setOnClickListener(v -> {
            Intent intent = new Intent(tracks.this, playlists.class);
            startActivity(intent);
        });

        // Обработчики для элементов item_track
        setupTrackClickListeners();
    }

    // Метод для настройки обработчиков нажатий на элементы item_track
    private void setupTrackClickListeners() {
        // Находим корневые элементы вложенных макетов (item_track)
        View track1 = findViewById(R.id.track1); // Убедитесь, что у include есть id
        View track2 = findViewById(R.id.track2); // Убедитесь, что у include есть id
        // Добавьте остальные элементы, если они есть

        // Обработчики для элементов track1
        if (track1 != null) {
            View trackImage1 = track1.findViewById(R.id.trackImage);
            View trackName1 = track1.findViewById(R.id.trackName);
            View trackAuthor1 = track1.findViewById(R.id.trackAuthor);

            trackImage1.setOnClickListener(v -> openMusicPlayingActivity());
            trackName1.setOnClickListener(v -> openMusicPlayingActivity());
            trackAuthor1.setOnClickListener(v -> openMusicPlayingActivity());
        }

        // Обработчики для элементов track2
        if (track2 != null) {
            View trackImage2 = track2.findViewById(R.id.trackImage);
            View trackName2 = track2.findViewById(R.id.trackName);
            View trackAuthor2 = track2.findViewById(R.id.trackAuthor);

            trackImage2.setOnClickListener(v -> openMusicPlayingActivity());
            trackName2.setOnClickListener(v -> openMusicPlayingActivity());
            trackAuthor2.setOnClickListener(v -> openMusicPlayingActivity());
        }

        // Добавьте обработчики для остальных треков (track3, track4 и т.д.)
    }

    // Метод для перехода на страницу music_playing
    private void openMusicPlayingActivity() {
        Intent intent = new Intent(tracks.this, music_playing.class);
        startActivity(intent);
    }
}