package com.example.jplayer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.jplayer.databinding.ActivityMainBinding;
import com.example.jplayer.ui.MiniPlayerFragment;
import com.example.jplayer.ui.FullPlayerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_media,
                R.id.navigation_add_new
        ).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * Показывает мини-плеер.
     */
    public void showMiniPlayer() {
        MiniPlayerFragment miniPlayerFragment = new MiniPlayerFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.miniPlayerContainer, miniPlayerFragment)
                .commit();
    }

    /**
     * Скрывает мини-плеер.
     */
    public void hideMiniPlayer() {
        Fragment miniPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.miniPlayerContainer);
        if (miniPlayerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(miniPlayerFragment)
                    .commit();
        }
    }


    public void showFullPlayer() {

        FullPlayerFragment fullPlayerFragment = new FullPlayerFragment();

        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        if (fullPlayerContainer != null) {
            fullPlayerContainer.setVisibility(View.VISIBLE);
            fullPlayerContainer.setTranslationY(fullPlayerContainer.getHeight());
            fullPlayerContainer.setAlpha(0.0f);

            // Анимация для появления плеера
            fullPlayerContainer.animate()
                    .translationY(0) 
                    .alpha(1.0f)
                    .setDuration(300)
                    .start();
        }

        // Скрываем BottomNavigationView
        setBottomNavigationVisibility(false);

        // Отображаем FullPlayerFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fullPlayerContainer, fullPlayerFragment)
                .addToBackStack(null) // Добавляем в back stack для возможности возврата
                .commit();
    }

    /**
     * Скрывает FullPlayerFragment.
     */
    public void hideFullPlayer() {
        // Удаляем FullPlayerFragment
        Fragment fullPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.fullPlayerContainer);
        if (fullPlayerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fullPlayerFragment)
                    .commit();
        }

        // Анимация для закрытия плеера
        View fullPlayerContainer = findViewById(R.id.fullPlayerContainer);
        if (fullPlayerContainer != null) {
            fullPlayerContainer.animate()
                    .translationY(fullPlayerContainer.getHeight()) // Перемещаем вниз
                    .alpha(0.0f) // Уменьшаем прозрачность
                    .setDuration(300) // Длительность анимации
                    .withEndAction(() -> {
                        fullPlayerContainer.setVisibility(View.GONE); // Скрываем контейнер
                    })
                    .start();
        }

        // Показываем BottomNavigationView
        setBottomNavigationVisibility(true);
    }

    /**
     * Управляет видимостью BottomNavigationView.
     *
     * @param isVisible true - показать, false - скрыть
     */
    public void setBottomNavigationVisibility(boolean isVisible) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (navView != null) {
            navView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}