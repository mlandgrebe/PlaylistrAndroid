package com.attu.models;

import com.attu.remote.Server;

/**
 * Created by patrick on 5/29/15.
 */
public class SpotifySong extends Identified {
    protected String spotifyURI;
    protected String name;

    public SpotifySong(ObjectId id, String spotifyURI) {
        super(id);
        this.spotifyURI = spotifyURI;
    }

    public SpotifySong(ObjectId id, String spotifyURI, String name) {
        super(id);
        this.spotifyURI = spotifyURI;
        this.name = name;
    }

    // There should be some method to fetch the spotify info about this, i dont know what that is though

    // Something here about playing? I dont know, that will probably be a thing with the views


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpotifySong that = (SpotifySong) o;

        return spotifyURI.equals(that.spotifyURI) && getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return spotifyURI.hashCode();
    }

    @Override
    public String toString() {
        return "SpotifySong{" +
                "spotifyURI='" + spotifyURI + '\'' +
                "} " + super.toString();
    }

    public String getSpotifyURI() {
        return spotifyURI;
    }

    public String getSpotifyTrackIdFromUri(){
        return spotifyURI.substring(14);
    }

    public static String getSpotifyTrackIdFromUri(String s){
        return s.substring(14);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
