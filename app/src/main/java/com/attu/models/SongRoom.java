package com.attu.models;

import android.location.Location;
import com.attu.remote.Server;

import java.util.List;

/**
 * Created by patrick on 5/29/15.
 */
public class SongRoom extends ServerLinked {
    int id;
    Location location;

    public SongRoom(int id, Location location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public SongQueue getQueue() {
        return server.getQueue(id);
    }

    public Location getLocation() {
        return location;
    }


}
