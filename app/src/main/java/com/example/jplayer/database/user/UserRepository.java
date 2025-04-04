package com.example.jplayer.database.user;

import android.content.Context;
import android.os.AsyncTask;

import com.example.jplayer.database.AppDatabase;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
    }

    public void getUserById(int userId, UserCallback callback) {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                return userDao.getUserById(userId);
            }

            @Override
            protected void onPostExecute(User user) {
                callback.onUserLoaded(user);
            }
        }.execute();
    }

    public interface UserCallback {
        void onUserLoaded(User user);
    }
}
