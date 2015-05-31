package com.attu.data;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import com.attu.models.APIUser;
import com.attu.models.SongRoom;
import com.attu.util.Maybe;

/**
 * Created by patrick on 5/29/15.
 */

public class SRLocationListener implements LocationListener {
    APIUser user;

    public SRLocationListener(APIUser user) {
        this.user = user;
    }

    @Override
    public void onLocationChanged(Location location) {
        user.setLocation(location);
        if (!user.inRoom()) {
            Maybe<SongRoom> toJoin = user.getJoinable();
            Log.d("LocationListener", "NOW WE SEND A PUSH NOTIFICATION");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
