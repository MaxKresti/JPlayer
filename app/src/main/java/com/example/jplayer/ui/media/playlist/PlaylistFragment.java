package com.example.jplayer.ui.media.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jplayer.R;

public class PlaylistFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Надуваем макет фрагмента
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Находим контейнер с плейлистами
        LinearLayout playlistsContainer = view.findViewById(R.id.playlistContainer);

        // Перебираем все элементы плейлистов
        for (int i = 0; i < playlistsContainer.getChildCount(); i++) {
            View playlistItem = playlistsContainer.getChildAt(i);
            ImageView playlistMenu = playlistItem.findViewById(R.id.playlistMenu);

            // Создаем финальную копию переменной i
            final int position = i;
            playlistMenu.setOnClickListener(v -> showContextMenu(v, position)); // Теперь position effectively final
        }
    }

    /**
     * Показывает контекстное меню для плейлиста.
     *
     * @param anchorView View, к которому привязывается меню (кнопка с тремя точками).
     * @param position   Позиция плейлиста в списке.
     */
    private void showContextMenu(View anchorView, int position) {
        PlaylistMenuSheetDialogFragment bottomSheet = new PlaylistMenuSheetDialogFragment();
        bottomSheet.setPosition(position);
        bottomSheet.setOnMenuItemClickListener(new PlaylistMenuSheetDialogFragment.OnMenuItemClickListener() {


            @Override
            public void onRename(int position) {

            }

            @Override
            public void onDelete(int position) {

            }
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }
}