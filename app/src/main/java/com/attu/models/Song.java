package com.attu.models;

import java.util.List;

/**
 * Created by patrick on 5/29/15.
 */
public class Song extends SpotifySong {
    // The id is **GLOBALLY UNIQUE*** -- the same song will have a different id in different SongRooms

    public Song(ObjectId id, String spotifyUri) {
        super(id, spotifyUri);
    }

    public List<Vote> getVotes() {
        return server.getVotes(id);
    }

    @Override
    public String toString() {
        return "Song{} " + super.toString();
    }
}
