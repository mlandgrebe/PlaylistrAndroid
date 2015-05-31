package com.attu.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by patrick on 5/29/15.
 */
public class Identified extends ServerLinked implements Serializable {
    @SerializedName("_id")
    protected final ObjectId id;

    public Identified(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identified that = (Identified) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
