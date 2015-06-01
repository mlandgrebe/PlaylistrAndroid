package com.attu.remote;


import android.location.Location;
import com.attu.data.MotionInstant;
import com.attu.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import kaaes.spotify.webapi.android.models.User;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;
import retrofit.http.*;

import java.util.Date;
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
        List<Vote> submitVote(@Query(SONG_ID) ObjectId songId,
                              @Query(USER_ID) ObjectId userId,
                              @Query("isUp") boolean isUp);

        @GET("/getQueue")
        SongQueue getQueue(@Query(SR_ID) ObjectId songRoomId);

        @GET("/setQueue")
        void setQueue(@Query(SR_ID) String srId, @Query("songIds") List<Integer> songIds);

        @GET("/changeQueue")
        List<Song> changeQueue(@Query(QUEUE_ID) ObjectId songQueueId,
                               @Query(SONG_ID) ObjectId songId,
                               @Query("isEnq") boolean isEnq);

        @FormUrlEncoded
        @POST("/bulkEnq")
        String bulkEnq(@Field("spotifyURIs") List<String> spotifyURIs, @Query(QUEUE_ID) ObjectId songQueueId);

        @GET("/leaveSR")
        String leaveSR(@Query(USER_ID) ObjectId userId, @Query(SR_ID) ObjectId songRoomId);

        // FIXME: this needs to be a PointLocation eventually
        @GET("/nearbySR")
        List<SongRoom> nearbySR(@Query(LOCATION) PointLocation location);

        @GET("/srMembers")
        List<APIUser> srMembers(@Query(SR_ID) ObjectId srId);

        @GET("/getSongs")
        List<Song> getSongs(@Query(QUEUE_ID) ObjectId songQueueId);

        @GET("/createSong")
        Song createSong(@Query(SPOTIFY_URI) String spotifyUri);

        // Needs a return type to compile
        @GET("/dropUsers")
        String dropUsers();

        @GET("/popSong")
        Song popSong(@Query(SR_ID) ObjectId srId);

        @GET("/getPlaying")
        Song getPlaying(@Query(SR_ID) ObjectId srId);

        @GET("/startPlaying")
        String startPlaying(@Query(SR_ID) ObjectId srId);

        @GET("/stopPlaying")
        String stopPlaying(@Query(SR_ID) ObjectId srId);

        @GET("/getStart")
        Date getStart(@Query(SONG_ID) ObjectId songId);

        @GET("/getStop")
        Date getStop(@Query(SONG_ID) ObjectId songId);

        @FormUrlEncoded
        @POST("/submitActivity")
        String submitActivity(@Field(USER_ID) ObjectId userId, @Field("instants") List<MotionInstant> instants);

        @GET("/getActivity")
        List<MotionInstant> getActivity(@Query(USER_ID) ObjectId userId);
    }


    public ServerLinked addMyself(ServerLinked serverLinked) {
        serverLinked.setServer(this);
        return serverLinked;
    }

    public Server(String host) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer())
                .registerTypeAdapter(PointLocation.class, new PointLocationDeserializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(MotionInstant.class, new MotionInstantDeserializer())
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

    @GET("/submitVote")
    public List<Vote> submitVote(@Query(SONG_ID) ObjectId songId,
                                 @Query(USER_ID) ObjectId userId,
                                 @Query("isUp") boolean isUp) {
        return api.submitVote(songId, userId, isUp);
    }

    public SongQueue getQueue(ObjectId songRoomId) {
        return (SongQueue)api.getQueue(songRoomId).setServer(this);
    }


    public void setQueue(String srId, List<Integer> songIds) {
        api.setQueue(srId, songIds);
    }


    public List<Song> changeQueue(ObjectId songQueueId,
                                  ObjectId songId,
                                  boolean isEnq) {
        return api.changeQueue(songQueueId, songId, isEnq);
    }


    public void leaveSR(ObjectId userId, ObjectId songRoomId) {
        api.leaveSR(userId, songRoomId);
    }


    public List<SongRoom> nearbySR(PointLocation location) {
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

    @GET("/createSong")
    public Song createSong(@Query(SPOTIFY_URI) String spotifyUri) {
        return (Song)api.createSong(spotifyUri).setServer(this);
    }

    @GET("/getPlaying")
    public Song getPlaying(@Query(SR_ID) ObjectId srId) {
        return (Song) api.getPlaying(srId).setServer(this);
    }

    @GET("/popSong")
    public Song popSong(@Query(SR_ID) ObjectId srId) {
        return api.popSong(srId).setServer(this);
    }

    @GET("/startPlaying")
    public void startPlaying(@Query(SR_ID) ObjectId srId) {
        api.startPlaying(srId);
    }

    @GET("/stopPlaying")
    public void stopPlaying(@Query(SR_ID) ObjectId srId) {
        api.stopPlaying(srId);
    }

    @GET("/getStart")
    public Date getStart(@Query(SONG_ID) ObjectId songId) {
        return api.getStart(songId);
    }

    @GET("/getStop")
    public Date getStop(@Query(SONG_ID) ObjectId songId) {
        return api.getStop(songId);
    }

    public void submitActivity(ObjectId userId, List<MotionInstant> instants) {
        api.submitActivity(userId, instants);
    }

    @GET("/getActivity")
    public List<MotionInstant> getActivity(@Query(USER_ID) ObjectId userId) {
        return api.getActivity(userId);
    }

    @GET("/bulkEnq")
    public void bulkEnq(@Query("spotifyURIs") List<String> spotifyURIs, @Query(QUEUE_ID) ObjectId songQueueId) {
        api.bulkEnq(spotifyURIs, songQueueId);
    }
}
