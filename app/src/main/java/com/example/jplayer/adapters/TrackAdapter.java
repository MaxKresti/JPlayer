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
            holder.trackAuthor.setText(track.artist != null ? track.artist : "Unknown Artist");
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

        // Обработчик клика по элементу списка для воспроизведения трека
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).playTrack(track);
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
                        // Реализуйте логику добавления в плейлист
                    }
                    @Override
                    public void onRename(int pos) {
                        // Здесь вызываем метод переименования, передавая текущий трек и позицию.
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).showRenameDialog(trackList.get(pos), pos);
                        }
                    }
                    @Override
                    public void onDelete(int position) {
                        Song songToDelete = trackList.get(position);

                        // Удаление из базы
                        AppDatabase.getInstance(context).songDao().delete(songToDelete);

                        // Остановка плеера, если удаляемая песня играет
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).stopIfPlaying(songToDelete);
                        }

                        // Удаление из списка адаптера
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
            // Обратите внимание на id – они должны совпадать с теми, что заданы в layoutResId
            trackImage = itemView.findViewById(R.id.trackImage);
            trackName = itemView.findViewById(R.id.trackName);
            trackAuthor = itemView.findViewById(R.id.trackAuthor); // Если в макете нет trackAuthor, то это будет null
            moreOptions = itemView.findViewById(R.id.trackMenu);     // Если макет не содержит trackMenu, то это будет null
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
