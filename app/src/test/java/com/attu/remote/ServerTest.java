package com.attu.remote;

import com.attu.models.APIUser;
import com.attu.models.Identified;
import com.attu.models.ObjectId;
import junit.framework.TestCase;
import kaaes.spotify.webapi.android.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import retrofit.RetrofitError;

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

    public void testSerializeObjectId() throws Exception {
        Identified identified = new Identified(new ObjectId("abc"));

        assertThat(identified.toString(), equalTo("{\"_id\"={\"$oid\"=\"abc\"}}"));
    }

    public void testSongRoomCRUD() throws Exception {
        Server server = new Server("http://localhost");


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