package com.example.jplayer.database.playlist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface PlaylistDao {

    // Вставка плейлиста (игнорируя конфликт)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Playlist playlist);

    // Вставка с заменой, если уже есть плейлист с таким id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Playlist playlist);

    // Получение списка плейлистов текущего пользователя с использованием LiveData,
    // при этом плейлист с именем "Loved Tracks" будет всегда на первом месте
    @Query("SELECT * FROM playlists WHERE user_id = :userId ORDER BY CASE WHEN name = 'Loved Tracks' THEN 0 ELSE 1 END, name ASC")
    LiveData<List<Playlist>> getLivePlaylistsByUserId(int userId);

    // Получение плейлиста по имени и ID пользователя (например, для проверки существования плейлиста "Loved Tracks")
    @Query("SELECT * FROM playlists WHERE user_id = :userId AND name = :playlistName LIMIT 1")
    Playlist getPlaylistByUserIdAndName(int userId, String playlistName);

    @Query("SELECT * FROM playlists WHERE user_id = :userId ORDER BY CASE WHEN name = 'Loved Tracks' THEN 0 ELSE 1 END, name ASC")
    LiveData<List<Playlist>> getLivePlaylistsByUserLive(int userId);

    // Удаление плейлиста
    @Delete
    void deletePlaylist(Playlist playlist);

    // Обновление плейлиста
    @Update
    void updatePlaylist(Playlist playlist);

    @Query("SELECT * FROM playlists WHERE user_id = :userId ORDER BY CASE WHEN name = 'Loved Tracks' THEN 0 ELSE 1 END, name ASC")
    List<Playlist> getPlaylistsByUserId(int userId);  // <-- обычный List, без LiveData

}
