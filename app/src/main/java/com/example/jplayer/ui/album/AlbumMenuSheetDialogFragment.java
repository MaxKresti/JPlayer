package com.example.jplayer.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jplayer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AlbumMenuSheetDialogFragment extends BottomSheetDialogFragment {

    private OnMenuItemClickListener listener;
    private int position;

    public interface OnMenuItemClickListener {
        void onRename(int position);
        void onDelete(int position);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Надуваем макет для контекстного меню альбома
        View view = inflater.inflate(R.layout.context_menu_album, container, false);

        // Находим элементы меню
        LinearLayout rename = view.findViewById(R.id.rename);
        LinearLayout delete = view.findViewById(R.id.delete);

        // Обработка клика на "Переименовать"
        rename.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRename(position);
            }
            dismiss(); // Закрываем BottomSheet после выбора
        });

        // Обработка клика на "Удалить"
        delete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(position);
            }
            dismiss(); // Закрываем BottomSheet после выбора
        });

        return view;
    }
}