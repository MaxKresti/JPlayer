package com.example.jplayer.ui.media.track;

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

import com.example.jplayer.R;
import com.example.jplayer.adapters.TrackAdapter;
import com.example.jplayer.database.song.Song;

import java.util.ArrayList;
import java.util.List;

public class TrackFragment extends Fragment {
    private TrackAdapter trackAdapter;
    private TrackViewModel trackViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView trackList = view.findViewById(R.id.trackList);
        trackList.setLayoutManager(new LinearLayoutManager(getContext()));

        trackAdapter = new TrackAdapter(getContext(), new ArrayList<>());
        trackList.setAdapter(trackAdapter);

        trackViewModel = new ViewModelProvider(this).get(TrackViewModel.class);

        int userId = 1; // Здесь можно получить ID текущего пользователя
        trackViewModel.getSongsByUser(userId).observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null) {
                trackAdapter.updateTracks(tracks);
            }
        });
    }
}
