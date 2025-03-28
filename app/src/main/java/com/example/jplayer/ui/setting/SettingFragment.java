package com.example.jplayer.ui.setting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;

public class SettingFragment extends Fragment {


    private ImageView backButton;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Инициализация элементов
        backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(v -> hideSetting());




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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




}