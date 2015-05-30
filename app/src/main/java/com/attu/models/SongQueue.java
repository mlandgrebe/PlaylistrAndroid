package com.attu.models;

import java.util.List;

/**
 * Created by patrick on 5/29/15.
 */
public class SongQueue extends Identified {
    List<Song> queue;

    public SongQueue(ObjectId id, List<Song> queue) {
        super(id);
        this.queue = queue;
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
}
