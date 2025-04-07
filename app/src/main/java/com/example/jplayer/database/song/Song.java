package com.example.jplayer.database.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.jplayer.database.user.User;

@Entity(tableName = "songs",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "_id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("user_id")})
public class Song {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_id")
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "artist")
    public String artist;

    @ColumnInfo(name = "album")
    public String album;

    @ColumnInfo(name = "duration")
    public long duration;

    @ColumnInfo(name = "file_path")
    public String filePath;

    @ColumnInfo(name = "cover_art")
    public String coverArt;

    @ColumnInfo(name = "is_favorite")
    public boolean isFavorite;


    // Конструктор
    public Song(int userId, String title, String artist, String album,
                long duration, String filePath, String coverArt, boolean isFavorite) {
        this.userId = userId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.filePath = filePath;
        this.coverArt = coverArt;
        this.isFavorite = isFavorite;
    }
}