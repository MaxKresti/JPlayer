package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class add_new extends AppCompatActivity {



            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_add_new);

                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.plus); // Установите выбранный элемент

                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.home) {
                            Intent homeIntent = new Intent(add_new.this, main.class);
                            startActivity(homeIntent);
                            finish(); // закрываем текущую активность
                            return true;
                        } else if (itemId == R.id.media) {
                            Intent mediaIntent = new Intent(add_new.this, tracks.class);
                            startActivity(mediaIntent);
                            finish();
                            return true;
                        } else if (itemId == R.id.plus) {
                            // закрываем текущую активность
                            return true;
                        }

                        return false;
                    }
                });

    }
}