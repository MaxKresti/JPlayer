package com.example.jplayer.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jplayer.R;
import com.example.jplayer.ui.enter.EnterFragment;
import com.example.jplayer.ui.Registration.RegistrationFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // По умолчанию отображаем EnterFragment
        if (savedInstanceState == null) {
            loadFragment(new EnterFragment(), false); // false, чтобы не добавлять в back stack
        }
    }

    /**
     * Метод для загрузки фрагмента в контейнер.
     *
     * @param fragment       Фрагмент, который нужно загрузить.
     * @param addToBackStack Добавлять ли транзакцию в back stack.
     */
    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Заменяем текущий фрагмент на новый
        transaction.replace(R.id.fragment_container, fragment);

        // Добавляем транзакцию в back stack (если нужно)
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        // Применяем изменения
        transaction.commit();
    }

    /**
     * Метод для перехода на фрагмент регистрации.
     */
    public void navigateToRegistration() {
        loadFragment(new RegistrationFragment(), true); // true, чтобы добавить в back stack
    }

    /**
     * Метод для возврата на фрагмент входа.
     */
    public void navigateToEnter() {
        loadFragment(new EnterFragment(), true); // true, чтобы добавить в back stack
    }
}