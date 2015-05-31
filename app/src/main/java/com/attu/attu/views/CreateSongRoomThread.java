package com.attu.attu.views;

import android.app.Activity;
import android.util.Log;

import java.util.List;
import java.util.Observable;

import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.User;
import retrofit.RetrofitError;

import com.attu.models.*;
import com.attu.remote.*;

/**
 * Created by marklandgrebe on 5/30/15.
 */

public class CreateSongRoomThread extends Observable implements Runnable {
    public Activity toUpdate;
    public SpotifyService spotify;
    public Server server;
    public List<PlaylistSimple> l;
    public void run(){
        User user = null;
        try {
            user = spotify.getMe();
            Log.d("Success", user.id);
        } catch (RetrofitError error) {
            SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
            Log.d("sync error", spotifyError.getMessage());
            // handle error
        }
        if(user != null) {
            Log.d("Successful login", user.id);

            l = null;
            try {
                Pager<PlaylistSimple> pp = spotify.getPlaylists(user.id);
                l = pp.items;
            } catch (RetrofitError error) {
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                Log.d("sync error", spotifyError.getMessage());
                // handle error
            }
            if(l != null) {
                setChanged();
                notifyObservers();
                for (PlaylistSimple play : l) {
                    Log.d("Success", play.name);
                }
            }
        }
    }
}
