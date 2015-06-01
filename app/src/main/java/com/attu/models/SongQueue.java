package com.attu.models;

import com.attu.util.Maybe;

import java.util.List;

/**
 * Created by patrick on 5/29/15.
 */
public class SongQueue extends Identified {

    public SongQueue(ObjectId id) {
        super(id);
    }

    private List<Song> changeQueue(Song song, boolean isEnq) {
        return server.changeQueue(id, song.getId(), isEnq);
    }

    // We're not going to bother updating our local state --- we should fetch one of these from the server every time
    // we want one
    public List<Song> enqueue(Song song) {
        return changeQueue(song, true);
    }

    public List<Song> enqueue(String spotifyURI) {
        Song song = server.createSong(spotifyURI);
        return enqueue(song);
    }

    public List<Song> enqueue(SpotifySong spotifySong) {
        return enqueue(spotifySong.spotifyURI);
    }

    // See comment above
    public List<Song> dequeue(Song song) {
        return  changeQueue(song, false);
    }

    public List<Song> getSongs() {
        System.out.println(getId());
        return server.getSongs(getId());
    }

    public void bulkEnq(List<String> spotifyURIs, List<String> names) {
        server.bulkEnq(spotifyURIs, names, getId());
    }
}
