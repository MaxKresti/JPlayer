package com.example.jplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jplayer.R;

public class MiniPlayerFragment extends Fragment {

    private boolean isPlaying = false; // По умолчанию трек не играет
    private ImageButton playPauseButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Надуваем макет для мини-плеера
        View view = inflater.inflate(R.layout.mini_player, container, false);

        // Находим кнопку закрытия
        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> closeMiniPlayer());

        // Инициализация кнопки воспроизведения/паузы
        playPauseButton = view.findViewById(R.id.playPauseButton);

        // Установка начальной иконки (play)
        playPauseButton.setImageResource(R.drawable.pause2);

        // Обработка нажатия на кнопку
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        return view;
    }

    /**
     * Переключение между воспроизведением и паузой.
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
     * Воспроизведение трека.
     */
    private void playTrack() {
        isPlaying = true;
        playPauseButton.setImageResource(R.drawable.pause2); // Меняем иконку на паузу
        // Здесь добавьте логику для начала воспроизведения трека
    }

    /**
     * Пауза трека.
     */
    private void pauseTrack() {
        isPlaying = false;
        playPauseButton.setImageResource(R.drawable.play2); // Меняем иконку на воспроизведение
        // Здесь добавьте логику для паузы трека
    }

    /**
     * Закрытие мини-плеера.
     */
    private void closeMiniPlayer() {
        // Удаляем фрагмент из активити
        if (getParentFragmentManager() != null) {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}