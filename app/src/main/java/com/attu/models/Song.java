package com.attu.models;

import com.attu.remote.Server;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by patrick on 5/29/15.
 */
public class Song extends SpotifySong {
    // The songId is **GLOBALLY UNIQUE*** -- the same song will have a different songId in different SongRooms
    int songId;

    public Song(String spotifyUri, int songId) {
        super(spotifyUri);
        this.songId = songId;
    }

    public List<Vote> getVotes() {
        return server.getVotes(songId);
    }

    public int getSongId() {
        return songId;
    }
}
