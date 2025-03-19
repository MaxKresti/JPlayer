package com.example.jplayer.database.user;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "username")
    public      String  username;

    @ColumnInfo(name = "password")
    public      String  password;

    @ColumnInfo(name = "secret_question")
    public      String  secretQuestion;

    @ColumnInfo(name = "secret_answer")
    public      String  secretAnswer;

    public User(String username, String password, String secretQuestion, String secretAnswer) {
        this.username = username;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
    }
}