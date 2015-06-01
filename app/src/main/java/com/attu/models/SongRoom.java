package com.attu.models;

import android.location.Location;
import com.attu.util.Maybe;

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

    public Maybe<Song> getPlaying() {
        return new Maybe<>(server.getPlaying(getId()));
    }

    public void startPlaying() {
        server.startPlaying(getId());
    }

    public void stopPlaying() {
        server.stopPlaying(getId());
    }

    public Maybe<Song> popSong() {
        return new Maybe<>(server.popSong(getId()));
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
