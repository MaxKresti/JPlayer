package com.example.jplayer.database.playlist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlists")
public class Playlist {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "cover_image")
    public String coverImage;

    public Playlist(int userId, String name, String coverImage) {
        this.userId = userId;
        this.name = name;
        this.coverImage = coverImage;
    }
}
