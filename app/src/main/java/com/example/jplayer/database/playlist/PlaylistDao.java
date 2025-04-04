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

    // Вставка плейлиста, если такого ещё нет
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Playlist playlist);

    // Вставка с заменой, если уже есть плейлист с таким id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Playlist playlist);

    // Получение списка плейлистов текущего пользователя в LiveData для автоматического обновления
    @Query("SELECT * FROM playlists WHERE user_id = :userId")
    LiveData<List<Playlist>> getLivePlaylistsByUserId(int userId);

    // Удаление
    @Delete
    void deletePlaylist(Playlist playlist);

    // Обновление
    @Update
    void updatePlaylist(Playlist playlist);


    @Query("SELECT * FROM playlists WHERE user_id = :userId")
    LiveData<List<Playlist>> getPlaylistsByUserLive(int userId); // Этот метод должен возвращать LiveData
}
