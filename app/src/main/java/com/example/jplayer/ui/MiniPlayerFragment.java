package com.example.jplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.jplayer.R;
import com.example.jplayer.MainActivity;

public class MiniPlayerFragment extends Fragment {

    private boolean isPlaying = false; // По умолчанию трек не играет
    private ImageButton playPauseButton;
    private ExoPlayer exoPlayer;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_player, container, false);

        // Находим фото трека
        ImageView trackCover = view.findViewById(R.id.trackCover);

        // Обработка клика на фото
        if (trackCover != null) {
            trackCover.setOnClickListener(v -> openFullPlayer());
        }

        // Получаем ExoPlayer из MainActivity
        if (getActivity() instanceof MainActivity) {
            exoPlayer = ((MainActivity) getActivity()).getExoPlayer();
        }

        // Инициализация кнопки воспроизведения/паузы
        playPauseButton = view.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        return view;
    }

    /**
     * Переключение между воспроизведением и паузой.
     */
    private void togglePlayPause() {
        if (exoPlayer == null) return;
        if (exoPlayer.isPlaying()) {
            exoPlayer.pause();
            isPlaying = false;
            updatePlayPauseIcon();
        } else {
            exoPlayer.play();
            isPlaying = true;
            updatePlayPauseIcon();
        }
    }

    private void updatePlayPauseIcon() {
        if (isPlaying) {
            playPauseButton.setImageResource(R.drawable.pause2);
        } else {
            playPauseButton.setImageResource(R.drawable.play2);
        }
    }

    private void playTrack() {
        isPlaying = true;
        // Анимация смены иконки
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playPauseButton.setImageResource(R.drawable.play2); // Меняем иконку на паузу
                playPauseButton.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        playPauseButton.startAnimation(scaleUp);
        // Здесь добавьте логику для начала воспроизведения трека
    }

    private void pauseTrack() {
        isPlaying = false;
        // Анимация смены иконки
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playPauseButton.setImageResource(R.drawable.pause2); // Меняем иконку на воспроизведение
                playPauseButton.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        playPauseButton.startAnimation(scaleUp);
        // Здесь добавьте логику для паузы трека
    }


    private void openFullPlayer() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFullPlayer();
        }
    }
}