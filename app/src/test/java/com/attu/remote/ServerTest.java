package com.attu.remote;

import com.attu.models.APIUser;
import junit.framework.TestCase;
import kaaes.spotify.webapi.android.models.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by patrick on 5/28/15.
 */
public class ServerTest extends TestCase {

    @Test
    public void testCreateUser() throws Exception {
        Server server = new Server("http://www.foo.com");
        User spotifyUser = new User();
        spotifyUser.display_name = "John Doe";
        spotifyUser.uri = "some:uri";
        APIUser user = server.createUser(spotifyUser);

    }
}