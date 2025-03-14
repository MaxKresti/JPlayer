package com.example.jplayer.ui.enter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jplayer.databinding.FragmentEnterBinding;
import com.example.jplayer.ui.LoginActivity;

public class EnterFragment extends Fragment {

    private FragmentEnterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEnterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Логика для обработки нажатий на кнопки и поля ввода
        binding.loginButton.setOnClickListener(v -> {
            // Обработка входа
        });

        // Переход на фрагмент регистрации
        binding.registerButton.setOnClickListener(v -> {
            if (getActivity() instanceof LoginActivity) {
                ((LoginActivity) getActivity()).navigateToRegistration();
            }
        });

        binding.authorName.setOnClickListener(v -> {
            // Обработка "Забыли пароль?"
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}