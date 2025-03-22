package com.example.jplayer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;

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

    // UI элементы дополнительных контролов
    private ImageView backButton;
    private SeekBar seekBar;
    private ImageView playPauseButton;
    private ImageView likeButton;

    // Флаги состояния
    private boolean isPlaying = false; // Состояние воспроизведения
    private boolean isLiked = false;   // Состояние лайка

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Надуваем макет для большого плеера (убедитесь, что layout содержит все необходимые элементы)
        View view = inflater.inflate(R.layout.fragment_full_player, container, false);

        // Инициализируем PlayerView и связываем его с ExoPlayer из MainActivity
//        playerView = view.findViewById(R.id.playerView);
//        if (getActivity() instanceof MainActivity) {
//            exoPlayer = ((MainActivity) getActivity()).getExoPlayer();
//            playerView.setPlayer(exoPlayer);
//        }

        // Инициализация дополнительных элементов UI
        backButton = view.findViewById(R.id.back);
        seekBar = view.findViewById(R.id.seek);
        playPauseButton = view.findViewById(R.id.pause);
        likeButton = view.findViewById(R.id.like);

        // Обработка клика на кнопку "Назад"
        backButton.setOnClickListener(v -> closeFullPlayer());

        // Обработка клика на кнопку воспроизведения/паузы
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        // Обработка клика на кнопку "Лайк"
        likeButton.setOnClickListener(v -> toggleLike());

        // Настройка SeekBar (логика перемотки может быть доработана)
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && exoPlayer != null) {
                    // Например, перематываем на указанный процент от общей длительности
                    long duration = exoPlayer.getDuration();
                    long newPosition = (duration * progress) / seekBar.getMax();
                    exoPlayer.seekTo(newPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Можно приостанавливать обновление SeekBar во время перемотки
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Можно возобновить обновление SeekBar после перемотки
            }
        });

        // Обработка нажатия аппаратной кнопки "Назад" в самом фрагменте
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

    /**
     * Закрывает полноэкранный плеер и возвращает мини-плеер.
     */
    private void closeFullPlayer() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFullPlayer();
        }
    }

    /**
     * Переключает воспроизведение и паузу.
     */
    private void togglePlayPause() {
        if (exoPlayer == null) return;
        if (exoPlayer.isPlaying()) {
            pauseTrack();
        } else {
            playTrack();
        }
    }

    /**
     * Начинает воспроизведение трека.
     */
    private void playTrack() {
        isPlaying = true;
        // Анимация смены иконки при старте воспроизведения
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Меняем иконку на ту, что обозначает «воспроизведение» (или «пауза», если играем)
                playPauseButton.setImageResource(R.drawable.play3);
                playPauseButton.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        playPauseButton.startAnimation(scaleUp);

        // Запускаем плеер
        exoPlayer.play();
    }

    /**
     * Ставит трек на паузу.
     */
    private void pauseTrack() {
        isPlaying = false;
        // Анимация смены иконки при паузе
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Меняем иконку на ту, что обозначает «воспроизведение»
                playPauseButton.setImageResource(R.drawable.pause3);
                playPauseButton.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        playPauseButton.startAnimation(scaleUp);

        // Ставим плеер на паузу
        exoPlayer.pause();
    }

    /**
     * Переключает состояние "Лайк" с анимацией.
     */
    private void toggleLike() {
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isLiked) {
                    // Снимаем лайк
                    likeButton.setImageResource(R.drawable.heart);
                } else {
                    // Добавляем лайк
                    likeButton.setImageResource(R.drawable.heart_fill);
                }
                likeButton.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        likeButton.startAnimation(scaleUp);
        isLiked = !isLiked;
    }
}
