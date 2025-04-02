package com.example.jplayer.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jplayer.R;
import com.example.jplayer.database.user.User;
import com.example.jplayer.ui.login.enter.EnterFragment;
import com.example.jplayer.ui.login.Registration.RegistrationFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            loadFragment(new EnterFragment(), false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void navigateToRegistration() {
        loadFragment(new RegistrationFragment(), true);
    }

    public void navigateToEnter() {
        getSupportFragmentManager().popBackStack();
    }

    // Метод для сохранения ID пользователя после успешного входа
    public void saveUserId(User user) {
        if (user != null) {
            SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("user_id", user.id);  // Сохраняем ID пользователя
            editor.apply();
        }
    }
}
