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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Playlist playlist);

    @Query("SELECT * FROM playlists WHERE user_id = :userId")
    List<Playlist> getPlaylistsByUser(int userId);

    @Query("SELECT * FROM playlists WHERE user_id = :userId")
    List<Playlist> getPlaylistsByUserId(int userId);

    @Query("SELECT * FROM playlists WHERE user_id = :userId")
    LiveData<List<Playlist>> getPlaylistsByUserLive(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylist(Playlist playlist);

    @Delete
    void deletePlaylist(Playlist playlist);

    @Update
    void updatePlaylist(Playlist playlist);
}
