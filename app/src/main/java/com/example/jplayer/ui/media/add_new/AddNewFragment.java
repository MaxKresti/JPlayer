package com.example.jplayer.ui.media.add_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.jplayer.R;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.FragmentAddNewBinding;
import com.example.jplayer.ui.CreatePlaylistDialogFragment;
import com.example.jplayer.ui.media.playlist.PlaylistFragment;
import com.example.jplayer.ui.media.playlist.PlaylistViewModel;

import java.io.File;
import java.io.FileOutputStream;

public class AddNewFragment extends Fragment {

    private FragmentAddNewBinding binding;
    private AppDatabase db;
    private int currentUserId;
    private static final int PICK_AUDIO_FILE = 1001;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddNewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = AppDatabase.getInstance(requireContext());
        currentUserId = getCurrentUserId();

        if (currentUserId == -1) {
            showToast("Ошибка: не удалось получить ID пользователя");
            return root;
        }

        // Настройка анимации для кнопок
        setupImageAnimation(binding.newTrack);
        setupImageAnimation(binding.newAlbum);
        setupImageAnimation(binding.newPlaylist);

        // Обработчик для добавления трека
        binding.addTrack.setOnClickListener(v -> openFileChooser());

        // Обработчик для создания плейлиста
        // Обработчик для создания плейлиста
        binding.addPlaylist.setOnClickListener(v -> {
            CreatePlaylistDialogFragment dialog = new CreatePlaylistDialogFragment();
            dialog.setOnPlaylistCreatedListener(playlist -> {
                int userId = getCurrentUserId();
                Playlist newPlaylist = new Playlist(userId, playlist.name, playlist.coverImage);

                new Thread(() -> {
                    AppDatabase.getInstance(requireContext()).playlistDao().insert(newPlaylist);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Плейлист создан: " + newPlaylist.name, Toast.LENGTH_SHORT).show();

                        // Ищем PlaylistFragment по тегу и вызываем обновление
                        PlaylistFragment playlistFragment = (PlaylistFragment) getParentFragmentManager()
                                .findFragmentByTag("playlist_fragment");

                        if (playlistFragment != null) {
                            playlistFragment.refreshPlaylists();
                        }
                    });
                }).start();
            });
            dialog.show(getChildFragmentManager(), "create_playlist_dialog");
        });




        return root;
    }

    private void setupImageAnimation(ImageView imageView) {
        Animation scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.smaller);
        Animation scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.bigger);

        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.startAnimation(scaleDown);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.startAnimation(scaleUp);
                    break;
            }
            return false;
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_FILE);
    }

    private void openCreatePlaylistDialog() {
        CreatePlaylistDialogFragment dialog = new CreatePlaylistDialogFragment();
        dialog.setOnPlaylistCreatedListener(playlist -> {
            // Сохраняем плейлист в базу данных (например, через App    Database)
            AppDatabase.getInstance(requireContext()).playlistDao().insert(playlist);
            Toast.makeText(requireContext(), "Плейлист создан: " + playlist.name, Toast.LENGTH_SHORT).show();
            // Если нужно – обновляем UI (например, обновляем список плейлистов)
        });
        dialog.show(getChildFragmentManager(), "create_playlist_dialog");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_FILE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                // Сохраняем постоянное разрешение на доступ к URI
                requireContext().getContentResolver().takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                processSelectedFile(uri);
            }
        }
    }

    private void processSelectedFile(Uri uri) {
        try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
            mmr.setDataSource(requireContext(), uri);

            String title = extractMetadata(mmr, MediaMetadataRetriever.METADATA_KEY_TITLE, getFileName(uri));
            String artist = extractMetadata(mmr, MediaMetadataRetriever.METADATA_KEY_ARTIST, "Unknown Artist");
            String album = extractMetadata(mmr, MediaMetadataRetriever.METADATA_KEY_ALBUM, "Unknown Album");
            long duration = extractDuration(mmr);
            String coverPath = saveCoverArt(extractCoverArt(mmr));

            Song song = new Song(
                    currentUserId, title, artist, album, duration, uri.toString(), coverPath, false
            );

            if (db.songDao().checkSongExists(uri.toString(), currentUserId) == 0) {
                db.songDao().insert(song);
                showToast("Трек добавлен: " + song.title);
            } else {
                showToast("Трек уже существует");
            }

        } catch (Exception e) {
            showToast("Ошибка обработки файла");
            e.printStackTrace();
        }
    }

    private String extractMetadata(MediaMetadataRetriever mmr, int key, String defaultValue) {
        String value = mmr.extractMetadata(key);
        return value != null ? value.trim() : defaultValue;
    }

    private long extractDuration(MediaMetadataRetriever mmr) {
        try {
            return Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Bitmap extractCoverArt(MediaMetadataRetriever mmr) {
        byte[] artBytes = mmr.getEmbeddedPicture();
        return (artBytes != null) ? BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length) : null;
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        return (result != null) ? result : uri.getLastPathSegment();
    }

    private String saveCoverArt(Bitmap bitmap) {
        if (bitmap == null) return null;
        try {
            File coversDir = new File(requireContext().getFilesDir(), "covers");
            if (!coversDir.exists() && !coversDir.mkdirs()) {
                return null;
            }
            String fileName = "cover_" + System.currentTimeMillis() + ".png";
            File outputFile = new File(coversDir, fileName);
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                return outputFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getCurrentUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getInt("user_id", -1);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
