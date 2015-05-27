package com.attu.attu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.util.List;
import java.util.Observer;
import java.util.Observable;

import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.*;

import retrofit.client.*;
import retrofit.RetrofitError;
import retrofit.Callback;
import android.widget.*;
import android.util.*;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback, Observer, Runnable {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "2de62f40903247208d3dd5e91846c410";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "attuapp://callback";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;
    private UpdatePlaylistThread upThread;
    TableLayout country_table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("started", "content view");

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-library-read"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        country_table=(TableLayout)findViewById(R.id.country_table);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
                SpotifyApi api = new SpotifyApi();

// Most (but not all) of the Spotify Web API endpoints require authorisation.
// If you know you'll only use the ones that don't require authorisation you can skip this step
                api.setAccessToken(response.getAccessToken());

                final SpotifyService spotify = api.getService();

                spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
                    @Override
                    public void success(Album album, Response response) {
                        Log.d("Album success", album.name);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Album failure", error.toString());
                    }
                });
                upThread = new UpdatePlaylistThread();
                upThread.toUpdate = this;
                upThread.spotify = spotify;
                upThread.addObserver(this);
                Thread t = new Thread(upThread);
                t.start();
                spotify.getMe(new SpotifyCallback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        // handle successful response
                        Log.d("Success", user.id);
                        getUserPlayLists(user.id, spotify);
                    }

                    @Override
                    public void failure(SpotifyError error) {
                        // handle error
                        Log.d("SavedTracks failure", error.toString());
                    }
                });
            }
        }
    }

    public void run(){
        TextView t1, t2;
        TableRow row;
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        for (PlaylistSimple play : upThread.l) {
            row = new TableRow(this);

            t1 = new TextView(this);
            t2 = new TextView(this);

            t1.setText(play.name);
            t2.setText(play.uri);
            t1.setTypeface(null, 1);
            t2.setTypeface(null, 1);

            t1.setTextSize(15);
            t2.setTextSize(15);

            t1.setWidth(50 * dip);
            t2.setWidth(150 * dip);
            t1.setPadding(20 * dip, 0, 0, 0);
            row.addView(t1);
            row.addView(t2);
            country_table.addView(row, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
    public void update(Observable obs, Object obj){
        Log.d("Update", "observer notified");

        // handle successful response
        if(upThread == null){
            Log.d("Null", "Going to Crash");
        }
        runOnUiThread(this);
    }

    public void getUserPlayLists(String id, SpotifyService spotify){
        /*
        spotify.getPlaylists(id, new SpotifyCallback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> p, Response response) {
                TableRow row;
                TextView t1, t2;
                //Converting to dip unit
                int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        (float) 1, getResources().getDisplayMetrics());
                // handle successful response
                List<PlaylistSimple> l = p.items;
                for(PlaylistSimple play: l){
                    /*
                    row = new TableRow(this);

                    t1 = new TextView(this);
                    t2 = new TextView(this);

                    t1.setText(play.name);
                    t2.setText(play.uri);

                    t1.setTypeface(null, 1);
                    t2.setTypeface(null, 1);

                    t1.setTextSize(15);
                    t2.setTextSize(15);

                    t1.setWidth(50 * dip);
                    t2.setWidth(150 * dip);
                    t1.setPadding(20*dip, 0, 0, 0);
                    row.addView(t1);
                    row.addView(t2);

                    country_table.addView(row, new TableLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            *//*
                    Log.d("Success", play.name);
                }

            }

            @Override
            public void failure(SpotifyError error) {
                // handle error
                Log.d("SavedTracks failure", error.toString());
            }
        });*/
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}