package com.attu.attu.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.location.LocationManager;
import com.attu.data.SRLocationListener;

import com.attu.attu.R;
import com.attu.models.APIUser;
import com.attu.remote.Server;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;
import com.attu.util.State;

import kaaes.spotify.webapi.android.models.User;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "2de62f40903247208d3dd5e91846c410";
    private static final String REDIRECT_URI = "attuapp://callback";
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    Button create_songroom_button;
    Button join_songroom_button;
    Button listen_yourself_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate starting");

        // needed to add these two lines of code to silence runtime errors
        ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-library-read"});
        AuthenticationRequest request = builder.build();

        create_songroom_button = (Button) findViewById(R.id.create_songroom_button);
        join_songroom_button = (Button) findViewById(R.id.join_songroom_button);
        listen_yourself_button = (Button) findViewById(R.id.listen_yourself_button);

        create_songroom_button.setOnClickListener(createSongRoomHandler);
        join_songroom_button.setOnClickListener(joinSongRoomHandler);
        listen_yourself_button.setOnClickListener(listenYourselfHandler);

        Log.d("MainActivity", "launching new activity");

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    View.OnClickListener createSongRoomHandler = new View.OnClickListener() {
        public void onClick(View v) {
//            v.setBackgroundColor(Color.RED);
            Intent i = new Intent(v.getContext(), CreateSongRoomActivity.class);
            startActivity(i);
        }
    };

    View.OnClickListener joinSongRoomHandler = new View.OnClickListener() {
        public void onClick(View v) {
//            v.setBackgroundColor(Color.GREEN);
            System.out.println("Button clicked: " + v.getId());
            Intent i = new Intent(v.getContext(), JoinSongRoomActivity.class);
            startActivity(i);
        }
    };

    View.OnClickListener listenYourselfHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Context con = v.getContext();
            Intent i = new Intent(con, UpdatePlaylistsActivity.class);
            startActivity(i);
            //Track toPlay = (Track) v.getTag();
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
                api.setAccessToken(response.getAccessToken());
                final SpotifyService spotify = api.getService();

                Server server = new Server("http://54.191.46.253:5000");
                User spotifyUser = spotify.getMe();
                APIUser apiUser =  server.createUser(spotifyUser);

                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                SRLocationListener srLocationListener = new SRLocationListener(apiUser);
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, srLocationListener);
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, srLocationListener);

                Log.d("MainActivity", "about to initialize");

                State state = State.getState();
                state.setServer(server);
                state.setUser(apiUser);
                state.setSpotifyService(spotify);
                state.setPlayer(mPlayer);
                state.setLocationManager(locationManager);

                Log.d("MainActivity", "initialized");

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