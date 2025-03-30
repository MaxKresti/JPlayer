package com.example.jplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.jplayer.R;
import java.io.File;

public class CreatePlaylistDialogFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 2001;
    private EditText playlistNameEditText;
    private ImageView coverImageView;
    private Button cancelButton, confirmButton;

    private Uri selectedImageUri = null;

    // Интерфейс обратного вызова для передачи данных созданного плейлиста
    public interface OnPlaylistCreatedListener {
        void onPlaylistCreated(String name, String coverImagePath);
    }

    private OnPlaylistCreatedListener listener;

    public void setOnPlaylistCreatedListener(OnPlaylistCreatedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Устанавливаем диалог на всю ширину экрана, высота - wrap_content
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Инфлейтим макет диалога (оформленный под темный стиль)
        View view = inflater.inflate(R.layout.dialog_create_playlist, container, false);

        playlistNameEditText = view.findViewById(R.id.playlistNameEditText);
        coverImageView = view.findViewById(R.id.coverImageView);
        cancelButton = view.findViewById(R.id.cancelButton);
        confirmButton = view.findViewById(R.id.confirmButton);

        // Обработчик клика на обложку: выбор изображения
        coverImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Кнопка "Отмена" просто закрывает диалог
        cancelButton.setOnClickListener(v -> dismiss());

        // Кнопка "Создать" – проверяет введенное название, передает данные через слушатель и закрывает диалог
        confirmButton.setOnClickListener(v -> {
            String playlistName = playlistNameEditText.getText().toString().trim();
            if (TextUtils.isEmpty(playlistName)) {
                Toast.makeText(getContext(), "Введите название плейлиста", Toast.LENGTH_SHORT).show();
                return;
            }
            // Если выбран URI, сохраняем его строковое представление, иначе пустая строка
            String coverImagePath = selectedImageUri != null ? selectedImageUri.toString() : "";
            if (listener != null) {
                listener.onPlaylistCreated(playlistName, coverImagePath);
            }
            dismiss();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Получаем постоянное разрешение на чтение (если требуется)
                int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                requireContext().getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
                coverImageView.setImageURI(selectedImageUri);
            }
        }
    }
}
