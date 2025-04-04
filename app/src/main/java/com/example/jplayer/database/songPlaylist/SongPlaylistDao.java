package com.example.jplayer.database.songPlaylist;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface SongPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSongPlaylist(SongPlaylistCrossRef crossRef);
}
