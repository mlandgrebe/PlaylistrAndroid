package com.attu.remote;


import android.util.Log;
import com.attu.models.APIUser;
import kaaes.spotify.webapi.android.models.User;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by patrick on 5/27/15.
 */
public class Server {
    private String DEFAULT_HOST = "localhost";
    private RestAPI api = null;

    private interface RestAPI {
        @GET("/createUser")
        APIUser createUser(@Query("spotifyURI") String spotifyURI,
                           @Query("name") String name);

        @GET("/lookupUser")
        APIUser lookupUser(@Query("userId") String userId);
    }



    public Server(String host) {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(host).build();
        api = adapter.create(RestAPI.class);
        Log.d("Got API. Null ?", api == null ? "yes" : "no");

    }

    public Server() {
        new Server(DEFAULT_HOST);
    }

    public APIUser createUser(User spotifyUser) {
        Log.d("Submitting user's URI", spotifyUser.uri);
        Log.d("Submitting user's name", spotifyUser.display_name);
        return api.createUser(spotifyUser.uri, spotifyUser.display_name);
    }


//
//    @GET("/login")
//    public APIUser login(@Path("spotifyData") String spotifyData) {
//        return api.login(spotifyData);
//    }

    //    private class APIService implements RestAPI {
//        private RestAPI api = null;
//        public APIService(RestAdapter adapter) {
//            api = adapter.create(RestAPI.class);
//        }
//
//        @Override
//        @GET("/login")
//        public APIUser login(@Path("spotifyData") String spotifyData) {
//            return api.login(spotifyData);
//        }
//    }




}
