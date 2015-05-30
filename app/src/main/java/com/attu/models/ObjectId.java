package com.attu.models;

import com.google.gson.annotations.SerializedName;

/**
 * We need this to make the parser Just Work
 */
public class ObjectId {
    @SerializedName("$oid")
    private String oid;

    public ObjectId(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    // Defined like this so retrofit understands
    @Override
    public String toString() {
        return getOid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectId objectId = (ObjectId) o;

        return oid.equals(objectId.oid);

    }

    @Override
    public int hashCode() {
        return oid.hashCode();
    }
}
