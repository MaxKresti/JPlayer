package com.example.jplayer.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.ui.login.LoginActivity;

public class SettingFragment extends Fragment {

    private ImageView backButton;
    private TextView logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация UI-элементов
        backButton = view.findViewById(R.id.back);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Назначаем обработчики кликов
        if (backButton != null) {
            backButton.setOnClickListener(v -> hideSetting());
        }

        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> logout());
        } else {
            throw new RuntimeException("logoutButton не найден! Проверь R.id.logoutButton в fragment_setting.xml");
        }

        // Обработка кнопки "Назад"
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                hideSetting();
                return true;
            }
            return false;
        });
    }

    private void hideSetting() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideSetting();
        }
    }

    private void logout() {
        // Очищаем сохранённые данные пользователя
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Переход на экран входа
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Завершаем активность
        requireActivity().finish();
    }
}
