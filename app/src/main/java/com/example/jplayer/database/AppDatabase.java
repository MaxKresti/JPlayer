package com.example.jplayer.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.jplayer.database.playlist.Playlist;
import com.example.jplayer.database.playlist.PlaylistDao;
import com.example.jplayer.database.playlistSong.PlaylistSong;
import com.example.jplayer.database.playlistSong.PlaylistSongDao;
import com.example.jplayer.database.song.Song;
import com.example.jplayer.database.song.SongDao;
import com.example.jplayer.database.user.User;
import com.example.jplayer.database.user.UserDao;

@Database(entities = {User.class, Song.class, Playlist.class, PlaylistSong.class}, version = 8)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "Users.db";
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract SongDao songDao();
    public abstract PlaylistDao playlistDao();
    public abstract PlaylistSongDao playlistSongDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // НЕ рекомендуется для продакшена
                    .build();
        }
        return instance;
    }
}
