package com.example.jplayer.ui.media.track;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jplayer.MainActivity; // Импорт MainActivity
import com.example.jplayer.R;
import com.example.jplayer.databinding.FragmentTrackBinding; // Импорт сгенерированного Binding-класса


public class TrackFragment extends Fragment {

    private FragmentTrackBinding binding; // Используем ViewBinding

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Инициализация ViewBinding
        binding = FragmentTrackBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout tracksContainer = binding.tracksContainer;

        for (int i = 0; i < tracksContainer.getChildCount(); i++) {
            View trackItem = tracksContainer.getChildAt(i);
            ImageView trackImage = trackItem.findViewById(R.id.trackImage);
            ImageView trackMenu = trackItem.findViewById(R.id.trackMenu);

            // Создаем финальную копию переменной i
            final int position = i;

            // Обработка клика на кнопку с тремя точками
            trackMenu.setOnClickListener(v -> showContextMenu(v, position));

            // Обработка клика на изображение трека
            trackImage.setOnClickListener(v -> showMiniPlayer(position));

            // Обработка клика на весь элемент трека
            trackItem.setOnClickListener(v -> showMiniPlayer(position));
        }
    }

    /**
     * Показывает мини-плеер.
     *
     * @param position Позиция трека в списке.
     */
    private void showMiniPlayer(int position) {
        // Проверяем, что активити является MainActivity
        if (getActivity() instanceof MainActivity) {
            // Вызываем метод showMiniPlayer() из MainActivity
            ((MainActivity) getActivity()).showMiniPlayer();
        }
    }

    /**
     * Показывает контекстное меню для трека.
     *
     * @param anchorView View, к которому привязывается меню (кнопка с тремя точками).
     * @param position   Позиция трека в списке.
     */
    private void showContextMenu(View anchorView, int position) {
        TrackMenuSheetDialogFragment bottomSheet = new TrackMenuSheetDialogFragment();
        bottomSheet.setPosition(position);
        bottomSheet.setOnMenuItemClickListener(new TrackMenuSheetDialogFragment.OnMenuItemClickListener() {
            @Override
            public void onAddToPlaylist(int position) {
                // Обработка добавления в плейлист
            }

            @Override
            public void onRename(int position) {
                // Обработка переименования
            }

            @Override
            public void onDelete(int position) {
                // Обработка удаления
            }
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }
}