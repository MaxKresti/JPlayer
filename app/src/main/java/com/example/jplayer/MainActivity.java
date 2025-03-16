package com.example.jplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jplayer.databinding.ActivityMainBinding;
import com.example.jplayer.ui.MiniPlayerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Настройка конфигурации для AppBar (ActionBar)
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, // ID для HomeFragment
                R.id.navigation_media, // ID для DashboardFragment (Media)
                R.id.navigation_add_new // ID для AddNewFragment
        ).build();

        // Настройка NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Связывание ActionBar с NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Связывание BottomNavigationView с NavController
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * Показывает мини-плеер.
     */
    public void showMiniPlayer() {
        // Создаем экземпляр MiniPlayerFragment
        MiniPlayerFragment miniPlayerFragment = new MiniPlayerFragment();

        // Отображение мини-плеера
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.miniPlayerContainer, miniPlayerFragment)
                .commit();
    }

    /**
     * Скрывает мини-плеер.
     */
    public void hideMiniPlayer() {
        // Удаляем мини-плеер
        Fragment miniPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.miniPlayerContainer);
        if (miniPlayerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(miniPlayerFragment)
                    .commit();
        }
    }
}