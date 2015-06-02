package com.attu.attu.views;

import android.app.Activity;
import android.util.Log;

import com.attu.util.State;

import java.util.List;
import java.util.Observable;

import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.*;

import retrofit.RetrofitError;

public class QueueSongThread extends Observable implements Runnable{
    public Activity toUpdate;
    public List<PlaylistTrack> ts;
    public String uri;

    public void run(){
        State state = State.getState();
        if(state != null) {
            ts = null;
            try {
                SpotifyService spotify = state.getSpotifyService();
                User user = spotify.getMe();
                Pager<PlaylistTrack> pt = spotify.getPlaylistTracks(user.id, uri);
                ts = pt.items;
            } catch (RetrofitError error) {
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                Log.d("sync error", spotifyError.getMessage());
            }
            if(ts != null) {
                setChanged();
                notifyObservers();
                for (PlaylistTrack plTrack : ts) {
                    Log.d("Success", plTrack.track.name);
                }
            }
        }
    }
}
