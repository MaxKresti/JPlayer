package com.example.jplayer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jplayer.adapters.TrackAdapter;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private AppDatabase db;
    private int currentUserId;
    private TrackAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = AppDatabase.getInstance(requireContext());
        currentUserId = getCurrentUserId();

        setupRecyclerView();
        setupTrackList();

        return root;
    }

    private void setupRecyclerView() {
        // Настройка RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        binding.tracksContainer.setLayoutManager(layoutManager);
        adapter = new TrackAdapter(requireContext(), new ArrayList<>());
        binding.tracksContainer.setAdapter(adapter);
    }

    private void setupTrackList() {
        db.songDao().getRecentSongsLive(currentUserId)
                .observe(getViewLifecycleOwner(), songs -> {
                    if (songs != null && !songs.isEmpty()) {
                        adapter.updateTracks(songs);
                    }
                });
    }

    private int getCurrentUserId() {
        // Ваша реализация получения ID пользователя
        return 1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}