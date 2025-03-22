package com.example.jplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.database.song.Song;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private final Context context;
    public final List<Song> tracks;

    public TrackAdapter(Context context, List<Song> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.main_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Song track = tracks.get(position);

        // Загрузка обложки
        if (track.coverArt != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(track.coverArt);
            if (bitmap != null) {
                holder.trackImage.setImageBitmap(bitmap);
            }
        }

        holder.trackName.setText(track.title);

        // Обработчик клика
        holder.itemView.setOnClickListener(v -> {
            // Предполагается, что Activity является MainActivity
            if (context instanceof MainActivity) {
                ((MainActivity) context).playTrack(track.filePath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {
        ImageView trackImage;
        TextView trackName;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackImage = itemView.findViewById(R.id.trackImage3);
            trackName = itemView.findViewById(R.id.trackName3);
        }
    }

    public void updateTracks(List<Song> newTracks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TrackDiffCallback(tracks, newTracks));
        tracks.clear();
        tracks.addAll(newTracks);
        diffResult.dispatchUpdatesTo(this);
    }

    static class TrackDiffCallback extends DiffUtil.Callback {
        private final List<Song> oldList;
        private final List<Song> newList;

        public TrackDiffCallback(List<Song> oldList, List<Song> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() { return oldList.size(); }

        @Override
        public int getNewListSize() { return newList.size(); }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }   
}