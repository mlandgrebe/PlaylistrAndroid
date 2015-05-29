package com.attu.attu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.*;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.*;

import retrofit.client.*;
import retrofit.Callback;
import retrofit.RetrofitError;

public class UpdatePlaylistsThread extends Observable implements Runnable {
    public Activity toUpdate;
    public SpotifyService spotify;
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