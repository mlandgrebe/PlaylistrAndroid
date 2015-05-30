package com.attu.remote;

import com.attu.models.APIUser;
import junit.framework.TestCase;
import kaaes.spotify.webapi.android.models.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import retrofit.RetrofitError;

import java.net.ConnectException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by patrick on 5/28/15.
 */
@RunWith(value = BlockJUnit4ClassRunner.class)
public class ServerTest extends TestCase {

    @Test (expected = RetrofitError.class)
    public void testCreateUserBad() throws Exception {
        Server server = new Server("http://www.foo.com");
        User spotifyUser = new User();
        spotifyUser.display_name = "John Doe";
        spotifyUser.uri = "some:uri";
        APIUser user = server.createUser(spotifyUser);
    }

//    @Test
//    public void testCreateUser() throws Exception {
//        Server server = new Server("http://www.foo.com");
//        User spotifyUser = new User();
//        spotifyUser.display_name = "John Doe";
//        spotifyUser.uri = "some:uri";
//        APIUser user = server.createUser(spotifyUser);
//    }
}