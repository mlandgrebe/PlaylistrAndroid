package com.attu.remote;


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
    private String DEFAULT_HOST = "http://localhost";
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
    }

    public Server() {
        new Server(DEFAULT_HOST);
    }

    public APIUser createUser(User spotifyUser) {
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
