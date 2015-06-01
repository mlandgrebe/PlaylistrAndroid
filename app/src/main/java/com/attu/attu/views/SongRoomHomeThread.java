package com.attu.attu.views;

import android.app.Activity;
import android.util.Log;

import java.util.List;
import java.util.Observable;

import com.attu.models.*;
import com.attu.models.SongQueue;
import com.attu.models.SongRoom;
import com.attu.util.State;

import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.User;
import retrofit.RetrofitError;


/**
 * Created by zacharyjenkins on 5/30/15.
 */
public class SongRoomHomeThread extends Observable implements Runnable{
    public Activity toUpdate;
    public List<PlaylistTrack> ts;
    public String uri;
    public String name;
    APIUser apiUser;


    @Override
    public void run() {
        State state = State.getState();
        apiUser = state.getUser();
        apiUser.createSR(name);

        if(state != null) {
            ts = null;
            try {
                SpotifyService spotify = state.getSpotifyService();
                User user = spotify.getMe();
                Pager<PlaylistTrack> pt = spotify.getPlaylistTracks(user.id, uri);
                ts = pt.items;
            } catch (RetrofitError error) {
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                Log.d("sync error", spotifyError.getMessage());
            }
            if(ts != null) {
                SongRoom songRoom = apiUser.getSongRoom();
                SongQueue songQueue = songRoom.getQueue();
                Log.d("Queue", "got queue");
                for (PlaylistTrack plTrack : ts) {
                    songQueue.enqueue(plTrack.track.uri);
                    Log.d("enqueued: ", plTrack.track.name);
                }
                setChanged();
                notifyObservers();
            }
        }
    }
}
