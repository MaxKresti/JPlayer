package com.example.jplayer.ui.media.playlist;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private MutableLiveData<List<Playlist>> playlistsLiveData = new MutableLiveData<>();

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadPlaylists(Context context, int userId) {
        AppDatabase.getInstance(context)
                .playlistDao()
                .getPlaylistsByUserLive(userId)
                .observeForever(playlists -> {
                    playlistsLiveData.postValue(playlists);
                });
    }

    public LiveData<List<Playlist>> getPlaylistsLiveData() {
        return playlistsLiveData;
    }
}