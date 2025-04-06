package com.example.jplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jplayer.MainActivity;
import com.example.jplayer.R;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.ui.media.track.TrackMenuSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private final Context context;
    private final List<Song> trackList;
    private final int layoutResId; // Позволяет передавать нужный макет

    public TrackAdapter(Context context, List<Song> trackList, int layoutResId) {
        this.context = context;
        this.trackList = new ArrayList<>(trackList);
        this.layoutResId = layoutResId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song track = trackList.get(position);

        holder.trackName.setText(track.title);

        if (holder.trackAuthor != null) {
            holder.trackAuthor.setText(track.artist != null ? track.artist : "Неизвестный исполнитель");
        }

        if (track.coverArt != null && !track.coverArt.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(track.coverArt);
            if (bitmap != null) {
                holder.trackImage.setImageBitmap(bitmap);
            } else {
                holder.trackImage.setImageResource(R.drawable.image);
            }
        } else {
            holder.trackImage.setImageResource(R.drawable.image);
        }

        // Обработчик клика по элементу списка для воспроизведения трека из всего списка
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                // Передаем весь список треков и позицию выбранного трека
                ((MainActivity) context).playTrackFromPlaylist(trackList, position);
            }
        });

        // Если в макете присутствует кнопка "три точки", назначаем обработчик клика
        if (holder.moreOptions != null) {
            holder.moreOptions.setOnClickListener(v -> {
                TrackMenuSheetDialogFragment dialog = new TrackMenuSheetDialogFragment();
                dialog.setPosition(position);
                dialog.setOnMenuItemClickListener(new TrackMenuSheetDialogFragment.OnMenuItemClickListener() {
                    @Override
                    public void onAddToPlaylist(int pos) {
                        Song track = trackList.get(pos);
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).showAddToPlaylistDialog(track);
                        }
                    }
                    @Override
                    public void onRename(int pos) {
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).showRenameDialog(trackList.get(pos), pos);
                        }
                    }
                    @Override
                    public void onDelete(int position) {
                        Song songToDelete = trackList.get(position);
                        AppDatabase.getInstance(context).songDao().delete(songToDelete);
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).stopIfPlaying(songToDelete);
                        }
                        removeTrack(position);
                    }
                });
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "track_menu");
            });
        }
    }

    public void removeTrack(int position) {
        if (position >= 0 && position < trackList.size()) {
            trackList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    /**
     * Обновляет список треков с использованием DiffUtil для анимированного обновления.
     */
    public void updateTracks(List<Song> newTracks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TrackDiffCallback(trackList, newTracks));
        trackList.clear();
        trackList.addAll(newTracks);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView trackImage;
        TextView trackName, trackAuthor;
        ImageView moreOptions;  // Может быть null, если макет не содержит эту кнопку

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Убедитесь, что ID совпадают с теми, что заданы в макете
            trackImage = itemView.findViewById(R.id.trackImage);
            trackName = itemView.findViewById(R.id.trackName);
            trackAuthor = itemView.findViewById(R.id.trackAuthor);
            moreOptions = itemView.findViewById(R.id.trackMenu);
        }
    }

    static class TrackDiffCallback extends DiffUtil.Callback {
        private final List<Song> oldList;
        private final List<Song> newList;

        public TrackDiffCallback(List<Song> oldList, List<Song> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

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
