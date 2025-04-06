package com.example.jplayer.ui.media.opened;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import java.util.List;

public class PlaylistAlbumViewModel extends AndroidViewModel {
    private final AppDatabase db;

    public PlaylistAlbumViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Song>> getSongsForPlaylist(int playlistId) {
        return db.songDao().getSongsByPlaylistLive(playlistId);
    }
}
