package com.attu.models;

import android.location.Location;

import java.util.List;

/**
 * Created by patrick on 5/29/15.
 */
public class SongRoom extends Identified {
    private PointLocation location;
    private String name;

    public SongRoom(ObjectId id, PointLocation location, String name) {
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

    public PointLocation getLocation() {
        return location;
    }

    public List<APIUser> getMembers() {
        return server.srMembers(getId());
    }

}
