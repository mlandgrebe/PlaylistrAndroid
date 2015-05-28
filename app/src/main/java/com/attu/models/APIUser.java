package com.attu.models;

import java.io.Serializable;

/**
 * Created by patrick on 5/27/15.
 */
public class APIUser implements Serializable {
    private final String spotifyURI;
    private final String name;
    private final int userId;

    public APIUser(String spotifyURI, String name, int userId) {
        this.spotifyURI = spotifyURI;
        this.name = name;
        this.userId = userId;
    }

    public String getSpotifyURI() {
        return spotifyURI;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }
}
