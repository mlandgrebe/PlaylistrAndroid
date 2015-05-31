package com.attu.attu.views;

import android.util.Log;

import java.util.Observable;

import com.attu.models.*;
import com.attu.remote.*;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.User;

/**
 * Created by zacharyjenkins on 5/30/15.
 */
public class SongRoomHomeThread extends Observable implements Runnable{
    public SongRoom room;
    public Server server;
    public String serverURL;
    public SpotifyService spotify;
    public String name;
    @Override
    public void run() {
        Log.d("Spotify", spotify.toString());
        // instantiate a server, create SongRoom, and move to SongRoomHomeActivity
        Server server = new Server(serverURL);
        //server.dropUsers();

        User spotifyUser = spotify.getMe();

        APIUser user = server.createUser(spotifyUser);
        // need to get the actual location, which should work now
        PointLocation loc = new PointLocation(15, 16);
        Log.d("loc", loc.toString());
        Log.d("User id", user.getId().toString());
        Log.d("name", name);
        room = server.createSR(user.getId(), loc, name);

        Log.d("Got passed songroom", "Awesome");

    }
}
