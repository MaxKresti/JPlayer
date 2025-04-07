package com.example.jplayer.database.playlistSong;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlaylistSongDao {

    // Добавить песню в плейлист
    @Insert
    void insert(PlaylistSong playlistSong);

    // Удалить песню из плейлиста
    @Delete
    void delete(PlaylistSong playlistSong);

    // Получить все song_id для указанного плейлиста
    @Query("SELECT song_id FROM playlist_song WHERE playlist_id = :playlistId")
    List<Integer> getSongIdsInPlaylist(int playlistId);

    // Проверить, есть ли песня в плейлисте
    @Query("SELECT COUNT(*) FROM playlist_song WHERE playlist_id = :playlistId AND song_id = :songId")
    int isSongInPlaylist(int playlistId, int songId);

    // Удалить все песни из плейлиста
    @Query("DELETE FROM playlist_song WHERE playlist_id = :playlistId")
    void deleteAllSongsFromPlaylist(int playlistId);
}
