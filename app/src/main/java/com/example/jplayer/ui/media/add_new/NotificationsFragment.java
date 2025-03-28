package com.example.jplayer.ui.media.add_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

import com.example.jplayer.R;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.FragmentAddNewBinding;

import java.io.File;
import java.io.FileOutputStream;

public class NotificationsFragment extends Fragment {

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

        // Настройка анимации для ImageView
        setupImageAnimation(binding.newTrack);
        setupImageAnimation(binding.newAlbum);
        setupImageAnimation(binding.newPlaylist);

        // Обработчик нажатия на кнопку добавления трека
        binding.addTrack.setOnClickListener(v -> openFileChooser());

        return root;
    }

    // Метод для настройки анимации ImageView
    private void setupImageAnimation(ImageView imageView) {
        Animation scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.smaller);
        Animation scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.bigger);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(scaleDown);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(scaleUp);
                        break;
                }
                return false; // Не перехватываем событие
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_FILE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                // Сохраняем постоянное разрешение на доступ к URI
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION &
                        (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                requireContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);

                processSelectedFile(uri);
            }
        }
    }

    private void processSelectedFile(Uri uri) {
        try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
            mmr.setDataSource(requireContext(), uri);

            String title = extractMetadata(mmr, MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = extractMetadata(mmr, MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String album = extractMetadata(mmr, MediaMetadataRetriever.METADATA_KEY_ALBUM);
            long duration = extractDuration(mmr);
            String fileName = getFileName(uri);

            Bitmap cover = extractCoverArt(mmr);
            String coverPath = saveCoverArt(cover);

            Song song = new Song(
                    currentUserId,
                    !title.isEmpty() ? title : fileName,
                    !artist.isEmpty() ? artist : "Unknown Artist",
                    !album.isEmpty() ? album : "Unknown Album",
                    duration,
                    uri.toString(),  // сохраняем URI как строку
                    coverPath
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

    private String extractMetadata(MediaMetadataRetriever mmr, int key) {
        String value = mmr.extractMetadata(key);
        return value != null ? value.trim() : "";
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
        if (artBytes != null) {
            return BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
        }
        return null;
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = requireContext().getContentResolver()
                    .query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result != null ? result : "unknown_file";
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
        // Реализация получения ID текущего пользователя (пример через SharedPreferences)
        return 1; // Временная заглушка
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
