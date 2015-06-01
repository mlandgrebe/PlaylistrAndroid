package com.attu.attu.views;

import java.util.List;
import java.util.Observable;
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
        availableRooms = state.getUser().getNearbySRs();
        setChanged();
        notifyObservers();
    }
}
