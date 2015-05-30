package com.attu.remote;


import android.location.Location;
import com.attu.models.*;
import kaaes.spotify.webapi.android.models.User;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

import java.util.List;

/**
 * Created by patrick on 5/27/15.
 */
public class Server {
    private static String DEFAULT_HOST = "http://10.0.3.2:5000";
    private RestAPI api = null;

    private interface RestAPI {
        @GET("/createUser")
        APIUser createUser(@Query("spotifyURI") String spotifyURI,
                           @Query("name") String name);

        @GET("/lookupUser")
        APIUser lookupUser(@Query("userId") String userId);

        @GET("/createSR")
        SongRoom createSR(@Query("hostId") int hostId,
                          @Query("location") Location location,
                          @Query("name") String name);

        @GET("/joinSR")
        void joinSR(@Query("srId") Integer srId, @Query("userId") int userId);

        @GET("/getVotes")
        List<Vote> getVotes(@Query("songId") int songId);

        @GET("/submitVote")
        void submitVote(@Query("userId") int userId, boolean isUp);

        @GET("/getQueue")
        SongQueue getQueue(@Query("srId") int songRoomId);

        @GET("/changeQueue")
        void changeQueue(@Query("queueId") int songQueueId,
                         @Query("songId") int songId,
                         @Query("isEnq") boolean isEnq);

        @GET("/leaveSR")
        void leaveSR(@Query("userId") int userId, @Query("srId") int songRoomId);

        @GET("/nearbySR")
        List<SongRoom> nearbySR(@Query("location") Location location);

        @GET("/srMembers")
        List<APIUser> srMembers(@Query("srId") int srId);
    }


    public ServerLinked addMyself(ServerLinked serverLinked) {
        serverLinked.setServer(this);
        return serverLinked;
    }

    public Server(String host) {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(host).build();
        api = adapter.create(RestAPI.class);
    }

    public Server() {
        this(DEFAULT_HOST);
    }

    public APIUser createUser(User spotifyUser) {
        return (APIUser)api.createUser(spotifyUser.uri, spotifyUser.display_name).setServer(this);
    }

    @GET("/lookupUser")
    public APIUser lookupUser(@Query("userId") String userId) {
        return api.lookupUser(userId);
    }

    @GET("/createSR")
    public SongRoom createSR(@Query("hostId") int hostId,
                             @Query("location") Location location,
                             @Query("name") String name) {
        return api.createSR(hostId, location, name);
    }

    @GET("/joinSR")
    public void joinSR(@Query("srId") Integer srId, @Query("userId") int userId) {
        api.joinSR(srId, userId);
    }

    public List<Vote> getVotes(int songId) {
        return api.getVotes(songId);
    }

    @GET("/createUser")
    public APIUser createUser(@Query("spotifyURI") String spotifyURI, @Query("name") String name) {
        return api.createUser(spotifyURI, name);
    }

    @GET("/submitVote")
    public void submitVote(@Query("userId") int userId, boolean isUp) {
        api.submitVote(userId, isUp);
    }

    @GET("/getQueue")
    public SongQueue getQueue(@Query("songRoomId") int songRoomId) {
        return (SongQueue)api.getQueue(songRoomId).setServer(this);
    }

    @GET("/changeQueue")
    public void changeQueue(@Query("songQueueId") int songQueueId,
                            @Query("songId") int songId,
                            @Query("isEnq") boolean isEnq) {
        api.changeQueue(songQueueId, songId, isEnq);
    }

    @GET("/leaveSR")
    public void leaveSR(@Query("userId") int userId, @Query("srId") int songRoomId) {
        api.leaveSR(userId, songRoomId);
    }

    @GET("/nearbySR")
    public List<SongRoom> nearbySR(@Query("location") Location location) {
        return api.nearbySR(location);
    }

    @GET("/srMembers")
    public List<APIUser> srMembers(@Query("srId") int srId) {
        return api.srMembers(srId);
    }
}
