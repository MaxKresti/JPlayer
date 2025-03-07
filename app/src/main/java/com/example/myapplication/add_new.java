package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class add_new extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new);

        // Настройка Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Обработчики для кнопок навигации
        ImageView homeButton = findViewById(R.id.home);
        ImageView mediaButton = findViewById(R.id.media);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(add_new.this, main.class);
            startActivity(intent);
        });

        mediaButton.setOnClickListener(v -> {
            Intent intent = new Intent(add_new.this, tracks.class);
            startActivity(intent);
        });
    }
}