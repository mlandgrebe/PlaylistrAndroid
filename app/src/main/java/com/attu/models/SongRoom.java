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

    @Override
    public int hashCode() {
        int result = getLocation() != null ? getLocation().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SongRoom{" +
                "location=" + location +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
