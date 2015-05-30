package models;

import remote.Server;

/**
 * Created by patrick on 5/29/15.
 */
public class ServerLinked {
    Server server;

    public ServerLinked setServer(Server server) {
        this.server = server;
        return this;
    }
}
