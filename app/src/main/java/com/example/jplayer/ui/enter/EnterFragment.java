package com.example.jplayer.ui.enter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jplayer.database.DatabaseHelper;
import com.example.jplayer.databinding.FragmentEnterBinding;
import com.example.jplayer.ui.LoginActivity;
import com.example.jplayer.MainActivity;

public class EnterFragment extends Fragment {

    private FragmentEnterBinding binding; //

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEnterBinding.inflate(inflater, container, false);

        binding.loginButton.setOnClickListener(v -> processLogin());
        binding.registerButton.setOnClickListener(v -> ((LoginActivity) requireActivity()).navigateToRegistration());
        binding.authorName.setOnClickListener(v -> ((LoginActivity) requireActivity()).loadFragment(new com.example.jplayer.ui.enter.ForgotPasswordFragment(), true));

        return binding.getRoot();
    }

    private void processLogin() {
        String username = binding.nameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        if (validateInput(username, password)) {
            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            if (dbHelper.checkCredentials(username, password)) {
                startActivity(new Intent(requireActivity(), MainActivity.class));
                requireActivity().finish();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}