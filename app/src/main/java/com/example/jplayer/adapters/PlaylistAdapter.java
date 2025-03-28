package com.example.jplayer.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.jplayer.R;
import com.example.jplayer.database.playlist.Playlist;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<Playlist> playlists = new ArrayList<>();
    private final Context context;
    private final OnPlaylistClickListener listener;

    public interface OnPlaylistClickListener {
        void onPlaylistClick(int playlistId);
        void onPlaylistMenuClick(View view, int position);
    }

    public PlaylistAdapter(Context context, OnPlaylistClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateData(List<Playlist> newPlaylists) {
        this.playlists = newPlaylists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        if (holder.playlistName != null) {
            holder.playlistName.setText(playlist.name);
        } else {
            Log.e("PlaylistAdapter", "playlistName is null!");
        }

        // Используем Glide для загрузки изображения
        if (playlist.coverImage != null && !playlist.coverImage.isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(playlist.coverImage))
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image)
                    .into(holder.coverImage);
        } else {
            holder.coverImage.setImageResource(R.drawable.image);
        }

        Log.d("PlaylistAdapter", "Binding playlist: " + playlist.name);

        holder.coverImage.setOnClickListener(v -> listener.onPlaylistClick(playlist.id));
        holder.menuButton.setOnClickListener(v -> listener.onPlaylistMenuClick(v, position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistName;
        ImageView coverImage, menuButton;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            // Убедитесь, что в item_playlist.xml установлены следующие ID:
            playlistName = itemView.findViewById(R.id.playlistName);
            coverImage = itemView.findViewById(R.id.playlistImage);
            menuButton = itemView.findViewById(R.id.playlistMenu);
        }
    }
}
