package com.attu.models;

/**
 * Created by patrick on 5/27/15.
 */
public class APIUser {
    private final String spotifyURI;
    private final String name;
    private final int userId;

    public APIUser(String spotifyURI, String name, int userId) {
        this.spotifyURI = spotifyURI;
        this.name = name;
        this.userId = userId;
    }
}
