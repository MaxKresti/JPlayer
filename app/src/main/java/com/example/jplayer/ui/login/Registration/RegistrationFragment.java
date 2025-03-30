package com.example.jplayer.ui.login.Registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jplayer.R;
import com.example.jplayer.database.AppDatabase;

import com.example.jplayer.database.user.User;
import com.example.jplayer.databinding.FragmentRegistrationBinding;
import com.example.jplayer.ui.login.LoginActivity;


public class RegistrationFragment extends Fragment {
    private FragmentRegistrationBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);

        binding.back.setOnClickListener(v -> ((LoginActivity) requireActivity()).navigateToEnter());
        binding.registerButton.setOnClickListener(v -> processRegistration());
        setupImageAnimation(binding.back);


        return binding.getRoot();
    }

    private void processRegistration() {
        String username = binding.nameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        String secretAnswer = binding.secretAnswerInput.getText().toString().trim();
        String secretQuestion = "Ваше любимое блюдо?";

        if (validateInput(username, password, secretAnswer)) {
            AppDatabase db = AppDatabase.getInstance(requireContext());

            // Проверка существования пользователя
            if (db.userDao().checkUsernameExists(username) > 0) {
                Toast.makeText(requireContext(), "Пользователь уже существует!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Создание и сохранение пользователя
            User newUser = new User(username, password, secretQuestion, secretAnswer);
            try {
                db.userDao().insert(newUser);
                Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                ((LoginActivity) requireActivity()).navigateToEnter();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Ошибка регистрации: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String username, String password, String secretAnswer) {
        if (username.isEmpty() || password.isEmpty() || secretAnswer.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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


}