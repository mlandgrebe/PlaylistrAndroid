package com.attu.util;

import android.location.LocationManager;
import com.attu.models.APIUser;
import com.attu.remote.Server;
import com.spotify.sdk.android.player.Player;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by patrick on 5/31/15.
 */
public class State {
    private static State instance;
    private Server server;
    private APIUser user;
    private SpotifyService spotifyService;
    private Player player;
    private LocationManager locationManager;

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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }
}
