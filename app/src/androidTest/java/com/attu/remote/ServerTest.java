package com.attu.remote;

import com.attu.models.APIUser;
import junit.framework.TestCase;
import kaaes.spotify.webapi.android.models.User;

import static org.junit.Assert.*;

/**
 * Created by patrick on 5/28/15.
 */
public class ServerTest extends TestCase {

    public void testCreateUser() throws Exception {
        User spotifyUser = new User();
        spotifyUser.display_name = "UnitTest";
        spotifyUser.uri = "some:uri:thing";

        Server server = new Server("foo");
        APIUser user = server.createUser(spotifyUser);


    }
}