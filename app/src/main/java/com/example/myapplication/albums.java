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

public class albums extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_albums);

        // Настройка Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Обработчики для кнопок навигации
        findViewById(R.id.home).setOnClickListener(v -> {
            Intent intent = new Intent(albums.this, main.class);
            startActivity(intent);
        });

        findViewById(R.id.media).setOnClickListener(v -> {
            Intent intent = new Intent(albums.this, tracks.class);
            startActivity(intent);
        });

        findViewById(R.id.plus).setOnClickListener(v -> {
            Intent intent = new Intent(albums.this, add_new.class);
            startActivity(intent);
        });

        // Обработчики для кнопок вкладок
        Button tracksButton = findViewById(R.id.tracksButton);
        Button albumsButton = findViewById(R.id.albumsButton);
        Button playlistsButton = findViewById(R.id.playlistsButton);

        tracksButton.setOnClickListener(v -> {
            Intent intent = new Intent(albums.this, tracks.class);
            startActivity(intent);
        });

        albumsButton.setOnClickListener(v -> {
            // Уже находимся на странице albums, поэтому ничего не делаем
        });

        playlistsButton.setOnClickListener(v -> {
            Intent intent = new Intent(albums.this, playlists.class);
            startActivity(intent);
        });
    }
}