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
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;
import java.util.List;

public class PlaylistFragment extends Fragment implements PlaylistAdapter.OnPlaylistClickListener {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;
    private int currentUserId; // Теперь получаем динамически

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = view.findViewById(R.id.playlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PlaylistAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);

        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        playlistViewModel.getPlaylistsLiveData().observe(getViewLifecycleOwner(), this::updatePlaylistData);

        loadPlaylists();
        return view;
    }

    private void loadPlaylists() {
        SharedPreferences preferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        currentUserId = preferences.getInt("currentUserId", -1);

        if (currentUserId == -1) {
            Log.e("PlaylistFragment", "Не удалось получить currentUserId!");
            return;
        }

        new Thread(() -> {
            List<Playlist> playlists = AppDatabase.getInstance(requireContext())
                    .playlistDao()
                    .getPlaylistsByUserId(currentUserId);

            requireActivity().runOnUiThread(() -> adapter.updateData(playlists));
        }).start();
    }


    private void updatePlaylistData(List<Playlist> playlists) {
        adapter.updateData(playlists);
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
