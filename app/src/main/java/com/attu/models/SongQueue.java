package com.attu.models;

import java.util.List;

/**
 * Created by patrick on 5/29/15.
 */
public class SongQueue extends Identified {

    public SongQueue(ObjectId id) {
        super(id);
    }

    // We're not going to bother updating our local state --- we should fetch one of these from the server every time
    // we want one
    public void enqueue(Song song) {
        server.changeQueue(id, song.getId(), true);
    }

    // See comment above
    public void dequeue(Song song) {
        server.changeQueue(id, song.getId(), false);
    }

    public List<Song> getSongs() {
        System.out.println(getId());
        return server.getSongs(getId());
    }
}
