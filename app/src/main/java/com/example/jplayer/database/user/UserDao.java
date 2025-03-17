package com.example.jplayer.database.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User getUser(String username, String password);

    @Query("SELECT secret_question FROM users WHERE username = :username")
    String getSecretQuestion(String username);

    @Query("SELECT * FROM users WHERE username = :username AND secret_answer = :answer")
    User validateSecretAnswer(String username, String answer);

    @Update
    int update(User user);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);
}