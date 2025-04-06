package com.example.jplayer.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.adapters.TrackAdapter;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.FragmentPlaylistAlbumBinding;
import com.example.jplayer.ui.media.opened.PlaylistAlbumViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAlbumFragment extends Fragment {

    private String playlistName;
    private String playlistImage;
    private int playlistId;
    private FragmentPlaylistAlbumBinding binding;
    private TrackAdapter trackAdapter;
    private PlaylistAlbumViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistAlbumBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        view.setClickable(true);

        // Назад
        binding.back.setOnClickListener(v -> closePlaylistAlbum());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlistId = getArguments().getInt("playlistId", -1);
            playlistName = getArguments().getString("playlistName", "Плейлист");
            playlistImage = getArguments().getString("playlistImage", "");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Установка названия и обложки
        binding.playlistTitle.setText(playlistName);
        if (playlistImage != null && !playlistImage.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(playlistImage))
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image)
                    .into(binding.playlistCover);
        } else {
            binding.playlistCover.setImageResource(R.drawable.image);
        }

        // Настройка RecyclerView
        RecyclerView recyclerView = binding.playlistTracksRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        trackAdapter = new TrackAdapter(requireContext(), new ArrayList<>(), R.layout.item_track);
        recyclerView.setAdapter(trackAdapter);

        // Подключаем ViewModel
        viewModel = new ViewModelProvider(this).get(PlaylistAlbumViewModel.class);
        viewModel.getSongsForPlaylist(playlistId).observe(getViewLifecycleOwner(), this::updateTracksUI);

        // Кнопка назад
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                closePlaylistAlbum();
                return true;
            }
            return false;
        });
    }

    /**
     * Обновляет список треков в адаптере
     */
    private void updateTracksUI(List<Song> songs) {
        if (trackAdapter != null) {
            trackAdapter.updateTracks(songs);
        }
    }

    /**
     * Закрывает фрагмент
     */
    private void closePlaylistAlbum() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hidePlaylistAlbum();
        }
    }
}
