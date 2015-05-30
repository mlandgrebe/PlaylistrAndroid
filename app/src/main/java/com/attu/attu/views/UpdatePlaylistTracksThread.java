package com.attu.attu.views;

import android.app.Activity;
import android.util.Log;

import java.util.List;
import java.util.Observable;

import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.*;

import retrofit.RetrofitError;

public class UpdatePlaylistTracksThread extends Observable implements Runnable{
    public Activity toUpdate;
    public SpotifyService spotify;
    public List<PlaylistTrack> ts;
    //public Playlist plist;
    public String uri;
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
            ts = null;
            try {
                Pager<PlaylistTrack> pt = spotify.getPlaylistTracks(user.id, uri);
                ts = pt.items;
            } catch (RetrofitError error) {
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                Log.d("sync error", spotifyError.getMessage());
                // handle error
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
