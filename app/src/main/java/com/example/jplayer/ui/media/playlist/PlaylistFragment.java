package com.example.jplayer.ui.media.playlist;

import android.os.Bundle;
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
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;
import java.util.List;

public class PlaylistFragment extends Fragment implements PlaylistAdapter.OnPlaylistClickListener {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;
    private int currentUserId = 1; // Получи реальный userId

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = view.findViewById(R.id.playlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PlaylistAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);

        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        playlistViewModel.getPlaylistsLiveData().observe(getViewLifecycleOwner(), playlists -> {
            adapter.updateData(playlists);
        });

        loadPlaylists();
        return view;
    }

    private void loadPlaylists() {
        new Thread(() -> {
            List<Playlist> playlists = AppDatabase.getInstance(requireContext()).playlistDao().getPlaylistsByUserId(currentUserId);
            requireActivity().runOnUiThread(() -> adapter.updateData(playlists));
        }).start();
    }

    @Override
    public void onPlaylistClick(int playlistId) {
        openPlaylistAlbumFragment();
    }

    @Override
    public void onPlaylistMenuClick(View view, int position) {
        showContextMenu(view, position);
    }

    private void openPlaylistAlbumFragment() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showPlaylistAlbum(); // Используем метод из MainActivity
        }
    }

    private void showContextMenu(View anchorView, int position) {
        PlaylistMenuSheetDialogFragment bottomSheet = new PlaylistMenuSheetDialogFragment();
        bottomSheet.setPosition(position);
        bottomSheet.setOnMenuItemClickListener(new PlaylistMenuSheetDialogFragment.OnMenuItemClickListener() {
            @Override
            public void onRename(int position) {
                // Логика переименования
            }

            @Override
            public void onDelete(int position) {
                // Логика удаления
            }
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }
}
