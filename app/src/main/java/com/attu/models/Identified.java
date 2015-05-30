package com.attu.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by patrick on 5/29/15.
 */
public class Identified extends ServerLinked {
    @SerializedName("_id")
    protected ObjectId id;

    public Identified(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }
}
