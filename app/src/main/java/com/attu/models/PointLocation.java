package com.attu.models;

import android.location.Location;

/**
 * We need to roll our own because the Android Location class is tightly coupled with the android VM and we can't use
 * it in tests without messing up many things
 */
public class PointLocation extends ServerLinked {
    private double latitude;
    private double longitude;

    public PointLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static PointLocation fromLocation(Location location) {
        return new PointLocation(location.getLatitude(), location.getLongitude());
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double distanceTo(PointLocation pointLocation) {
        double dLat = latitude - pointLocation.latitude;
        double dLong = longitude - pointLocation.longitude;
        return Math.sqrt(dLat * dLat + dLong * dLong);
    }
}
