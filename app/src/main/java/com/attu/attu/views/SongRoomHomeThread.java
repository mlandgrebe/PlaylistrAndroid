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
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.User;
import retrofit.RetrofitError;
import java.util.ArrayList;


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
        SpotifyService spotify = state.getSpotifyService();
        if(state.getUser().getHostStatus() == true) {
            if (state != null) {
                apiUser = state.getUser();
                apiUser.createSR(name);
                ts = null;
                try {
                    User user = spotify.getMe();
                    Pager<PlaylistTrack> pt = spotify.getPlaylistTracks(user.id, uri);
                    ts = pt.items;
                } catch (RetrofitError error) {
                    SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                    Log.d("sync error", spotifyError.getMessage());
                }
                if (ts != null) {
                    SongRoom songRoom = apiUser.getSongRoom();
                    SongQueue songQueue = songRoom.getQueue();
                    Log.d("Queue", "got queue");
                    List<String> trackUris = new ArrayList<String>();
                    for (PlaylistTrack plTrack : ts) {
                        trackUris.add(plTrack.track.uri);
                        Log.d("Queue", plTrack.track.name);
                    }
                    songQueue.bulkEnq(trackUris);
                    List<Song> queue = state.getUser().getSongRoom().getQueue().getSongs();
                    for(Song s: queue){
                        String trackID = s.getSpotifyTrackIdFromUri();
                        Track t = spotify.getTrack(trackID);
                        s.setName(t.name);
                    }
                    setChanged();
                    notifyObservers();
                }
            }
        }
        if(state.getUser().getHostStatus() == false){
            List<Song> queue = state.getUser().getSongRoom().getQueue().getSongs();
            String trackString = new String();
            for(Song s: queue){
                String trackID = s.getSpotifyTrackIdFromUri();
                trackString += trackID + ",";

                //Track t = spotify.getTrack(trackID);
                //s.setName(t.name);
            }
            trackString = trackString.substring(0, trackString.length() -1);
            Log.d("track ids: ", trackString);

            Tracks trackObjects = spotify.getTracks(trackString);
            List<Track> result  = trackObjects.tracks;
            for(int i = 0; i<queue.size(); i++){
                String obj = result.get(i).name;
                queue.get(i).setName(obj);
            }
            setChanged();
            notifyObservers();
        }
    }
}
