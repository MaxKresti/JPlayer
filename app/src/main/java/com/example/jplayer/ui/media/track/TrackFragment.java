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

        RecyclerView trackRecyclerView = view.findViewById(R.id.trackList);
        trackRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        trackAdapter = new TrackAdapter(getContext(), new ArrayList<>(), R.layout.item_track);
        trackRecyclerView.setAdapter(trackAdapter);

        trackViewModel = new ViewModelProvider(this).get(TrackViewModel.class);

        trackViewModel.getSongsByUser(1).observe(getViewLifecycleOwner(), this::updateTrackList);
    }

    private void updateTrackList(List<Song> tracks) {
        if (tracks != null) {
            trackAdapter.updateTracks(tracks);
        }
    }
}
