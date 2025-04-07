package com.example.jplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;
import com.example.jplayer.database.playlistSong.PlaylistSong;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.ActivityMainBinding;
import com.example.jplayer.ui.FullPlayerFragment;
import com.example.jplayer.ui.MiniPlayerFragment;
import com.example.jplayer.ui.PlayerViewModel;
import com.example.jplayer.ui.PlaylistAlbumFragment;

import com.example.jplayer.ui.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ExoPlayer exoPlayer;
    private PlayerViewModel playerViewModel;
    private int currentUserId;
    private Song currentSong;
    private List<Song> currentPlaylist = new ArrayList<>();
    private int currentTrackIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Получаем ID текущего пользователя из SharedPreferences
        currentUserId = getCurrentUserId();
        if (currentUserId == -1) {
            Toast.makeText(this, "Ошибка: не удалось получить ID пользователя", Toast.LENGTH_SHORT).show();
            // Здесь можно перенаправить пользователя на экран авторизации
        }

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        ensureLovedTracksPlaylist(); // Создаем плейлист "Loved Tracks", если его нет

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

        exoPlayer.addListener(new ExoPlayer.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e("ExoPlayerError", "Ошибка воспроизведения: " + error.getMessage());
            }
        });
    }

    // Метод для получения текущего user_id из SharedPreferences
    private int getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getInt("user_id", -1);
    }

    // Метод для создания плейлиста "Loved Tracks", если его ещё нет
    private void ensureLovedTracksPlaylist() {
        if (currentUserId == -1) {
            Log.e("MainActivity", "Не удалось получить ID пользователя");
            return;
        }
        new Thread(() -> {
            Playlist lovedTracks = AppDatabase.getInstance(getApplicationContext())
                    .playlistDao().getPlaylistByUserIdAndName(currentUserId, "Loved Tracks");
            if (lovedTracks == null) {
                Playlist newPlaylist = new Playlist(currentUserId, "Loved Tracks", "");
                AppDatabase.getInstance(getApplicationContext()).playlistDao().insert(newPlaylist);
                Log.d("MainActivity", "Создан плейлист Loved Tracks для пользователя " + currentUserId);
            }
        }).start();
    }

    /**
     * Метод для воспроизведения трека.
     */
    public void playTrack(Song song) {
        currentSong = song;
        int index = currentPlaylist.indexOf(song);
        if (index >= 0) {
            currentTrackIndex = index;
        } else {
            currentPlaylist.clear();
            currentPlaylist.add(song);
            currentTrackIndex = 0;
        }
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
        playerViewModel.setCurrentSong(song);
        if (isFullPlayerVisible()) {
            updateFullPlayerUI();
        } else {
            showMiniPlayer(currentSong);
        }
    }

    /**
     * Воспроизводит трек из переданного плейлиста.
     */
    public void playTrackFromPlaylist(List<Song> playlist, int index) {
        if (playlist == null || playlist.isEmpty() || index < 0 || index >= playlist.size()) {
            return;
        }
        currentPlaylist = playlist;
        currentTrackIndex = index;
        playTrack(playlist.get(index));
    }

    /**
     * Переходит к предыдущему треку (циклически).
     */
    public void playPreviousTrack() {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentTrackIndex = (currentTrackIndex - 1 + currentPlaylist.size()) % currentPlaylist.size();
            playTrack(currentPlaylist.get(currentTrackIndex));
        }
    }

    /**
     * Переходит к следующему треку (циклически).
     */
    public void playNextTrack() {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % currentPlaylist.size();
            playTrack(currentPlaylist.get(currentTrackIndex));
        }
    }

    /**
     * Обновляет UI FullPlayerFragment, если он открыт.
     */
    private void updateFullPlayerUI() {
        Bundle args = new Bundle();
        args.putString("trackName", currentSong.title);
        args.putString("author", currentSong.artist);
        args.putString("coverArt", currentSong.coverArt);
        args.putLong("trackPosition", exoPlayer.getCurrentPosition());
        Fragment fullPlayerFragment = getSupportFragmentManager().findFragmentByTag("full_player_fragment");
        if (fullPlayerFragment instanceof FullPlayerFragment) {
            ((FullPlayerFragment) fullPlayerFragment).updateTrackInfo(args);
        }
    }

    /**
     * Возвращает экземпляр ExoPlayer.
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

    /**
     * Показывает мини-плеер без данных.
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
     * Проверяет, открыт ли FullPlayerFragment.
     */
    private boolean isFullPlayerVisible() {
        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        return fullPlayerContainer != null && fullPlayerContainer.getVisibility() == View.VISIBLE;
    }

    /**
     * Показывает FullPlayerFragment с передачей данных о текущем треке.
     */
    public void showFullPlayer() {
        if (currentSong == null) {
            Log.e("MainActivity", "currentSong is null. Невозможно открыть полноэкранный плеер.");
            return;
        }
        hideMiniPlayer();
        FullPlayerFragment fullPlayerFragment = new FullPlayerFragment();
        Bundle args = new Bundle();
        args.putString("trackName", currentSong.title);
        args.putString("author", currentSong.artist);
        args.putString("coverArt", currentSong.coverArt);
        args.putLong("trackPosition", exoPlayer.getCurrentPosition());
        fullPlayerFragment.setArguments(args);
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
        setBottomNavigationVisibility(false);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.nav_host_fragment_activity_main, fullPlayerFragment, "full_player_fragment")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Скрывает FullPlayerFragment, восстанавливает мини-плеер и показывает нижнюю навигацию.
     */
    public void hideFullPlayer() {
        getSupportFragmentManager().popBackStack();
        Fragment fullPlayerFragment = getSupportFragmentManager().findFragmentByTag("full_player_fragment");
        if (fullPlayerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fullPlayerFragment)
                    .commit();
        }
        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        if (fullPlayerContainer != null) {
            fullPlayerContainer.animate()
                    .translationY(fullPlayerContainer.getHeight())
                    .alpha(0.0f)
                    .setDuration(300)
                    .withEndAction(() -> fullPlayerContainer.setVisibility(View.GONE))
                    .start();
        }
        setBottomNavigationVisibility(true);
        if (currentSong != null) {
            showMiniPlayer(currentSong);
        } else {
            showMiniPlayer();
        }
    }

    /**
     * Удаляет трек из базы данных и останавливает воспроизведение, если он воспроизводится.
     */
    public void deleteTrack(Song song) {
        AppDatabase.getInstance(this).songDao().delete(song);
        Log.d("MainActivity", "Трек удален из базы: " + song.title);
        if (currentSong != null && currentSong.id == song.id) {
            exoPlayer.stop();
            currentSong = null;
        }
    }

    /**
     * Управляет видимостью BottomNavigationView.
     */
    public void setBottomNavigationVisibility(boolean isVisible) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (navView != null) {
            navView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Останавливает воспроизведение, если удаляемая песня воспроизводится.
     */
    public void stopIfPlaying(Song song) {
        if (currentSong != null && currentSong.id == song.id) {
            exoPlayer.stop();
            currentSong = null;
        }
    }

    /**
     * Показывает AlertDialog для переименования трека.
     */
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
            }
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Показывает SettingFragment.
     */
    public void showSetting() {
        SettingFragment settingFragment = new SettingFragment();
        View settingContainer = findViewById(R.id.SettingContainer);
        if (settingContainer != null) {
            settingContainer.setVisibility(View.VISIBLE);
            settingContainer.setTranslationY(settingContainer.getHeight());
            settingContainer.setAlpha(0.0f);
            settingContainer.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(300)
                    .start();
        }
        setBottomNavigationVisibility(false);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.SettingContainer, settingFragment)
                .addToBackStack(null)
                .commit();
    }

    public void hideSetting() {
        Fragment settingFragment = getSupportFragmentManager().findFragmentById(R.id.SettingContainer);
        if (settingFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(settingFragment)
                    .commit();
        }
        View settingContainer = findViewById(R.id.SettingContainer);
        if (settingContainer != null) {
            settingContainer.animate()
                    .translationY(settingContainer.getHeight())
                    .alpha(0.0f)
                    .setDuration(300)
                    .withEndAction(() -> settingContainer.setVisibility(View.GONE))
                    .start();
        }
        setBottomNavigationVisibility(true);
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Показывает полноэкранное представление плейлиста.
     */
    public void showPlaylistAlbum(Playlist playlist) {
        if (playlist == null) {
            Log.e("MainActivity", "Playlist is null!");
            return;
        }
        PlaylistAlbumFragment fragment = new PlaylistAlbumFragment();
        Bundle args = new Bundle();
        args.putInt("playlistId", playlist.id);
        args.putString("playlistName", playlist.name);
        args.putString("playlistImage", playlist.coverImage);
        fragment.setArguments(args);
        View container = findViewById(R.id.playlistAlbumContainer);
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        } else {
            Log.e("MainActivity", "Контейнер playlistAlbumContainer не найден!");
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.playlistAlbumContainer, fragment, "playlist_album_fragment")
                .addToBackStack(null)
                .commit();
    }

    public void hidePlaylistAlbum() {
        View container = findViewById(R.id.playlistAlbumContainer);
        if (container != null) {
            container.setVisibility(View.GONE);
        }
    }

    /**
     * Показывает диалог для добавления трека в плейлист.
     */
    public void showAddToPlaylistDialog(Song track) {
        new Thread(() -> {
            // Получаем список плейлистов текущего пользователя
            List<Playlist> playlists = AppDatabase.getInstance(this)
                    .playlistDao().getPlaylistsByUserId(currentUserId);

            runOnUiThread(() -> {
                if (playlists == null || playlists.isEmpty()) {
                    Toast.makeText(this, "Сначала создайте плейлист", Toast.LENGTH_SHORT).show();
                } else {
                    // Создаем массив названий и массив ID
                    String[] playlistNames = new String[playlists.size()];
                    int[] playlistIds = new int[playlists.size()];
                    for (int i = 0; i < playlists.size(); i++) {
                        playlistNames[i] = playlists.get(i).name;
                        playlistIds[i] = playlists.get(i).id;
                    }
                    new AlertDialog.Builder(this)
                            .setTitle("Выберите плейлист")
                            .setItems(playlistNames, (dialog, which) -> {
                                int selectedPlaylistId = playlistIds[which];
                                // Создаем связь между треком и плейлистом
                                PlaylistSong playlistSong = new PlaylistSong(selectedPlaylistId, track.id);
                                new Thread(() -> {
                                    AppDatabase.getInstance(this)
                                            .playlistSongDao().insert(playlistSong);
                                    runOnUiThread(() -> Toast.makeText(this, "Трек добавлен в плейлист", Toast.LENGTH_SHORT).show());
                                }).start();
                            })
                            .show();
                }
            });
        }).start();
    }


    public void hidePlaylistAlbum2() {
        View container = findViewById(R.id.playlistAlbumContainer);
        if (container != null) {
            container.setVisibility(View.GONE);
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
