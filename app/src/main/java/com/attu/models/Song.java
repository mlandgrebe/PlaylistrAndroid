package com.attu.models;

import com.attu.remote.Server;

import java.util.Date;
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
        System.out.println("server = " + server);
        return server.getVotes(getId());
    }

    public Date getStart() {
        return server.getStart(getId());
    }

    public Date getStop() {
        return server.getStart(getId());
    }

    @Override
    public String toString() {
        return "Song{} " + super.toString();
    }

    @Override
    public Song setServer(Server server) {
        return (Song)super.setServer(server);
    }


}
