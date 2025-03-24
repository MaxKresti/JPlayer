package com.example.jplayer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.ActivityMainBinding;
import com.example.jplayer.ui.FullPlayerFragment;
import com.example.jplayer.ui.MiniPlayerFragment;
import com.example.jplayer.ui.PlaylistAlbumFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ExoPlayer exoPlayer;
    private Song currentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация viewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Создаем экземпляр ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();

        // Настройка BottomNavigationView и Navigation Component
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_media,
                R.id.navigation_add_new
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Обработчик ошибок ExoPlayer
        exoPlayer.addListener(new ExoPlayer.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e("ExoPlayerError", "Ошибка воспроизведения: " + error.getMessage());
            }
        });
    }

    /**
     * Метод для воспроизведения трека.
     * Сохраняет текущий трек, настраивает ExoPlayer и отображает мини-плеер.
     */
    public void playTrack(Song song) {
        currentSong = song;
        if (exoPlayer.isPlaying()) {
            exoPlayer.stop();
        }
        exoPlayer.clearMediaItems();

        // Проверка существования файла (для отладки)
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

        // Показываем мини-плеер с данными о треке
        showMiniPlayer(song);
    }

    /**
     * Предоставляет доступ к ExoPlayer для других фрагментов.
     */
    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    /**
     * Показывает мини-плеер с информацией о треке.
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

    /**
     * Перегруженный метод для показа мини-плеера без передачи данных.
     */
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
     * Показывает FullPlayerFragment с анимацией и передает данные о текущем треке.
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

        // Передаем информацию о текущем треке
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
     * Скрывает FullPlayerFragment, восстанавливает мини-плеер и показывает нижнюю навигацию.
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

        // Анимация для закрытия контейнера большого плеера
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
     * Метод для удаления трека.
     * Удаляет песню из базы данных и останавливает воспроизведение, если она играет.
     */
    public void deleteTrack(Song song) {
        // Удаление из базы данных
        AppDatabase.getInstance(this).songDao().delete(song);
        Log.d("MainActivity", "Трек удален из базы: " + song.title);

        // Если удаляемая песня воспроизводится, остановить плеер
        if (currentSong != null && currentSong.id == song.id) {
            exoPlayer.stop();
            currentSong = null;
        }

        // Можно добавить дополнительное обновление UI, например, обновление списка треков
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

    public void stopIfPlaying(Song song) {
        if (currentSong != null && currentSong.id == song.id) {
            exoPlayer.stop();
            // Можно также вызвать exoPlayer.clearMediaItems(), если нужно очистить список медиа.
            currentSong = null;
        }
    }

    public void showRenameDialog(final Song song, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Переименовать трек");

        final EditText input = new EditText(this);
        input.setText(song.title);
        builder.setView(input);

        builder.setPositiveButton("ОК", (dialog, which) -> {
            String newTitle = input.getText().toString().trim();
            if (!newTitle.isEmpty() && !newTitle.equals(song.title)) {
                song.title = newTitle;
                AppDatabase.getInstance(this).songDao().update(song);
                Toast.makeText(this, "Трек переименован", Toast.LENGTH_SHORT).show();
                // Если адаптер использует LiveData, обновление произойдёт автоматически.
                // Или можно вызвать notifyItemChanged(position) в адаптере.
            }
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
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
