package com.example.jplayer.ui.media.playlist;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.playlist.Playlist;
import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    // LiveData, которая будет содержать список плейлистов
    private LiveData<List<Playlist>> playlistsLiveData;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Playlist>> getPlaylistsLiveData() {
        return playlistsLiveData;
    }

    // Загружаем плейлисты для указанного userId; метод возвращает LiveData из DAO
    public void loadPlaylists(Context context, int userId) {
        playlistsLiveData = AppDatabase.getInstance(context)
                .playlistDao()
                .getLivePlaylistsByUserId(userId);
    }
}
