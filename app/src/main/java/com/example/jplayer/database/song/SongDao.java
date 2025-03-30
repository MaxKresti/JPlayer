package com.example.jplayer.database.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SongDao {
    @Insert
    void insert(Song song);

    @Query("SELECT * FROM songs WHERE user_id = :userId")
    List<Song> getSongsByUser(int userId);

    @Query("SELECT COUNT(*) FROM songs WHERE file_path = :filePath AND user_id = :userId")
    int checkSongExists(String filePath, int userId);



    @Query("SELECT * FROM songs WHERE user_id = :userId ORDER BY song_id DESC LIMIT 10")
    List<Song> getRecentSongs(int userId);

    @Query("SELECT * FROM songs WHERE user_id = :userId ORDER BY song_id DESC LIMIT 10")
    LiveData<List<Song>> getRecentSongsLive(int userId);

    @Delete
    void delete(Song song);

    @Update
    void update(Song song);

}