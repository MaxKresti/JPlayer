package com.example.jplayer.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;

public class AlbumFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Надуваем макет фрагмента
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Находим контейнер с альбомами
        LinearLayout albumsContainer = view.findViewById(R.id.albumsContainer);

        // Перебираем все элементы альбомов
        for (int i = 0; i < albumsContainer.getChildCount(); i++) {
            View albumItem = albumsContainer.getChildAt(i);
            ImageView albumMenu = albumItem.findViewById(R.id.albumMenu);
            ImageView albumImage = albumItem.findViewById(R.id.albumImage); // Находим изображение альбома

            // Создаем финальную копию переменной i
            final int position = i;

            // Обработка клика на кнопку с тремя точками
            albumMenu.setOnClickListener(v -> showContextMenu(v, position));

            // Обработка клика на изображение альбома
            albumImage.setOnClickListener(v -> openPlaylistAlbumFragment());
        }
    }

    /**
     * Открывает PlaylistAlbumFragment.
     */
    private void openPlaylistAlbumFragment() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showPlaylistAlbum(); // Используем метод из MainActivity
        }
    }

    /**
     * Показывает контекстное меню для альбома.
     *
     * @param anchorView View, к которому привязывается меню (кнопка с тремя точками).
     * @param position   Позиция альбома в списке.
     */
    private void showContextMenu(View anchorView, int position) {
        AlbumMenuSheetDialogFragment bottomSheet = new AlbumMenuSheetDialogFragment();
        bottomSheet.setPosition(position);
        bottomSheet.setOnMenuItemClickListener(new AlbumMenuSheetDialogFragment.OnMenuItemClickListener() {
            @Override
            public void onRename(int position) {
                // Обработка переименования альбома
            }

            @Override
            public void onDelete(int position) {
                // Обработка удаления альбома
            }
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }
}