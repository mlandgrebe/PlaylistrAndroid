package com.attu.data;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * We'll be watching the linear accelerometer
 */
public class MotionListener implements SensorEventListener {
    private final MotionMonitor monitor;

    public MotionListener(MotionMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        MotionInstant instant = new MotionInstant(sensorEvent.values, sensorEvent.timestamp);
        monitor.recordInstant(instant);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Don't care
    }
}
