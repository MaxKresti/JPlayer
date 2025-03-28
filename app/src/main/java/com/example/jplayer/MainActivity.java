package com.example.jplayer;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.ActivityMainBinding;
import com.example.jplayer.ui.MiniPlayerFragment;
import com.example.jplayer.ui.FullPlayerFragment;
import com.example.jplayer.ui.PlaylistAlbumFragment;
import com.example.jplayer.ui.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ExoPlayer exoPlayer;
    private Song currentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        exoPlayer = new ExoPlayer.Builder(this).build();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Настройка цветов BottomNavigationView
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },   // активный элемент
                new int[] { -android.R.attr.state_checked }   // неактивный элемент
        };

        int[] colors = new int[] {
                ContextCompat.getColor(this, R.color.purple_500), // фиолетовый для активного
                Color.WHITE                                      // белый для неактивного
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        navView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.nav_icon_color));
        navView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.nav_icon_color));

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_media,
                R.id.navigation_add_new
        ).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e("ExoPlayerError", "Ошибка воспроизведения: " + error.getMessage());
            }
        });
    }

    /**
     * Метод для воспроизведения трека.
     */
    public void playTrack(Song song) {
        currentSong = song;
        if (exoPlayer.isPlaying()) {
            exoPlayer.stop();
        }
        exoPlayer.clearMediaItems();

        File file = new File(song.filePath);
        if (!file.exists()) {
            Log.e("playTrack", "Файл не существует: " + song.filePath);
        }
        Uri uri = file.exists() ? Uri.fromFile(file) : Uri.parse(song.filePath);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.seekTo(0);
        exoPlayer.play();

        showMiniPlayer(song);
    }

    /**
     * Предоставляет доступ к ExoPlayer для других фрагментов.
     */
    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    /**
     * Показывает мини-плеер с данными о треке.
     */
    public void showMiniPlayer(Song song) {
        Bundle bundle = new Bundle();
        bundle.putString("title", song.title);
        bundle.putString("artist", song.artist);
        bundle.putString("coverArt", song.coverArt);

        MiniPlayerFragment miniPlayerFragment = new MiniPlayerFragment();
        miniPlayerFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.miniPlayerContainer, miniPlayerFragment)
                .commit();
    }

    public void showMiniPlayer() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.miniPlayerContainer, new MiniPlayerFragment())
                .commit();
    }

    /**
     * Скрывает мини-плеер.
     */
    public void hideMiniPlayer() {
        Fragment miniPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.miniPlayerContainer);
        if (miniPlayerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(miniPlayerFragment)
                    .commit();
        }
    }

    /**
     * Показывает FullPlayerFragment.
     */
    public void showFullPlayer() {
        // Проверяем, что currentSong не равен null
        if (currentSong == null) {
            Log.e("MainActivity", "currentSong is null. Невозможно открыть полноэкранный плеер.");
            return;
        }

        // Скрываем мини-плеер
        hideMiniPlayer();

        // Создаем экземпляр FullPlayerFragment
        FullPlayerFragment fullPlayerFragment = new FullPlayerFragment();

        // Передаём информацию о текущем треке
        Bundle args = new Bundle();
        args.putString("trackName", currentSong.title);
        args.putString("author", currentSong.artist);
        args.putString("coverArt", currentSong.coverArt);
        args.putLong("trackPosition", exoPlayer.getCurrentPosition());
        fullPlayerFragment.setArguments(args);

        // Анимируем контейнер для большого плеера, если он существует
        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        if (fullPlayerContainer != null) {
            fullPlayerContainer.setVisibility(View.VISIBLE);
            fullPlayerContainer.setTranslationY(fullPlayerContainer.getHeight());
            fullPlayerContainer.setAlpha(0.0f);
            fullPlayerContainer.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(300)
                    .start();
        } else {
            Log.e("MainActivity", "fullPlayerContainer не найден");
        }

        // Скрываем нижнюю навигацию
        setBottomNavigationVisibility(false);

        // Заменяем фрагмент в контейнере навигации (например, nav_host_fragment_activity_main)
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.nav_host_fragment_activity_main, fullPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Скрывает FullPlayerFragment и восстанавливает мини-плеер с данными о текущем треке.
     */
    public void hideFullPlayer() {
        getSupportFragmentManager().popBackStack();

        // Попытка удалить FullPlayerFragment из контейнера fullPlayerContainer
        Fragment fullPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.fullPlayerContainer);
        if (fullPlayerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fullPlayerFragment)
                    .commit();
        }

        // Анимация закрытия контейнера большого плеера
        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        if (fullPlayerContainer != null) {
            fullPlayerContainer.animate()
                    .translationY(fullPlayerContainer.getHeight())
                    .alpha(0.0f)
                    .setDuration(300)
                    .withEndAction(() -> fullPlayerContainer.setVisibility(View.GONE))
                    .start();
        }

        // Показываем нижнюю навигацию
        setBottomNavigationVisibility(true);

        // Восстанавливаем мини-плеер с данными текущей песни (если есть)
        if (currentSong != null) {
            showMiniPlayer(currentSong);
        } else {
            showMiniPlayer();
        }
    }

    /**
     * Показывает PlaylistAlbumFragment.
     */
    public void showPlaylistAlbum() {
        PlaylistAlbumFragment playlistAlbumFragment = new PlaylistAlbumFragment();
        View playlistAlbumContainer = findViewById(R.id.playlistAlbumContainer);
        if (playlistAlbumContainer != null) {
            playlistAlbumContainer.setVisibility(View.VISIBLE);
            playlistAlbumContainer.setTranslationY(playlistAlbumContainer.getHeight());
            playlistAlbumContainer.setAlpha(0.0f);
            playlistAlbumContainer.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(300)
                    .start();
        }
        setBottomNavigationVisibility(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.playlistAlbumContainer, playlistAlbumFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Скрывает PlaylistAlbumFragment.
     */
    public void hidePlaylistAlbum() {
        Fragment playlistAlbumFragment = getSupportFragmentManager().findFragmentById(R.id.playlistAlbumContainer);
        if (playlistAlbumFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(playlistAlbumFragment)
                    .commit();
        }
        View playlistAlbumContainer = findViewById(R.id.playlistAlbumContainer);
        if (playlistAlbumContainer != null) {
            playlistAlbumContainer.animate()
                    .translationY(playlistAlbumContainer.getHeight())
                    .alpha(0.0f)
                    .setDuration(300)
                    .withEndAction(() -> playlistAlbumContainer.setVisibility(View.GONE))
                    .start();
        }
        setBottomNavigationVisibility(true);
    }

    /**
     * Управляет видимостью BottomNavigationView.
     *
     * @param isVisible true - показать, false - скрыть
     */
    public void setBottomNavigationVisibility(boolean isVisible) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (navView != null) {
            navView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void showSetting() {

        SettingFragment SettingFragment = new SettingFragment();


        View SettingContainer = findViewById(R.id.SettingContainer);
        if (SettingContainer != null) {
            SettingContainer.setVisibility(View.VISIBLE);
            SettingContainer.setTranslationY(SettingContainer.getHeight());
            SettingContainer.setAlpha(0.0f);

            // Анимация для появления фрагмента
            SettingContainer.animate()
                    .translationY(0) // Перемещаем вверх
                    .alpha(1.0f) // Увеличиваем прозрачность
                    .setDuration(300) // Длительность анимации
                    .start();
        }

        // Скрываем BottomNavigationView
        setBottomNavigationVisibility(false);

        // Заменяем фрагмент
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.SettingContainer, SettingFragment)
                .addToBackStack(null) // Добавляем в back stack для возможности возврата
                .commit();
    }


    public void hideSetting() {

        Fragment SettingFragment = getSupportFragmentManager().findFragmentById(R.id.SettingContainer);
        if (SettingFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(SettingFragment)
                    .commit();
        }

        // Анимация для закрытия фрагмента
        View SettingContainer = findViewById(R.id.SettingContainer);
        if (SettingContainer != null) {
            SettingContainer.animate()
                    .translationY(SettingContainer.getHeight()) // Перемещаем вниз
                    .alpha(0.0f) // Уменьшаем прозрачность
                    .setDuration(300) // Длительность анимации
                    .withEndAction(() -> {
                        SettingContainer.setVisibility(View.GONE); // Скрываем контейнер
                    })
                    .start();
        }

        // Показываем BottomNavigationView
        setBottomNavigationVisibility(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
