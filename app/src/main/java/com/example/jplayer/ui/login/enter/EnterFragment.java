package com.example.jplayer.ui.login.enter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.user.User;
import com.example.jplayer.databinding.FragmentEnterBinding;
import com.example.jplayer.ui.login.LoginActivity;
import com.example.jplayer.MainActivity;
import com.example.jplayer.ui.login.ForgotPasswordFragment;

public class EnterFragment extends Fragment {
    private FragmentEnterBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEnterBinding.inflate(inflater, container, false);
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        checkRememberedUser(); // Проверяем, входил ли пользователь ранее

        binding.loginButton.setOnClickListener(v -> processLogin());
        binding.registerButton.setOnClickListener(v -> ((LoginActivity) requireActivity()).navigateToRegistration());
        binding.authorName.setOnClickListener(v -> ((LoginActivity) requireActivity()).loadFragment(new ForgotPasswordFragment(), true));

        return binding.getRoot();
    }

    private void processLogin() {
        String username = binding.nameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        boolean rememberMe = binding.checkBox.isChecked(); // Получаем состояние чекбокса

        if (validateInput(username, password)) {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            User user = db.userDao().getUser(username, password);

            if (user != null) {
                if (rememberMe) {
                    saveUserSession(user.id, username);
                }
                goToMainActivity();
            } else {
                Toast.makeText(requireContext(), "Неверные учетные данные!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUserSession(int userId, String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("remember_me", true);
        editor.putInt("user_id", userId);
        editor.putString("username", username);
        editor.apply();
    }

    private void checkRememberedUser() {
        boolean isRemembered = sharedPreferences.getBoolean("remember_me", false);
        if (isRemembered) {
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        startActivity(new Intent(requireActivity(), MainActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
