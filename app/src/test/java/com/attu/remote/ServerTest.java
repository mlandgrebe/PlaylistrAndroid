package com.attu.remote;

import android.content.Intent;
import android.location.Location;
import com.attu.models.*;
import junit.framework.TestCase;
import kaaes.spotify.webapi.android.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import retrofit.RetrofitError;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by patrick on 5/28/15.
 */
@RunWith(value = BlockJUnit4ClassRunner.class)
public class ServerTest extends TestCase {

    private User spotifyUser;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        spotifyUser = new User();
        spotifyUser.display_name = "John Doe";
        spotifyUser.uri = "some:uri";
    }

    @Test (expected = RetrofitError.class)
    public void testCreateUserBad() throws Exception {
        Server server = new Server("http://www.foo.com");

        assertNotNull(spotifyUser);

        APIUser user = server.createUser(spotifyUser);
    }
    // NEED REAL SERVER RUNNING
    @Test
    public void testUserCRUD() throws Exception {
        Server server = new Server("http://localhost:5000");
        server.dropUsers();

        APIUser user = server.createUser(spotifyUser);

        assertThat(user.getName(), equalTo(spotifyUser.display_name));
        assertThat(user.getSpotifyURI(), equalTo(spotifyUser.uri));
        assertNotNull(user.getId());

        APIUser userFromLookup = server.lookupUser(user.getId());

        assertThat(userFromLookup.getName(), equalTo(spotifyUser.display_name));
        assertThat(userFromLookup.getSpotifyURI(), equalTo(spotifyUser.uri));
        assertThat(userFromLookup.getId(), equalTo(user.getId()));
    }

    @Test
    public void testSongRoomCRUD() throws Exception {
        Server server = new Server("http://localhost:5000");
        server.dropUsers();

        APIUser user = server.createUser(spotifyUser);
        PointLocation loc = new PointLocation(15, 16);

        SongRoom room = server.createSR(user.getId(), loc, "testSr");

        assertThat(room.getName(), equalTo("testSR"));
        assertThat(room.getLocation(), equalTo(loc));
        assertNotNull(room.getId());

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