package com.attu.models;

import com.attu.remote.Server;

/**
 * Created by patrick on 5/29/15.
 */
public class SpotifySong extends ServerLinked {
    Server server;
    String spotifyUri;

    public SpotifySong(String spotifyUri) {
        this.spotifyUri = spotifyUri;
    }


    // There should be some method to fetch the spotify info about this, i dont know what that is though

    // Something here about playing? I dont know, that will probably be a thing with the views
}