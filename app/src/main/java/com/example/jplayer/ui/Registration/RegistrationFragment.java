package com.example.jplayer.ui.Registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jplayer.database.DatabaseHelper;
import com.example.jplayer.databinding.FragmentRegistrationBinding;
import com.example.jplayer.ui.LoginActivity;

public class RegistrationFragment extends Fragment {
    private FragmentRegistrationBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);

        binding.back.setOnClickListener(v -> ((LoginActivity) requireActivity()).navigateToEnter());

        binding.registerButton.setOnClickListener(v -> processRegistration());

        return binding.getRoot();
    }

    private void processRegistration() {
        String username = binding.nameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        String secretAnswer = binding.secretAnswerInput.getText().toString().trim();

        if (validateInput(username, password, secretAnswer)) {
            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            if (dbHelper.registerUser(username, password, secretAnswer)) {
                Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                ((LoginActivity) requireActivity()).navigateToEnter();
            } else {
                Toast.makeText(requireContext(), "Пользователь уже существует!", Toast.LENGTH_SHORT).show();
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
}