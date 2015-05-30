package com.attu.models;

import java.util.List;
import java.util.Queue;

/**
 * Created by patrick on 5/29/15.
 */
public class SongQueue extends ServerLinked {
    List<Song> queue;
    int id;

    public SongQueue(List<Song> queue, int id) {
        this.queue = queue;
        this.id = id;
    }

    // We're not going to bother updating our local state --- we should fetch one of these from the server every time
    // we want one
    public void enqueue(Song song) {
        server.changeQueue(id, song.getSongId(), true);
    }

    // See comment above
    public void dequeue(Song song) {
        server.changeQueue(id, song.getSongId(), false);
    }
}
