package com.example.jplayer.ui.media.track;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.database.song.SongDao;

import java.util.List;

public class TrackViewModel extends AndroidViewModel {
    private final SongDao songDao;
    private LiveData<List<Song>> songList;

    public TrackViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        songDao = db.songDao();
    }

    public LiveData<List<Song>> getSongsByUser(int userId) {
        if (songList == null) {
            songList = songDao.getRecentSongsLive(userId);
        }
        return songList;
    }
}
