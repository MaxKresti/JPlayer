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

public class albums extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.media); // Установите выбранный элемент

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    Intent homeIntent = new Intent(albums.this, main.class);
                    startActivity(homeIntent);

                    return true;
                } else if (itemId == R.id.media) {
                    // Уже на экране медиа
                    return true;
                } else if (itemId == R.id.plus) {
                    Intent plusIntent = new Intent(albums.this, add_new.class);
                    startActivity(plusIntent);

                    return true;
                }

                return false;
            }
        });


        Button tracksButton = findViewById(R.id.tracksButton);
        Button albumsButton = findViewById(R.id.albumsButton);
        Button playlistsButton = findViewById(R.id.playlistsButton);

        if (tracksButton != null) {
            tracksButton.setOnClickListener(v -> {
                Intent intent = new Intent(albums.this, tracks.class);
                startActivity(intent);
            });
        }

        if (albumsButton != null) {
            albumsButton.setOnClickListener(v -> {

            });
        }

        if (playlistsButton != null) {
            playlistsButton.setOnClickListener(v -> {
                Intent intent = new Intent(albums.this, playlists.class);
                startActivity(intent);
            });
        }


        setupAlbumsClickListeners();
    }


    private void setupAlbumsClickListeners() {

        View album1 = findViewById(R.id.track1);
        View album2 = findViewById(R.id.track2);
        View album3 = findViewById(R.id.track3);

        // Обработчики для элементов album1
        if (album1 != null) {
            View albumImage1 = album1.findViewById(R.id.albumImage);
            View albumName1 = album1.findViewById(R.id.albumName);
            View albumMenu1 = album1.findViewById(R.id.albumMenu);

            if (albumImage1 != null) {
                albumImage1.setOnClickListener(v -> openAlbumActivity());
            }

            if (albumName1 != null) {
                albumName1.setOnClickListener(v -> openAlbumActivity());
            }


            if (albumMenu1 != null) {
                albumMenu1.setOnClickListener(v -> showContextMenu(v));
            }
        }


        if (album2 != null) {
            View albumImage2 = album2.findViewById(R.id.albumImage);
            View albumName2 = album2.findViewById(R.id.albumName);
            View albumMenu2 = album2.findViewById(R.id.albumMenu);

            if (albumImage2 != null) {
                albumImage2.setOnClickListener(v -> openAlbumActivity());
            }

            if (albumName2 != null) {
                albumName2.setOnClickListener(v -> openAlbumActivity());
            }


            if (albumMenu2 != null) {
                albumMenu2.setOnClickListener(v -> showContextMenu(v));
            }
        }

        if (album3 != null) {
            View albumImage3 = album3.findViewById(R.id.albumImage);
            View albumName3 = album3.findViewById(R.id.albumName);
            View albumMenu3 = album3.findViewById(R.id.albumMenu);

            if (albumImage3 != null) {
                albumImage3.setOnClickListener(v -> openAlbumActivity());
            }

            if (albumName3 != null) {
                albumName3.setOnClickListener(v -> openAlbumActivity());
            }


            if (albumMenu3 != null) {
                albumMenu3.setOnClickListener(v -> showContextMenu(v));
            }
        }
    }


    private void showContextMenu(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.context_menu_album, null);

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


    private void openAlbumActivity() {
        Intent intent = new Intent(albums.this, album_opened.class);
        startActivity(intent);
    }
}