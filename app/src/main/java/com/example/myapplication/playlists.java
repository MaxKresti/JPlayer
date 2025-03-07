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

public class playlists extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlists);

        // Настройка Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Обработчики для кнопок навигации
        findViewById(R.id.home).setOnClickListener(v -> {
            Intent intent = new Intent(playlists.this, main.class);
            startActivity(intent);
        });

        findViewById(R.id.media).setOnClickListener(v -> {
            Intent intent = new Intent(playlists.this, tracks.class);
            startActivity(intent);
        });

        findViewById(R.id.plus).setOnClickListener(v -> {
            Intent intent = new Intent(playlists.this, add_new.class);
            startActivity(intent);
        });

        // Обработчики для кнопок вкладок
        Button tracksButton = findViewById(R.id.tracksButton);
        Button albumsButton = findViewById(R.id.albumsButton);
        Button playlistsButton = findViewById(R.id.playlistsButton);

        tracksButton.setOnClickListener(v -> {
            Intent intent = new Intent(playlists.this, tracks.class);
            startActivity(intent);
        });

        albumsButton.setOnClickListener(v -> {
            Intent intent = new Intent(playlists.this, albums.class);
            startActivity(intent);
        });

        playlistsButton.setOnClickListener(v -> {
            // Уже находимся на странице playlists, поэтому ничего не делаем
        });
    }
}