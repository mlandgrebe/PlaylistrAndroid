package com.attu.models;

import android.location.Location;
import com.attu.data.MotionListener;
import com.attu.data.MotionMonitor;
import com.attu.util.Maybe;

import java.io.Serializable;
import java.util.List;

/**
 * Created by patrick on 5/27/15.
 */
public class APIUser extends Identified implements Serializable {
    private final String spotifyURI;
    private final String name;
    private SongRoom songRoom;
    private final MotionMonitor motionMonitor;
    private final float DEFAULT_DISTANCE_CUTOFF_METERS = 100.0f;
    private boolean isHost;

    // The LocationListener should set this
    private PointLocation location;

    public APIUser(ObjectId id, String spotifyURI, String name) {
        super(id);
        this.spotifyURI = spotifyURI;
        this.name = name;
        this.motionMonitor = new MotionMonitor();
    }

    public String getSpotifyURI() {
        return spotifyURI;
    }

    public String getName() {
        return name;
    }

    public SongRoom createSR(String name) {
        System.out.println(location);
        songRoom = server.createSR(id, getLocation(), name);
        return songRoom;
    }

    public void joinSR(SongRoom songRoom) {
        System.out.println("APIUser.joinSR");
        this.songRoom = server.joinSR(songRoom.getId(), id);
    }

    public void leaveSR() throws BadStateException {
        if (songRoom == null) {
            throw new BadStateException("User " + id + "is not part of a songRoom.");
        } else {
            server.leaveSR(id, songRoom.getId());
            songRoom = null;
        }
    }

    public PointLocation getLocation() {
        return location;
    }

    public void setLocation(PointLocation location) {
        this.location = location;
    }

    public void setLocation(Location location) {
        this.location = PointLocation.fromLocation(location);
    }

    public List<SongRoom> getNearbySRs() {
        return server.nearbySR(location);
    }

    // Return a songroom if there is a songroom within range that this user should be prompted to join.
    public Maybe<SongRoom> getJoinable() {
        List<SongRoom> srs = getNearbySRs();
        for (SongRoom sr : srs) {
            if (sr.getLocation().distanceTo(getLocation()) < DEFAULT_DISTANCE_CUTOFF_METERS) {
                return new Maybe<SongRoom>(sr);
            }
        }
        return new Maybe<SongRoom>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APIUser apiUser = (APIUser) o;

        if (!spotifyURI.equals(apiUser.spotifyURI)) return false;
        return name.equals(apiUser.name);

    }

    @Override
    public int hashCode() {
        int result = spotifyURI.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public List<Vote> upvote(Song song) {
        return server.submitVote(song.getId(), getId(), true);
    }

    public List<Vote> downvote(Song song) {
        return server.submitVote(song.getId(), getId(), false);
    }

    public MotionListener getMotionListener() {
        return new MotionListener(motionMonitor);
    }

    public boolean inRoom() {
        return songRoom != null;
    }

    public void setHostStatus(boolean host) {
        isHost = host;
    }

    public boolean getHostStatus() {
        return isHost;
    }

    public SongRoom getSongRoom() {
        return songRoom;
    }
}
