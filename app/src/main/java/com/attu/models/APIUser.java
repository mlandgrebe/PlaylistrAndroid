package com.attu.models;

import android.location.Location;
import com.attu.util.Maybe;

import java.io.Serializable;
import java.util.List;

/**
 * Created by patrick on 5/27/15.
 */
public class APIUser extends ServerLinked implements Serializable {
    private final String spotifyURI;
    private final String name;
    private final int id;
    private SongRoom songRoom;
    private final float DEFAULT_DISTANCE_CUTOFF_METERS = 100.0f;

    // The LocationListener should set this
    private Location location;



    public APIUser(String spotifyURI, String name, int userId) {
        this.spotifyURI = spotifyURI;
        this.name = name;
        this.id = userId;
    }

    public String getSpotifyURI() {
        return spotifyURI;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void joinSR(SongRoom songRoom) {
        server.joinSR(songRoom.getId(), id);
        songRoom = songRoom;
    }

    //
    public void leaveSR() throws BadStateException {
        if (songRoom == null) {
            throw new BadStateException("User " + id + "is not part of a songRoom.");
        } else {
            server.leaveSR(id, songRoom.getId());
            songRoom = null;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private List<SongRoom> getNearbySRs() {
        return server.nearbySR(location);
    }

    // Return a songroom if there is a songroom within range that this user should be prompted to join.
    private Maybe<SongRoom> getSRToJoin() {
        List<SongRoom> srs = getNearbySRs();

        for (SongRoom sr : srs) {
            if (sr.getLocation().distanceTo(location) < DEFAULT_DISTANCE_CUTOFF_METERS) {
                return new Maybe<SongRoom>(sr);
            }
        }
        return new Maybe<SongRoom>();
    }

}
