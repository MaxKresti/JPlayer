package com.example.jplayer.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.user.User;
import com.example.jplayer.databinding.FragmentSettingBinding;
import com.example.jplayer.ui.login.LoginActivity;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Предполагается, что вы используете ViewBinding; если нет, можете использовать inflate() с findViewById
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Обработчики для кнопок настройки
        binding.changeUsernameButton.setOnClickListener(v -> showUpdateUsernameDialog());
        binding.changePasswordButton.setOnClickListener(v -> showUpdatePasswordDialog());
        binding.logoutButton.setOnClickListener(v -> logout());

        // Обработка кнопки "Назад" системы
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                hideSetting();
                return true;
            }
            return false;
        });

        // Обработчик для кнопки "Назад" в UI фрагмента
        binding.back.setOnClickListener(v -> hideSetting());
    }

    /**
     * Диалог для обновления имени пользователя.
     */
    private void showUpdateUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Изменить имя пользователя");

        final EditText input = new EditText(getContext());
        input.setHint("Новое имя пользователя");
        builder.setView(input);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String newUsername = input.getText().toString().trim();
            if (newUsername.isEmpty()) {
                Toast.makeText(getContext(), "Имя не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            int userId = getCurrentUserId();
            if (userId == -1) {
                Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                User user = AppDatabase.getInstance(getContext()).userDao().getUserById(userId);
                if (user != null) {
                    user.username = newUsername;
                    int rows = AppDatabase.getInstance(getContext()).userDao().update(user);
                    requireActivity().runOnUiThread(() -> {
                        if (rows > 0) {
                            Toast.makeText(getContext(), "Имя пользователя обновлено", Toast.LENGTH_SHORT).show();
                            // Обновляем UI в MainActivity (например, приветствие)

                        } else {
                            Toast.makeText(getContext(), "Ошибка обновления имени", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Диалог для обновления пароля.
     */
    private void showUpdatePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Изменить пароль");

        final EditText input = new EditText(getContext());
        input.setHint("Новый пароль");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String newPassword = input.getText().toString().trim();
            if (newPassword.isEmpty()) {
                Toast.makeText(getContext(), "Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            int userId = getCurrentUserId();
            if (userId == -1) {
                Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                User user = AppDatabase.getInstance(getContext()).userDao().getUserById(userId);
                if (user != null) {
                    user.password = newPassword;
                    int rows = AppDatabase.getInstance(getContext()).userDao().update(user);
                    requireActivity().runOnUiThread(() -> {
                        if (rows > 0) {
                            Toast.makeText(getContext(), "Пароль обновлён", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Ошибка обновления пароля", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Очищает данные пользователя и выполняет переход на экран логина.
     */
    private void logout() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    /**
     * Скрывает фрагмент настроек, вызывая метод из MainActivity.
     */
    private void hideSetting() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideSetting();
        }
    }

    /**
     * Получает текущий user_id из SharedPreferences.
     */
    private int getCurrentUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getInt("user_id", -1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
