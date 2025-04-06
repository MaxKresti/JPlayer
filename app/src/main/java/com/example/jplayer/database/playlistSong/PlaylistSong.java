package com.example.jplayer.database.playlistSong;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "playlist_song", primaryKeys = {"playlist_id", "song_id"})
public class PlaylistSong {
    @ColumnInfo(name = "playlist_id")
    public int playlistId;

    @ColumnInfo(name = "song_id")
    public int songId;

    public PlaylistSong(int playlistId, int songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }
}
