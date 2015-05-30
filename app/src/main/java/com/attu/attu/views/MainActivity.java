package com.attu.attu.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.attu.attu.R;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import remote.Server;

import kaaes.spotify.webapi.android.*;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "2de62f40903247208d3dd5e91846c410";
    private static final String REDIRECT_URI = "attuapp://callback";
    private static final int REQUEST_CODE = 1337;
    Server server;
    private Player mPlayer;

    // Access Token String
    private String SID;

    Button create_songroom_button;
    Button join_songroom_button;
    Button listen_yourself_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("started", "content view");

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-library-read"});
        AuthenticationRequest request = builder.build();
        server = new Server();

        create_songroom_button = (Button) findViewById(R.id.create_songroom_button);
        join_songroom_button = (Button) findViewById(R.id.join_songroom_button);
        listen_yourself_button = (Button) findViewById(R.id.listen_yourself_button);

        create_songroom_button.setOnClickListener(createSongRoomHandler);
        join_songroom_button.setOnClickListener(joinSongRoomHandler);
        listen_yourself_button.setOnClickListener(listenYourselfHandler);

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    View.OnClickListener createSongRoomHandler = new View.OnClickListener() {
        public void onClick(View v) {
            v.setBackgroundColor(Color.RED);
            System.out.println("Button clicked: " + v.getId());
            //Track toPlay = (Track) v.getTag();
        }
    };

    View.OnClickListener joinSongRoomHandler = new View.OnClickListener() {
        public void onClick(View v) {
            v.setBackgroundColor(Color.GREEN);
            System.out.println("Button clicked: " + v.getId());
            //Track toPlay = (Track) v.getTag();
        }
    };

    View.OnClickListener listenYourselfHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Context con = v.getContext();
            Intent i = new Intent(con, UpdatePlaylistsActivity.class);
            i.putExtra("spotifyToken", SID);
            startActivity(i);
        }
    };

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
                        //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
                SpotifyApi api = new SpotifyApi();
                SID = response.getAccessToken();
                api.setAccessToken(SID);
                final SpotifyService spotify = api.getService();
            }
        }
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