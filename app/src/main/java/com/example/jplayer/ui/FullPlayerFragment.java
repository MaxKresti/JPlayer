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

import com.example.jplayer.R;
import com.example.jplayer.MainActivity;

public class FullPlayerFragment extends Fragment {

    private ImageView backButton;
    private SeekBar seekBar;
    private ImageView playPauseButton;
    private ImageView likeButton;
    private boolean isPlaying = false; // Состояние воспроизведения
    private boolean isLiked = false; // Состояние лайка

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Надуваем макет для большого плеера
        View view = inflater.inflate(R.layout.fragment_full_player, container, false);

        // Инициализация элементов UI
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

        // Настройка SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Логика перемотки трека (если нужно)
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Логика при начале перемотки
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Логика при завершении перемотки
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Обработка кнопки "Назад"
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                closeFullPlayer();
                return true;
            }
            return false;
        });
    }

    /**
     * Закрывает большой плеер и возвращает мини-плеер.
     */
    private void closeFullPlayer() {

        MainActivity mainActivity = (MainActivity) requireActivity();


        backButton.animate()
                .rotation(270)
                .setDuration(200) // Длительность анимации
                .withEndAction(() -> {

                    mainActivity.hideFullPlayer();
                })
                .start();
    }

    /**
     * Переключает воспроизведение и паузу.
     */
    private void togglePlayPause() {
        if (isPlaying) {
            // Если трек играет, ставим на паузу
            pauseTrack();
        } else {
            // Если трек на паузе, воспроизводим
            playTrack();
        }
    }

    /**
     * Переключает состояние "Лайк".
     */
    private void toggleLike() {
        if (isLiked) {
            // Анимация для снятия лайка
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

            scaleUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    likeButton.setImageResource(R.drawable.heart); // Меняем иконку на "Лайк"
                    likeButton.startAnimation(scaleDown);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            likeButton.startAnimation(scaleUp);
        } else {
            // Анимация для добавления лайка
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

            scaleUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    likeButton.setImageResource(R.drawable.heart_fill); // Меняем иконку на "Лайк (заполненный)"
                    likeButton.startAnimation(scaleDown);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            likeButton.startAnimation(scaleUp);
        }
        isLiked = !isLiked; // Меняем состояние
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
}