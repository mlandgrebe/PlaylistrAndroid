package com.attu.models;

import android.location.Location;

/**
 * Created by patrick on 5/29/15.
 */
public class SongRoom extends Identified {
    Location location;

    public SongRoom(ObjectId id, Location location) {
        super(id);
        this.location = location;
    }

    public SongQueue getQueue() {
        return server.getQueue(id);
    }

    public Location getLocation() {
        return location;
    }


}
