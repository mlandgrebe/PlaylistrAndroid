package com.attu.models;

import android.location.Location;

/**
 * Created by patrick on 5/29/15.
 */
public class SongRoom extends Identified {
    private Location location;
    private String name;

    public SongRoom(ObjectId id, Location location, String name) {
        super(id);
        this.location = location;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SongQueue getQueue() {
        return server.getQueue(id);
    }

    public Location getLocation() {
        return location;
    }


}
