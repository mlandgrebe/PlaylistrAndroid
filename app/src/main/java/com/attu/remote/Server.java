package com.attu.remote;


import android.location.Location;
import com.attu.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kaaes.spotify.webapi.android.models.User;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
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
        APIUser lookupUser(@Query("userId") ObjectId userId);

        @GET("/createSR")
        SongRoom createSR(@Query("hostId") String hostId,
                          @Query("location") Location location,
                          @Query("name") String name);

        @GET("/joinSR")
        void joinSR(@Query("srId") ObjectId srId, @Query("userId") ObjectId userId);

        @GET("/getVotes")
        List<Vote> getVotes(@Query("songId") ObjectId songId);

        @GET("/submitVote")
        void submitVote(@Query("userId") ObjectId userId, boolean isUp);

        @GET("/getQueue")
        SongQueue getQueue(@Query("srId") ObjectId songRoomId);

        @GET("/setQueue")
        void setQueue(@Query("srId") String srId, @Query("songIds") List<Integer> songIds);

        @GET("/changeQueue")
        void changeQueue(@Query("queueId") ObjectId songQueueId,
                         @Query("songId") ObjectId songId,
                         @Query("isEnq") boolean isEnq);

        @GET("/leaveSR")
        void leaveSR(@Query("userId") ObjectId userId, @Query("srId") ObjectId songRoomId);

        @GET("/nearbySR")
        List<SongRoom> nearbySR(@Query("location") Location location);

        @GET("/srMembers")
        List<APIUser> srMembers(@Query("srId") String srId);

        // Needs a return type to compile
        @GET("/dropUsers")
        String dropUsers();
    }


    public ServerLinked addMyself(ServerLinked serverLinked) {
        serverLinked.setServer(this);
        return serverLinked;
    }

    public Server(String host) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer())
                .registerTypeAdapter(ObjectId.class, new ObjectIdSerializer())
                .create();

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(host)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        api = adapter.create(RestAPI.class);
    }

    public Server() {
        this(DEFAULT_HOST);
    }

    public APIUser createUser(User spotifyUser) {
        return (APIUser)api.createUser(spotifyUser.uri, spotifyUser.display_name).setServer(this);
    }

    @GET("/lookupUser")
    public APIUser lookupUser(@Query("userId") ObjectId userId) {
        return (APIUser)api.lookupUser(userId).setServer(this);
    }

    @GET("/createSR")
    public SongRoom createSR(@Query("hostId") String hostId,
                             @Query("location") Location location,
                             @Query("name") String name) {
        return (SongRoom)api.createSR(hostId, location, name).setServer(this);
    }

    @GET("/joinSR")
    public void joinSR(@Query("srId") ObjectId srId, @Query("userId") ObjectId userId) {
        api.joinSR(srId, userId);
    }

    public List<Vote> getVotes(ObjectId songId) {
        return api.getVotes(songId);
    }

    @GET("/createUser")
    public APIUser createUser(@Query("spotifyURI") String spotifyURI, @Query("name") String name) {
        return (APIUser)api.createUser(spotifyURI, name).setServer(this);
    }

    @GET("/submitVote")
    public void submitVote(@Query("userId") ObjectId userId, boolean isUp) {
        api.submitVote(userId, isUp);
    }

    @GET("/getQueue")
    public SongQueue getQueue(@Query("srId") ObjectId songRoomId) {
        return (SongQueue)api.getQueue(songRoomId).setServer(this);
    }

    @GET("/setQueue")
    public void setQueue(@Query("srId") String srId, @Query("songIds") List<Integer> songIds) {
        api.setQueue(srId, songIds);
    }

    @GET("/changeQueue")
    public void changeQueue(@Query("songQueueId") ObjectId songQueueId,
                            @Query("songId") ObjectId songId,
                            @Query("isEnq") boolean isEnq) {
        api.changeQueue(songQueueId, songId, isEnq);
    }

    @GET("/leaveSR")
    public void leaveSR(@Query("userId") ObjectId userId, @Query("srId") ObjectId songRoomId) {
        api.leaveSR(userId, songRoomId);
    }

    @GET("/nearbySR")
    public List<SongRoom> nearbySR(@Query("location") Location location) {
        return api.nearbySR(location);
    }

    @GET("/srMembers")
    public List<APIUser> srMembers(@Query("srId") String srId) {
        return api.srMembers(srId);
    }

    @GET("/dropUsers")
    public void dropUsers() {
        api.dropUsers();
    }
}
