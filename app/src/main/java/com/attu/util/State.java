package com.attu.util;

import com.attu.models.APIUser;
import com.attu.remote.Server;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by patrick on 5/31/15.
 */
public class State {
    private static State instance;
    private Server server;
    private APIUser user;
    private SpotifyService spotifyService;

    private State() {

    }

    public static State getState() {
        if (instance == null) {
            instance = new State();
        }

        return instance;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public APIUser getUser() {
        return user;
    }

    public void setUser(APIUser user) {
        this.user = user;
    }

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }

    public void setSpotifyService(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }
}
