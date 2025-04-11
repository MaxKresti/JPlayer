package com.example.jplayer.ui.media.player;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.jplayer.database.song.Song;

public class PlayerViewModel extends AndroidViewModel {
    private final MutableLiveData<Song> currentSongLiveData = new MutableLiveData<>();

    public PlayerViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Song> getCurrentSong() {
        return currentSongLiveData;
    }

    public void setCurrentSong(Song song) {
        currentSongLiveData.setValue(song);
    }
}
