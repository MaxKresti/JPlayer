package com.example.jplayer.ui;

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

    private ImageButton playPauseButton;
    private ImageView trackCover;
    private TextView trackTitle;
    private TextView trackArtist;
    private boolean isPlaying = false;
    private ExoPlayer exoPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_player, container, false);
        View miniPlayer = view.findViewById(R.id.mini_player); // Контейнер миниплеера

        miniPlayer.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showFullPlayer();
            }
        });


        // Инициализация UI элементов
        trackCover = view.findViewById(R.id.trackCover);
        trackTitle = view.findViewById(R.id.trackTitle);
        trackArtist = view.findViewById(R.id.trackArtist);
        playPauseButton = view.findViewById(R.id.playPauseButton);

        // Если переданы данные о треке через аргументы, устанавливаем их
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String artist = args.getString("artist");
            String coverArt = args.getString("coverArt");

            trackTitle.setText(title);
            trackArtist.setText(artist);
            if (coverArt != null && !coverArt.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(coverArt);
                if (bitmap != null) {
                    trackCover.setImageBitmap(bitmap);
                }
            }
        }

        // Обработка клика по обложке: открываем полноэкранный плеер
        trackCover.setOnClickListener(v -> openFullPlayer());

        // Получаем ExoPlayer из MainActivity
        if (getActivity() instanceof MainActivity) {
            exoPlayer = ((MainActivity) getActivity()).getExoPlayer();
        }

        // Обработка клика по кнопке play/pause
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        setupMarqueeEffect();

        return view;
    }

    /**
     * Переключает воспроизведение и паузу с анимацией.
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
     * Выполняет анимацию при начале воспроизведения.
     */
    private void playWithAnimation() {
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // При воспроизведении устанавливаем иконку, обозначающую состояние "пауза"
                playPauseButton.setImageResource(R.drawable.pause2);
                playPauseButton.startAnimation(scaleDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        playPauseButton.startAnimation(scaleUp);
    }

    /**
     * Выполняет анимацию при постановке на паузу.
     */
    private void pauseWithAnimation() {
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // При паузе устанавливаем иконку, обозначающую состояние "воспроизведение"
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
            Bundle bundle = new Bundle();
            bundle.putString("trackName", trackTitle.getText().toString());
            bundle.putString("author", trackArtist.getText().toString());
            bundle.putLong("trackPosition", exoPlayer != null ? exoPlayer.getCurrentPosition() : 0);

            FullPlayerFragment fullPlayerFragment = new FullPlayerFragment();
            fullPlayerFragment.setArguments(bundle);

            ((MainActivity) getActivity()).showFullPlayer();
        }
    }

    private void setupMarqueeEffect() {
        // Установка параметров для бегущей строки
        trackTitle.setSelected(true);
        trackArtist.setSelected(true);

        // Автоматическая активация при длинном тексте
        trackTitle.post(() -> {
            if (trackTitle.getLineCount() > 1 || trackTitle.getPaint().measureText(trackTitle.getText().toString()) > trackTitle.getWidth()) {
                trackTitle.setSelected(true);
            }
        });

        trackArtist.post(() -> {
            if (trackArtist.getLineCount() > 1 || trackArtist.getPaint().measureText(trackArtist.getText().toString()) > trackArtist.getWidth()) {
                trackArtist.setSelected(true);
            }
        });
    }

}
