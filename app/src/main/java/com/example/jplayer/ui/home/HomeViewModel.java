package com.example.jplayer.ui.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.jplayer.database.AppDatabase;
import com.example.jplayer.database.song.Song;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final AppDatabase db;

    // LiveData для секций
    private LiveData<List<Song>> recentAddedSongs;
    private LiveData<List<Song>> recentPlayedSongs;
    private LiveData<List<Song>> recommendedSongs;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application.getApplicationContext());
    }

    // Последние добавленные треки (запрос: последние 10, отсортированные по song_id DESC)
    public LiveData<List<Song>> getRecentAddedSongs(int userId) {
        if (recentAddedSongs == null) {
            recentAddedSongs = db.songDao().getRecentSongsLive(userId);
        }
        return recentAddedSongs;
    }

    // Последние прослушанные треки (здесь можно использовать тот же запрос, если у вас нет отдельного столбца;
    // в противном случае надо создать запрос, сортирующий по времени последнего прослушивания)
    public LiveData<List<Song>> getRecentPlayedSongs(int userId) {
        if (recentPlayedSongs == null) {
            // Здесь для примера используем тот же запрос, что и для последних добавленных
            recentPlayedSongs = db.songDao().getRecentSongsLive(userId);
        }
        return recentPlayedSongs;
    }

    // Рекомендации (рандомные треки)
    public LiveData<List<Song>> getRecommendedSongs(int userId) {
        if (recommendedSongs == null) {
            recommendedSongs = db.songDao().getRecommendedSongsLive(userId);
        }
        return recommendedSongs;
    }
}
