package com.example.jplayer.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.user.User;
import com.example.jplayer.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);

        binding.btnRecover.setOnClickListener(v -> processPasswordRecovery());
        binding.btnBack.setOnClickListener(v -> ((LoginActivity) requireActivity()).navigateToEnter());

        return binding.getRoot();
    }

    private void processPasswordRecovery() {
        String username = binding.etUsername.getText().toString().trim();
        String newPassword = binding.etNewPassword.getText().toString().trim();
        String answer = binding.etSecretAnswer.getText().toString().trim();

        if (validateInput(username, newPassword, answer)) {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            User user = db.userDao().validateSecretAnswer(username, answer);

            if (user != null) {
                user.password = newPassword;
                int result = db.userDao().update(user);

                if (result > 0) {
                    Toast.makeText(requireContext(), "Пароль успешно изменен!", Toast.LENGTH_SHORT).show();
                    ((LoginActivity) requireActivity()).navigateToEnter();
                } else {
                    Toast.makeText(requireContext(), "Ошибка изменения пароля!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Неверный ответ или пользователь!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String username, String password, String answer) {
        if (username.isEmpty() || password.isEmpty() || answer.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}