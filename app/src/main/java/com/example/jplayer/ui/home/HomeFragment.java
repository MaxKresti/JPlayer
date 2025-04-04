package com.example.jplayer.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.adapters.TrackAdapter;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.database.user.User;
import com.example.jplayer.database.user.UserRepository;
import com.example.jplayer.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Calendar;

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
        setupImageAnimation(binding.setting);
        loadUserName(); // Загружаем имя пользователя из БД

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setting.setOnClickListener(v -> openSetting());
    }

    private void setupImageAnimation(ImageView imageView) {
        Animation scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.smaller);
        Animation scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.bigger);

        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.startAnimation(scaleDown);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.startAnimation(scaleUp);
                    break;
            }
            return false;
        });
    }

    private void loadUserName() {
        int userId = getCurrentUserId();

        if (userId == -1) {
            binding.greetingText.setText(getGreetingMessage("Гость"));
            return;
        }

        UserRepository userRepository = new UserRepository(requireContext());
        userRepository.getUserById(userId, user -> {
            String greeting = getGreetingMessage(user != null ? user.username : "Гость");
            requireActivity().runOnUiThread(() -> binding.greetingText.setText(greeting));
        });
    }


    private String getGreetingMessage(String username) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Доброе утро";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Добрый день";
        } else if (hour >= 17 && hour < 23) {
            greeting = "Добрый вечер";
        } else {
            greeting = "Доброй ночи";
        }

        return greeting + ", " + username + "!";
    }

    private void setupRecyclerView() {
        binding.tracksContainer.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
        ));
        adapter = new TrackAdapter(requireContext(), new ArrayList<>(), R.layout.main_track);
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
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getInt("user_id", -1);
    }

    private void openSetting() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showSetting();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
