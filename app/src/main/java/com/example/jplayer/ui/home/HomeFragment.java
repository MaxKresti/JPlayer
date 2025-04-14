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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AppDatabase db;
    private int currentUserId;
    private TrackAdapter recentAddedAdapter;
    private TrackAdapter recentPlayedAdapter;
    private TrackAdapter recommendedAdapter;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = AppDatabase.getInstance(requireContext());
        currentUserId = getCurrentUserId();

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Настраиваем RecyclerView для последних добавленных треков
        binding.recentAddedRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recentAddedAdapter = new TrackAdapter(requireContext(), new ArrayList<>(), R.layout.main_track);
        binding.recentAddedRecyclerView.setAdapter(recentAddedAdapter);

        // Настраиваем RecyclerView для последних прослушанных треков
        binding.recentPlayedRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recentPlayedAdapter = new TrackAdapter(requireContext(), new ArrayList<>(), R.layout.main_track);
        binding.recentPlayedRecyclerView.setAdapter(recentPlayedAdapter);

        // Настраиваем RecyclerView для рекомендаций (рандомные треки)
        binding.recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendedAdapter = new TrackAdapter(requireContext(), new ArrayList<>(), R.layout.main_track);
        binding.recommendedRecyclerView.setAdapter(recommendedAdapter);

        setupImageAnimation(binding.setting);
        loadUserName();
        setupSearch();

        // Подписываемся на LiveData из ViewModel
        viewModel.getRecentAddedSongs(currentUserId).observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                recentAddedAdapter.updateTracks(songs);
            }
        });

        viewModel.getRecentPlayedSongs(currentUserId).observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                recentPlayedAdapter.updateTracks(songs);
            }
        });

        viewModel.getRecommendedSongs(currentUserId).observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                recommendedAdapter.updateTracks(songs);
            }
        });

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

    private void setupSearch() {
        // Реализуйте настройку поиска, если требуется (например, обработчик для EditText)
    }

    private void loadUserName() {
        int userId = getCurrentUserId();
        if (userId == -1) {
            binding.greetingText.setText(getGreetingMessage("Гость"));
            return;
        }
        UserRepository userRepository = new UserRepository(requireContext());
        userRepository.getUserById(userId, (User user) -> {
            String greeting = getGreetingMessage(user != null ? user.username : "Гость");
            requireActivity().runOnUiThread(() -> binding.greetingText.setText(greeting));
        });
    }

    private String getGreetingMessage(String username) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Доброе утро 🌄";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Добрый день ⛅";
        } else if (hour >= 17 && hour < 23) {
            greeting = "Добрый вечер 🌇";
        } else {
            greeting = "Доброй ночи 🌑🌃";
        }
        return greeting + ", " + username + " ✌️";
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
