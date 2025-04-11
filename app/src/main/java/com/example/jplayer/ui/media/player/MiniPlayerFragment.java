package com.example.jplayer.ui.media.player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.exoplayer.ExoPlayer;
import com.example.jplayer.R;
import com.example.jplayer.MainActivity;

public class MiniPlayerFragment extends Fragment {

    private ImageView trackCover;
    private TextView trackTitle;
    private TextView trackArtist;
    private ImageButton playPauseButton;
    private ImageButton prevButton, nextButton;
    private boolean isPlaying = false;
    private ExoPlayer exoPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_player, container, false);

        // Инициализация UI элементов
        trackCover = view.findViewById(R.id.trackCover);
        trackTitle = view.findViewById(R.id.trackTitle);
        trackArtist = view.findViewById(R.id.trackArtist);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        prevButton = view.findViewById(R.id.prevButton);
        nextButton = view.findViewById(R.id.nextButton);

        // Если переданы данные через аргументы, устанавливаем их
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String artist = args.getString("artist");
            String coverArt = args.getString("coverArt");
            if (title != null) trackTitle.setText(title);
            if (artist != null) trackArtist.setText(artist);
            if (coverArt != null && !coverArt.isEmpty()) {
                // Попробуем загрузить изображение через BitmapFactory, если путь абсолютный
                Bitmap bitmap = BitmapFactory.decodeFile(coverArt);
                if (bitmap != null) {
                    trackCover.setImageBitmap(bitmap);
                } else {
                    trackCover.setImageResource(R.drawable.image);
                }
            } else {
                trackCover.setImageResource(R.drawable.image);
            }
        }

        // При клике по обложке открываем полноэкранный плеер
        trackCover.setOnClickListener(v -> openFullPlayer());

        // Получаем ExoPlayer из MainActivity
        if (getActivity() instanceof MainActivity) {
            exoPlayer = ((MainActivity) getActivity()).getExoPlayer();
        }

        // Обработка клика по кнопке play/pause
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        // Обработка кликов по кнопкам переключения треков
        prevButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).playPreviousTrack();
            }
        });
        nextButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).playNextTrack();
            }
        });

        return view;
    }

    /**
     * Переключает воспроизведение/поставку на паузу с анимацией.
     */
    private void togglePlayPause() {
        if (exoPlayer == null) return;
        if (exoPlayer.isPlaying()) {
            pauseWithAnimation();
            exoPlayer.pause();
            isPlaying = false;
        } else {
            playWithAnimation();
            exoPlayer.play();
            isPlaying = true;
        }
    }

    /**
     * Анимация при воспроизведении.
     */
    private void playWithAnimation() {
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                playPauseButton.setImageResource(R.drawable.pause2);
                playPauseButton.startAnimation(scaleDown);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        playPauseButton.startAnimation(scaleUp);
    }

    /**
     * Анимация при постановке на паузу.
     */
    private void pauseWithAnimation() {
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                playPauseButton.setImageResource(R.drawable.play2);
                playPauseButton.startAnimation(scaleDown);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        playPauseButton.startAnimation(scaleUp);
    }

    /**
     * Открывает полноэкранный плеер через MainActivity.
     */
    private void openFullPlayer() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFullPlayer();
        }
    }
}
