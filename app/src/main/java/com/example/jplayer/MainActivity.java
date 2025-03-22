package com.example.jplayer;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ExoPlayer exoPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        exoPlayer = new ExoPlayer.Builder(this).build();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_media,
                R.id.navigation_add_new
        ).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * Метод для воспроизведения трека по указанному пути.
     * Вызывается, например, из адаптера при нажатии на элемент.
     */
    public void playTrack(Song song) {
        // Пример: установка медиа айтема и запуск плеера
        Uri uri = Uri.parse(song.filePath);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        // Показываем мини-плеер и передаем данные о треке через Bundle
        showMiniPlayer(song);
    }

    /**
     * Предоставляем доступ к ExoPlayer для других фрагментов.
     */
    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    /**
     * Показывает мини-плеер.
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
        // Скрываем мини-плеер
        hideMiniPlayer();

        // Создаем экземпляр FullPlayerFragment
        FullPlayerFragment fullPlayerFragment = new FullPlayerFragment();

        // Делаем контейнер для большого плеера видимым
        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        if (fullPlayerContainer != null) {
            fullPlayerContainer.setVisibility(View.VISIBLE);
            fullPlayerContainer.setTranslationY(fullPlayerContainer.getHeight());
            fullPlayerContainer.setAlpha(0.0f);

            // Анимация для появления плеера
            fullPlayerContainer.animate()
                    .translationY(0) // Перемещаем вверх
                    .alpha(1.0f) // Увеличиваем прозрачность
                    .setDuration(300) // Длительность анимации
                    .start();
        }


        setBottomNavigationVisibility(false);

        // Заменяем фрагмент
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fullPlayerContainer, fullPlayerFragment)
                .addToBackStack(null) // Добавляем в back stack для возможности возврата
                .commit();
    }

    /**
     * Скрывает FullPlayerFragment.
     */
    public void hideFullPlayer() {
        // Удаляем FullPlayerFragment
        Fragment fullPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.fullPlayerContainer);
        if (fullPlayerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fullPlayerFragment)
                    .commit();
        }

        // Анимация для закрытия плеера
        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        if (fullPlayerContainer != null) {
            fullPlayerContainer.animate()
                    .translationY(fullPlayerContainer.getHeight()) // Перемещаем вниз
                    .alpha(0.0f) // Уменьшаем прозрачность
                    .setDuration(300) // Длительность анимации
                    .withEndAction(() -> {
                        fullPlayerContainer.setVisibility(View.GONE); // Скрываем контейнер
                    })
                    .start();
        }

        // Показываем BottomNavigationView
        setBottomNavigationVisibility(true);

        // Показываем мини-плеер
        showMiniPlayer();
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

            // Анимация для появления фрагмента
            playlistAlbumContainer.animate()
                    .translationY(0) // Перемещаем вверх
                    .alpha(1.0f) // Увеличиваем прозрачность
                    .setDuration(300) // Длительность анимации
                    .start();
        }

        // Скрываем BottomNavigationView
        setBottomNavigationVisibility(false);

        // Заменяем фрагмент
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.playlistAlbumContainer, playlistAlbumFragment)
                .addToBackStack(null) // Добавляем в back stack для возможности возврата
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

        // Анимация для закрытия фрагмента
        View playlistAlbumContainer = findViewById(R.id.playlistAlbumContainer);
        if (playlistAlbumContainer != null) {
            playlistAlbumContainer.animate()
                    .translationY(playlistAlbumContainer.getHeight()) // Перемещаем вниз
                    .alpha(0.0f) // Уменьшаем прозрачность
                    .setDuration(300) // Длительность анимации
                    .withEndAction(() -> {
                        playlistAlbumContainer.setVisibility(View.GONE); // Скрываем контейнер
                    })
                    .start();
        }

        // Показываем BottomNavigationView
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

}