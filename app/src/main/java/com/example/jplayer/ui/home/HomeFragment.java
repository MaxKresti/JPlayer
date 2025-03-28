    package com.example.jplayer.ui.home;


import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.adapters.TrackAdapter;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.databinding.FragmentHomeBinding;
import com.example.jplayer.ui.setting.SettingFragment;

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


            setupImageAnimation(binding.setting);


            db = AppDatabase.getInstance(requireContext());
            currentUserId = getCurrentUserId();


            setupRecyclerView();
            setupTrackList();


            binding.setting.setOnClickListener(v -> {

                SettingFragment settingFragment = new SettingFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.nav_host_fragment_activity_main, settingFragment);

                transaction.addToBackStack(null);

                transaction.commit();
            });

            return root;
        }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Находим контейнер с альбомами
        LinearLayout homeContainer = view.findViewById(R.id.nav_host_fragment_activity_main);



        ImageView setting = view.findViewById(R.id.setting); // Находим изображение альбома

        setting.setOnClickListener(v -> openSetting());

    }
    private void setupImageAnimation(ImageView imageView) {
        Animation scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.smaller);
        Animation scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.bigger);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(scaleDown);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(scaleUp);
                        break;
                }
                return false; // Возвращаем false, чтобы не перехватывать событие
            }
        });
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


        binding.tracksContainer.setAdapter(adapter);
    }

    private void openSetting() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showSetting(); // Используем метод из MainActivity
        }


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