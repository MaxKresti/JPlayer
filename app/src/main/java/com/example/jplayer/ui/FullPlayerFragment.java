package com.example.jplayer.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.jplayer.R;
import com.example.jplayer.MainActivity;

public class FullPlayerFragment extends Fragment {

    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    private ImageView backButton, playPauseButton, likeButton, albumImageView;
    private SeekBar seekBar;
    private TextView trackNameTextView, authorTextView;

    private boolean isPlaying = false;
    private boolean isLiked = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_player, container, false);

        // Инициализация UI элементов
        trackNameTextView = view.findViewById(R.id.trackName);
        authorTextView = view.findViewById(R.id.author);
        albumImageView = view.findViewById(R.id.album_image);
        backButton = view.findViewById(R.id.back);
        seekBar = view.findViewById(R.id.seek);
        playPauseButton = view.findViewById(R.id.pause);
        likeButton = view.findViewById(R.id.like);

        if (getArguments() != null) {
            String trackName = getArguments().getString("trackName", "Неизвестный трек");
            String artist = getArguments().getString("author", "Неизвестный артист");
            String coverArt = getArguments().getString("coverArt", null);

//            titleTextView.setText(trackName);
//            artistTextView.setText(artist);
//
//            if (coverArt != null) {
//                Uri imageUri = Uri.parse(coverArt);
//                coverImageView.setImageURI(imageUri);
//            } else {
//                coverImageView.setImageResource(R.drawable.default_cover);
//            }
        }

        // Получаем ExoPlayer из MainActivity
        if (getActivity() instanceof MainActivity) {
            exoPlayer = ((MainActivity) getActivity()).getExoPlayer();
        }

        // Получаем данные из Bundle
        Bundle args = getArguments();
        if (args != null) {
            String trackName = args.getString("trackName");
            String author = args.getString("author");
            int albumImageRes = args.getInt("albumImageRes");
            long trackPosition = args.getLong("trackPosition");

            trackNameTextView.setText(trackName);
            authorTextView.setText(author);
            albumImageView.setImageResource(albumImageRes);

            if (exoPlayer != null) {
                exoPlayer.seekTo(trackPosition);
            }
        }

        // Обработчики кликов
        backButton.setOnClickListener(v -> closeFullPlayer());
        playPauseButton.setOnClickListener(v -> togglePlayPause());
        likeButton.setOnClickListener(v -> toggleLike());

        // Обработка нажатия аппаратной кнопки "Назад"
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                closeFullPlayer();
                return true;
            }
            return false;
        });

        return view;
    }

    private void closeFullPlayer() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFullPlayer();
        }
    }

    private void togglePlayPause() {
        if (exoPlayer == null) return;
        if (exoPlayer.isPlaying()) {
            pauseTrack();
        } else {
            playTrack();
        }
    }

    private void playTrack() {
        isPlaying = true;
        animateButton(playPauseButton, R.drawable.play3);
        exoPlayer.play();
    }

    private void pauseTrack() {
        isPlaying = false;
        animateButton(playPauseButton, R.drawable.pause3);
        exoPlayer.pause();
    }

    private void toggleLike() {
        isLiked = !isLiked;
        animateButton(likeButton, isLiked ? R.drawable.heart_fill : R.drawable.heart);
    }

    private void animateButton(ImageView button, int newIcon) {
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                button.setImageResource(newIcon);
                button.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        button.startAnimation(scaleUp);
    }
}
