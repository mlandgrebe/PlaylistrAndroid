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

    @Override
    public String toString() {
        return "{" +
                "$oid='" + oid + '\'' +
                '}';
    }
}
