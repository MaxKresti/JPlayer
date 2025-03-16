package com.example.jplayer.ui.track;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jplayer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TrackMenuSheetDialogFragment extends BottomSheetDialogFragment {

    private OnMenuItemClickListener listener;
    private int position;

    public interface OnMenuItemClickListener {
        void onAddToPlaylist(int position);
        void onRename(int position);
        void onDelete(int position);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.context_menu_track, container, false);

        LinearLayout addToPlaylist = view.findViewById(R.id.addToPlaylist);
        LinearLayout rename = view.findViewById(R.id.rename);
        LinearLayout delete = view.findViewById(R.id.delete);

        addToPlaylist.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToPlaylist(position);
            }
            dismiss();
        });

        rename.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRename(position);
            }
            dismiss();
        });

        delete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(position);
            }
            dismiss();
        });

        return view;


    }


}