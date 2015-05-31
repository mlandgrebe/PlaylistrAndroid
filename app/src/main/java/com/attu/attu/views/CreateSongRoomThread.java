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

import com.attu.remote.*;
import com.attu.util.State;

/**
 * Created by marklandgrebe on 5/30/15.
 */

public class CreateSongRoomThread extends Observable implements Runnable {
    public Activity toUpdate;
    public List<PlaylistSimple> l;

    public void run(){
        State state = State.getState();
        if(state != null) {
            l = null;
            try {
                SpotifyService spotify = state.getSpotifyService();
                User user = spotify.getMe();
                Pager<PlaylistSimple> pp = spotify.getPlaylists(user.id);
                l = pp.items;
            } catch (RetrofitError error) {
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                Log.d("sync error", spotifyError.getMessage());
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
