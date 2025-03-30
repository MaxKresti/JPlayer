package com.example.jplayer.ui.media.playlist;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;
import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private final LiveData<List<Playlist>> playlistsLiveData;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        // Получаем LiveData для плейлистов текущего пользователя (например, userId = 1)
        playlistsLiveData = AppDatabase.getInstance(application).playlistDao().getPlaylistsByUserLive(1);
    }

    public LiveData<List<Playlist>> getPlaylistsLiveData() {
        return playlistsLiveData;
    }
}
