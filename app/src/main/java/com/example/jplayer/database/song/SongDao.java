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

    // Вставка нового трека
    @Insert
    void insert(Song song);

    // Обновление трека
    @Update
    void update(Song song);

    // Удаление трека
    @Delete
    void delete(Song song);

    // Получить все треки пользователя
    @Query("SELECT * FROM songs WHERE user_id = :userId")
    List<Song> getSongsByUser(int userId);

    // Проверка, существует ли трек
    @Query("SELECT COUNT(*) FROM songs WHERE file_path = :filePath AND user_id = :userId")
    int checkSongExists(String filePath, int userId);

    // Последние 10 добавленных треков (не LiveData)
    @Query("SELECT * FROM songs WHERE user_id = :userId ORDER BY song_id DESC LIMIT 10")
    List<Song> getRecentSongs(int userId);

    // Последние 10 добавленных треков (LiveData)
    @Query("SELECT * FROM songs WHERE user_id = :userId ORDER BY song_id DESC LIMIT 10")
    LiveData<List<Song>> getRecentSongsLive(int userId);

    // Получить треки из конкретного плейлиста (LiveData)
    @Query("SELECT songs.* FROM songs " +
            "JOIN playlist_song ON songs.song_id = playlist_song.song_id " +
            "WHERE playlist_song.playlist_id = :playlistId")
    LiveData<List<Song>> getSongsByPlaylistLive(int playlistId);

    // Получить треки из конкретного плейлиста (не LiveData)
    @Query("SELECT songs.* FROM songs " +
            "JOIN playlist_song ON songs.song_id = playlist_song.song_id " +
            "WHERE playlist_song.playlist_id = :playlistId")
    List<Song> getSongsByPlaylist(int playlistId);

    // Удалить трек из плейлиста
    @Query("DELETE FROM playlist_song WHERE playlist_id = :playlistId AND song_id = :songId")
    void removeSongFromPlaylist(int playlistId, int songId);

    // Получить избранные треки пользователя
    @Query("SELECT * FROM songs WHERE user_id = :userId AND is_favorite = 1")
    LiveData<List<Song>> getFavoriteSongsLive(int userId);

    // Обновить статус избранного трека (лайк/дизлайк)
    @Query("UPDATE songs SET is_favorite = :isFavorite WHERE song_id = :songId")
    void updateFavoriteStatus(int songId, boolean isFavorite);

}
