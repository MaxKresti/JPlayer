package com.example.jplayer.ui.media.add_new;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jplayer.databinding.FragmentAddNewBinding;

public class NotificationsFragment extends Fragment {

    private FragmentAddNewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddNewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Убедитесь, что текст не переопределяется ViewModel
        // final TextView textView = binding.text1;
        // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}