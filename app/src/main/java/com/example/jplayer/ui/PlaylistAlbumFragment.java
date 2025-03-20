package com.example.jplayer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.databinding.FragmentPlaylistAlbumBinding;
import com.example.jplayer.ui.media.track.TrackMenuSheetDialogFragment;

public class PlaylistAlbumFragment extends Fragment {

    private ImageView backButton;
    private ImageView playPauseButton;
    private ImageView remixButton;
    private boolean isPlaying = false;

    private FragmentPlaylistAlbumBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistAlbumBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Инициализация элементов UI через binding
        backButton = binding.back;
        playPauseButton = binding.play;

        // Обработка клика на кнопку "Назад"
        backButton.setOnClickListener(v -> closePlaylistAlbum());

        // Обработка клика на кнопку воспроизведения/паузы
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Находим контейнер с треками
        LinearLayout tracksContainer = binding.tracksContainer;

        // Перебираем все элементы треков
        for (int i = 0; i < tracksContainer.getChildCount(); i++) {
            View trackItem = tracksContainer.getChildAt(i);
            ImageView trackImage = trackItem.findViewById(R.id.trackImage);
            ImageView trackMenu = trackItem.findViewById(R.id.trackMenu);

            // Создаем финальную копию переменной i
            final int position = i;

            // Обработка клика на кнопку с тремя точками
            if (trackMenu != null) {
                trackMenu.setOnClickListener(v -> showContextMenu(v, position));
            }

            // Обработка клика на изображение трека
            if (trackImage != null) {
                trackImage.setOnClickListener(v -> {
                    // Логика при нажатии на трек
                });
            }
        }

        // Обработка кнопки "Назад" на устройстве
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                closePlaylistAlbum();
                return true;
            }
            return false;
        });
    }

    /**
     * Закрывает PlaylistAlbumFragment.
     */
    private void closePlaylistAlbum() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hidePlaylistAlbum(); // Закрываем PlaylistAlbumFragment
        }
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
                playPauseButton.setImageResource(R.drawable.play); // Меняем иконку на паузу
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
                playPauseButton.setImageResource(R.drawable.pause); // Меняем иконку на воспроизведение
                playPauseButton.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        playPauseButton.startAnimation(scaleUp);
        // Здесь добавьте логику для паузы трека
    }

    private void showContextMenu(View anchorView, int position) {
        TrackMenuSheetDialogFragment bottomSheet = new TrackMenuSheetDialogFragment();
        bottomSheet.setPosition(position);
        bottomSheet.setOnMenuItemClickListener(new TrackMenuSheetDialogFragment.OnMenuItemClickListener() {
            @Override
            public void onAddToPlaylist(int position) {
                // Обработка добавления в плейлист
            }

            @Override
            public void onRename(int position) {
                // Обработка переименования
            }

            @Override
            public void onDelete(int position) {
                // Обработка удаления
            }
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }
}