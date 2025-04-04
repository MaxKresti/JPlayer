package com.example.jplayer.ui.media.playlist;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;
import java.util.List;

public class PlaylistViewModel extends ViewModel {
    private final MutableLiveData<List<Playlist>> playlistsLiveData = new MutableLiveData<>();

    public LiveData<List<Playlist>> getPlaylistsLiveData() {
        return playlistsLiveData;
    }

    public void loadPlaylists(Context context, int userId) {
        new Thread(() -> {
            List<Playlist> playlists = AppDatabase.getInstance(context)
                    .playlistDao()
                    .getPlaylistsByUserId(userId);
            playlistsLiveData.postValue(playlists);
        }).start();
    }
}
