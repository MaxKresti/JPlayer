package com.example.jplayer.ui.media;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jplayer.ui.album.AlbumFragment;
import com.example.jplayer.ui.media.playlist.PlaylistFragment;
import com.example.jplayer.ui.media.track.TrackFragment;

public class MediaPagerAdapter extends FragmentStateAdapter {

    public MediaPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TrackFragment(); // Первая вкладка — TrackFragment
            case 1:
                return new AlbumFragment(); // Вторая вкладка — AlbumFragment
            case 2:
                return new PlaylistFragment(); // Третья вкладка — PlaylistFragment
            default:
                return new TrackFragment(); // По умолчанию возвращаем TrackFragment
        }
    }

    @Override
    public int getItemCount() {
        return 3; // У нас три вкладки: Track, Album, Playlist
    }
}