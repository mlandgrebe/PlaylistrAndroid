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

//  Query parameters
    private static final String USER_ID = "userId";
    private static final String LOCATION = "location";
    private static final String HOST_ID = "hostId";
    private static final String SR_ID = "srId";
    private static final String SPOTIFY_URI = "spotifyURI";
    private static final String QUEUE_ID = "queueId";
    private static final String NAME = "name";
    private static final String SONG_ID = "songId";

    private interface RestAPI {
        @GET("/createUser")
        APIUser createUser(@Query(SPOTIFY_URI) String spotifyURI,
                           @Query(NAME) String name);

        @GET("/lookupUser")
        APIUser lookupUser(@Query(USER_ID) ObjectId userId);

        @GET("/createSR")
        SongRoom createSR(@Query(HOST_ID) ObjectId hostId,
                          @Query(LOCATION) PointLocation location,
                          @Query(NAME) String name);

        @GET("/joinSR")
        SongRoom joinSR(@Query(SR_ID) ObjectId srId, @Query(USER_ID) ObjectId userId);

        @GET("/getVotes")
        List<Vote> getVotes(@Query(SONG_ID) ObjectId songId);

        @GET("/submitVote")
        void submitVote(@Query(USER_ID) ObjectId userId, @Query("isUp") boolean isUp);

        @GET("/getQueue")
        SongQueue getQueue(@Query(SR_ID) ObjectId songRoomId);

        @GET("/setQueue")
        void setQueue(@Query(SR_ID) String srId, @Query("songIds") List<Integer> songIds);

        @GET("/changeQueue")
        void changeQueue(@Query(QUEUE_ID) ObjectId songQueueId,
                         @Query(SONG_ID) ObjectId songId,
                         @Query("isEnq") boolean isEnq);

        @GET("/leaveSR")
        String leaveSR(@Query(USER_ID) ObjectId userId, @Query(SR_ID) ObjectId songRoomId);

        // FIXME: this needs to be a PointLocation eventually
        @GET("/nearbySR")
        List<SongRoom> nearbySR(@Query(LOCATION) Location location);

        @GET("/srMembers")
        List<APIUser> srMembers(@Query(SR_ID) ObjectId srId);

        @GET("/getSongs")
        List<Song> getSongs(@Query(QUEUE_ID) ObjectId songQueueId);

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
                .registerTypeAdapter(PointLocation.class, new PointLocationDeserializer())
                .create();

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(host)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = adapter.create(RestAPI.class);
    }

    public Server() {
        this(DEFAULT_HOST);
    }

    public APIUser createUser(User spotifyUser) {
        return (APIUser)api.createUser(spotifyUser.uri, spotifyUser.display_name).setServer(this);
    }

    public APIUser lookupUser(ObjectId userId) {
        return (APIUser)api.lookupUser(userId).setServer(this);
    }


    public SongRoom createSR(ObjectId hostId,
                             PointLocation location,
                             String name) {
        return (SongRoom)api.createSR(hostId, location, name).setServer(this);
    }


    public SongRoom joinSR(ObjectId srId, ObjectId userId) {
        return (SongRoom)api.joinSR(srId, userId).setServer(this);
    }

    public List<Vote> getVotes(ObjectId songId) {
        return api.getVotes(songId);
    }


    public APIUser createUser(String spotifyURI, String name) {
        return (APIUser)api.createUser(spotifyURI, name).setServer(this);
    }


    public void submitVote(ObjectId userId, boolean isUp) {
        api.submitVote(userId, isUp);
    }


    public SongQueue getQueue(ObjectId songRoomId) {
        return (SongQueue)api.getQueue(songRoomId).setServer(this);
    }


    public void setQueue(String srId, List<Integer> songIds) {
        api.setQueue(srId, songIds);
    }


    public void changeQueue(ObjectId songQueueId,
                            ObjectId songId,
                            boolean isEnq) {
        api.changeQueue(songQueueId, songId, isEnq);
    }


    public void leaveSR(ObjectId userId, ObjectId songRoomId) {
        api.leaveSR(userId, songRoomId);
    }


    public List<SongRoom> nearbySR(Location location) {
        return api.nearbySR(location);
    }


    public List<APIUser> srMembers(ObjectId srId) {
        return api.srMembers(srId);
    }


    public void dropUsers() {
        api.dropUsers();
    }


    public List<Song> getSongs(ObjectId songQueueId) {
        return api.getSongs(songQueueId);
    }
}
