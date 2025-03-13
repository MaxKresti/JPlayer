package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class playlists extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.media); // Установите выбранный элемент

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    Intent homeIntent = new Intent(playlists.this, main.class);
                    startActivity(homeIntent);

                    return true;
                } else if (itemId == R.id.media) {
                    // Уже на экране медиа
                    return true;
                } else if (itemId == R.id.plus) {
                    Intent plusIntent = new Intent(playlists.this, add_new.class);
                    startActivity(plusIntent);

                    return true;
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
            Intent intent = new Intent(playlists.this, albums.class);
            startActivity(intent);
        });

        playlistsButton.setOnClickListener(v -> {
            Intent intent = new Intent(playlists.this, playlists.class);
            startActivity(intent);
        });

        setupPlaylistClickListeners();

    }


    private void setupPlaylistClickListeners() {
        View track1 = findViewById(R.id.track1);
        View track2 = findViewById(R.id.track2);
        View track3 = findViewById(R.id.track3);

        if (track1 != null) {
            View playlistImage1 = track1.findViewById(R.id.playlistImage);
            View playlistName1 = track1.findViewById(R.id.playlistName);
            View trackMenu1 = track1.findViewById(R.id.playlistMenu);

            if (playlistImage1 != null) {
                playlistImage1.setOnClickListener(v -> openPlaylistActivity());
            }

            if (playlistName1 != null) {
                playlistName1.setOnClickListener(v -> openPlaylistActivity());
            }

            if (trackMenu1 != null) {
                trackMenu1.setOnClickListener(v -> showContextMenu(v));
            }
        }

        if (track2 != null) {
            View playlistImage2 = track2.findViewById(R.id.playlistImage);
            View playlistName2 = track2.findViewById(R.id.playlistName);
            View trackMenu2 = track2.findViewById(R.id.playlistMenu);

            if (playlistImage2 != null) {
                playlistImage2.setOnClickListener(v -> openPlaylistActivity());
            }

            if (playlistName2 != null) {
                playlistName2.setOnClickListener(v -> openPlaylistActivity());
            }

            if (trackMenu2 != null) {
                trackMenu2.setOnClickListener(v -> showContextMenu(v));
            }
        }

        if (track3 != null) {
            View playlistImage3 = track3.findViewById(R.id.playlistImage);
            View playlistName3 = track3.findViewById(R.id.playlistName);
            View trackMenu3 = track3.findViewById(R.id.playlistMenu);

            if (playlistImage3 != null) {
                playlistImage3.setOnClickListener(v -> openPlaylistActivity());
            }

            if (playlistName3 != null) {
                playlistName3.setOnClickListener(v -> openPlaylistActivity());
            }

            if (trackMenu3 != null) {
                trackMenu3.setOnClickListener(v -> showContextMenu(v));
            }
        }
    }

    private void showContextMenu(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.context_menu_playlist, null);

        if (popupView == null) {
            Toast.makeText(this, "Error: popupView is null", Toast.LENGTH_SHORT).show();
            return;
        }

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

        LinearLayout rename = popupView.findViewById(R.id.rename);
        LinearLayout delete = popupView.findViewById(R.id.delete);

        if (rename != null) {
            rename.setOnClickListener(v -> {
                Toast.makeText(this, "Rename clicked", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });
        }

        if (delete != null) {
            delete.setOnClickListener(v -> {
                Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });
        }

        // Показываем контекстное меню снизу
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);

        // Закрытие меню при касании вне его области
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    private void openPlaylistActivity() {
        Intent intent = new Intent(playlists.this, playlist_opened.class);
        startActivity(intent);
    }
}