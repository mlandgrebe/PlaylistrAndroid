package com.attu.attu.views;

import android.util.Log;

import java.util.Observable;

import com.attu.models.*;
import com.attu.remote.*;
import com.attu.util.State;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.User;

/**
 * Created by zacharyjenkins on 5/30/15.
 */
public class SongRoomHomeThread extends Observable implements Runnable{
    public String name;

    @Override
    public void run() {
        State state = State.getState();
        APIUser apiUser = state.getUser();
        apiUser.createSR(name);
    }
}
