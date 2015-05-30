package com.attu.attu;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.attu.models.APIUser;

/**
 * Created by patrick on 5/29/15.
 */
// TODO: All of this
public class SRLocationListener implements LocationListener {
    APIUser user;

    public SRLocationListener(APIUser user) {
        this.user = user;
    }

    @Override
    public void onLocationChanged(Location location) {
        user.setLocation(location);
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
