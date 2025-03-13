package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class playlist_opened extends AppCompatActivity {

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlist_opened);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.play_op), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.back).setOnClickListener(v -> {
            onBackPressed();
        });

        ImageView playButton = findViewById(R.id.play);

        playButton.setOnClickListener(v -> togglePlayPause(playButton));

        setupTrackClickListeners();
    }

    private void togglePlayPause(ImageView playButton) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(playButton, "alpha", 1f, 0f).setDuration(150);
        fadeOut.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {

            public void onAnimationEnd(Animator animation) {
                if (isPlaying) {
                    playButton.setImageResource(R.mipmap.play);
                } else {
                    playButton.setImageResource(R.mipmap.pause);
                }

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(playButton, "alpha", 0f, 1f).setDuration(150);
                fadeIn.start();
            }
        });

        isPlaying = !isPlaying; // Обновляем состояние
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

            trackImage1.setOnClickListener(v -> openMusicPlayingActivity());
            trackName1.setOnClickListener(v -> openMusicPlayingActivity());
            trackAuthor1.setOnClickListener(v -> openMusicPlayingActivity());

            trackMenu1.setOnClickListener(v -> showContextMenu(v));
        }

        if (track2 != null) {
            View trackImage2 = track2.findViewById(R.id.trackImage);
            View trackName2 = track2.findViewById(R.id.trackName);
            View trackAuthor2 = track2.findViewById(R.id.trackAuthor);
            ImageView trackMenu2 = track2.findViewById(R.id.trackMenu);

            trackImage2.setOnClickListener(v -> openMusicPlayingActivity());
            trackName2.setOnClickListener(v -> openMusicPlayingActivity());
            trackAuthor2.setOnClickListener(v -> openMusicPlayingActivity());

            trackMenu2.setOnClickListener(v -> showContextMenu(v));
        }

        if (track3 != null) {
            View trackImage3 = track3.findViewById(R.id.trackImage);
            View trackName3 = track3.findViewById(R.id.trackName);
            View trackAuthor3 = track3.findViewById(R.id.trackAuthor);
            ImageView trackMenu3 = track3.findViewById(R.id.trackMenu);

            trackImage3.setOnClickListener(v -> openMusicPlayingActivity());
            trackName3.setOnClickListener(v -> openMusicPlayingActivity());
            trackAuthor3.setOnClickListener(v -> openMusicPlayingActivity());

            trackMenu3.setOnClickListener(v -> showContextMenu(v));
        }
    }

    private void showContextMenu(View anchorView) {
        // Создаем PopupWindow
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

        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);
    }

    private void openMusicPlayingActivity() {
        Intent intent = new Intent(playlist_opened.this, music_playing.class);
        startActivity(intent);
    }
}