package com.attu.models;

import com.attu.remote.Server;

/**
 * Created by patrick on 5/30/15.
 */
public class ServerLinked {
    protected Server server;

    public ServerLinked setServer(Server server) {
        this.server = server;
        return this;
    }
}
