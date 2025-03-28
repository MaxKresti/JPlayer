    package com.example.jplayer.ui.home;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.jplayer.R;
    import com.example.jplayer.adapters.TrackAdapter;
    import com.example.jplayer.database.AppDatabase;
    import com.example.jplayer.database.song.Song;
    import com.example.jplayer.databinding.FragmentHomeBinding;
    import com.example.jplayer.ui.login.LoginActivity;

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

            binding.logoutButton.setOnClickListener(v -> logout());

            return root;
        }
        private void logout() {
            // Используем requireContext(), так как getSharedPreferences() есть только у Context
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Очищаем сохранённые данные
            editor.apply();

            // Используем requireActivity() вместо this, так как Fragment не является Context
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Очищаем стек активностей
            startActivity(intent);

            // Завершаем активность, чтобы пользователь не мог вернуться назад кнопкой "Назад"
            requireActivity().finish();
        }

        private void setupRecyclerView() {
            // Настройка RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );

            binding.tracksContainer.setLayoutManager(layoutManager);
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
            // Ваша реализация получения ID пользователя
            return 1;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }
    }