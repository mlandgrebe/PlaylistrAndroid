package com.attu.attu.views;

import java.util.List;
import java.util.Observable;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.attu.models.APIUser;
import com.attu.util.State;
import com.attu.models.SongRoom;

/**
 * Created by zacharyjenkins on 5/31/15.
 */
public class JoinSongRoomThread extends Observable implements Runnable {
    public List<SongRoom> availableRooms;

    @Override
    public void run() {
        State state = State.getState();
        APIUser user = state.getUser();
        Location lastLocation = state.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Log.d("JoinSRThread", lastLocation.toString());
        user.setLocation(lastLocation);

        availableRooms = state.getUser().getNearbySRs();
        setChanged();
        notifyObservers();
    }
}
