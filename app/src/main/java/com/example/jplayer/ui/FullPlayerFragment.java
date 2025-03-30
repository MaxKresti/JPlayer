package com.example.jplayer.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.util.Locale;

public class FullPlayerFragment extends Fragment {

    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    private ImageView backButton, playPauseButton, likeButton, albumImageView;
    private SeekBar seekBar;
    private TextView trackNameTextView, authorTextView;
    private TextView currentTimeTextView, durationTimeTextView;

    private boolean isPlaying = false;
    private boolean isLiked = false;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            if (exoPlayer != null && exoPlayer.getDuration() > 0) {
                long currentPosition = exoPlayer.getCurrentPosition();
                long duration = exoPlayer.getDuration();
                // Обновляем SeekBar, предполагая, что максимальное значение = 100
                int progress = (int) ((currentPosition * 100) / duration);
                seekBar.setProgress(progress);
                // Обновляем текстовые поля времени
                currentTimeTextView.setText(formatTime(currentPosition));
                durationTimeTextView.setText(formatTime(duration - currentPosition));
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trackNameTextView = view.findViewById(R.id.trackName);
        setupMarquee();
    }

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
        currentTimeTextView = view.findViewById(R.id.current);
        durationTimeTextView = view.findViewById(R.id.duration);

        // Получаем ExoPlayer из MainActivity
        if (getActivity() instanceof MainActivity) {
            exoPlayer = ((MainActivity) getActivity()).getExoPlayer();
        }

        // Извлекаем данные из Bundle и устанавливаем UI
        Bundle args = getArguments();
        if (args != null) {
            String trackName = args.getString("trackName", "Неизвестный трек");
            String author = args.getString("author", "Неизвестный артист");
            String coverArtPath = args.getString("coverArt", "");
            long trackPosition = args.getLong("trackPosition", 0);

            trackNameTextView.setText(trackName);
            authorTextView.setText(author);

            if (coverArtPath != null && !coverArtPath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(coverArtPath);
                if (bitmap != null) {
                    albumImageView.setImageBitmap(bitmap);
                } else {
                    albumImageView.setImageResource(R.drawable.image);
                }
            } else {
                albumImageView.setImageResource(R.drawable.image);
            }

            if (exoPlayer != null) {
                long currentPos = exoPlayer.getCurrentPosition();
                // Если разница больше 500 мс, тогда перематываем, иначе оставляем как есть
                if (Math.abs(currentPos - trackPosition) > 500) {
                    exoPlayer.seekTo(trackPosition);
                }
            }
        }

        // Устанавливаем слушатель для SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private boolean userTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Если изменение вызвано пользователем, рассчитываем новую позицию
                if (fromUser && exoPlayer != null && exoPlayer.getDuration() > 0) {
                    long duration = exoPlayer.getDuration();
                    long newPosition = (duration * progress) / 100;
                    currentTimeTextView.setText(formatTime(newPosition));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTouch = true;
                handler.removeCallbacks(updateProgressAction);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (exoPlayer != null && exoPlayer.getDuration() > 0) {
                    long duration = exoPlayer.getDuration();
                    long newPosition = (duration * seekBar.getProgress()) / 100;
                    exoPlayer.seekTo(newPosition);
                }
                userTouch = false;
                handler.post(updateProgressAction);
            }
        });

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

    private String formatTime(long millis) {
        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
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
        animateButton(playPauseButton, R.drawable.pause3);
        exoPlayer.play();
    }

    private void pauseTrack() {
        isPlaying = false;
        animateButton(playPauseButton, R.drawable.play3);
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

    @Override
    public void onStart() {
        super.onStart();
        // Запускаем обновление прогресса плеера
        handler.post(updateProgressAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Останавливаем обновление прогресса, чтобы не было утечек памяти
        handler.removeCallbacks(updateProgressAction);
    }

    private void setupMarquee() {

        trackNameTextView.setSelected(true);

        trackNameTextView.post(() -> {
            boolean isTextTooLong = trackNameTextView.getLayout() != null
                    && trackNameTextView.getLayout().getLineWidth(0) > trackNameTextView.getWidth();
            trackNameTextView.setSelected(isTextTooLong);
        });
    }
}
