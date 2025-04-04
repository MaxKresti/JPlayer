package com.example.jplayer.ui.media.playlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.adapters.PlaylistAdapter;
import com.example.jplayer.database.playlist.Playlist;

public class PlaylistFragment extends Fragment implements PlaylistAdapter.OnPlaylistClickListener {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;
    private int currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        recyclerView = view.findViewById(R.id.playlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlaylistAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);

        currentUserId = getCurrentUserId();

        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        playlistViewModel.getPlaylistsLiveData().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null) {
                adapter.updateData(playlists);
            } else {
                Log.w("PlaylistFragment", "Плейлисты не найдены для пользователя " + currentUserId);
            }
        });

        if (currentUserId != -1) {
            playlistViewModel.loadPlaylists(requireContext(), currentUserId);
        } else {
            Log.e("PlaylistFragment", "Ошибка: не удалось получить ID пользователя!");
        }

        return view;
    }

    private int getCurrentUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getInt("user_id", -1);
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showPlaylistAlbum(playlist);
        }
    }

    @Override
    public void onPlaylistMenuClick(View view, int position) {
        showContextMenu(view, position);
    }

    private void showContextMenu(View anchorView, int position) {
        PlaylistMenuSheetDialogFragment bottomSheet = new PlaylistMenuSheetDialogFragment();
        bottomSheet.setPosition(position);
        bottomSheet.setOnMenuItemClickListener(new PlaylistMenuSheetDialogFragment.OnMenuItemClickListener() {
            @Override
            public void onRename(int pos) {
                // Логика переименования
            }

            @Override
            public void onDelete(int pos) {
                // Логика удаления
            }
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }

    public void refreshPlaylists() {
        if (currentUserId != -1) {
            playlistViewModel.loadPlaylists(requireContext(), currentUserId);
        }
    }
}
