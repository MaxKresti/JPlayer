package com.example.jplayer.ui.media.playlist;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
import com.example.jplayer.ui.media.track.TrackMenuSheetDialogFragment;
import com.google.android.material.appbar.AppBarLayout;

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

        // Инициализация UI-элементов через binding
        binding.back.setOnClickListener(v -> closePlaylistAlbum());
        binding.play.setOnClickListener(v -> togglePlayPause());

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

        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout);


        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            float alpha = 1 - Math.abs(verticalOffset / (float) appBarLayout1.getTotalScrollRange());
            binding.playlistCover.setAlpha(alpha);
        });

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
     * Назначает обработчики кликов для каждого элемента трека в контейнере.
     * Если необходимо, здесь можно настроить RecyclerView для треков.
     */
    private void updateTracksUI(List<Song> songs) {
        if (trackAdapter != null) {
            trackAdapter.updateTracks(songs);
        }
    }

    /**
     * Закрывает PlaylistAlbumFragment, вызывая метод из MainActivity.
     */
    private void closePlaylistAlbum() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hidePlaylistAlbum();
        }
    }

    /**
     * Переключает воспроизведение и паузу.
     */
    private void togglePlayPause() {
        // Здесь нужно добавить логику воспроизведения/паузы трека для плейлиста.
        // Пример анимации для смены иконки:
        if (binding.play.getTag() != null && binding.play.getTag().equals("playing")) {
            pauseTrack();
        } else {
            playTrack();
        }
    }

    private void playTrack() {
        // Логика для начала воспроизведения трека плейлиста

        animateButton(binding.play, R.drawable.pause); // Здесь замените на нужную иконку паузы


        binding.play.setTag("playing");
        // Добавьте вызов логики для воспроизведения трека
    }

    private void pauseTrack() {
        // Логика для паузы трека плейлиста

        animateButton(binding.play, R.drawable.play); // Здесь замените на нужную иконку воспроизведения

        binding.play.setTag("paused");
        // Добавьте вызов логики для постановки трека на паузу
    }

    /**
     * Выполняет анимацию смены иконки на переданном ImageView.
     */
    private void animateButton(ImageView button, int newIcon) {
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) {
                button.setImageResource(newIcon);
                button.startAnimation(scaleDown);
            }
            @Override public void onAnimationRepeat(Animation animation) { }
        });
        button.startAnimation(scaleUp);
    }

    /**
     * Показывает контекстное меню для трека.
     */
    private void showContextMenu(View anchorView, int position) {
        TrackMenuSheetDialogFragment bottomSheet = new TrackMenuSheetDialogFragment();
        bottomSheet.setPosition(position);
        bottomSheet.setOnMenuItemClickListener(new TrackMenuSheetDialogFragment.OnMenuItemClickListener() {
            @Override
            public void onAddToPlaylist(int position) {
                // Обработка добавления трека в плейлист
            }
            @Override
            public void onRename(int position) {
                // Обработка переименования трека
            }
            @Override
            public void onDelete(int position) {
                // Обработка удаления трека
            }
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }
}
