package com.example.jplayer.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jplayer.R;
import com.example.jplayer.ui.enter.EnterFragment;
import com.example.jplayer.ui.Registration.RegistrationFragment;
import com.example.jplayer.ui.enter.ForgotPasswordFragment;

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
}