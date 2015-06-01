package com.attu.data;

/**
 * Created by patrick on 5/31/15.
 */
public class MotionInstant {
    private final long timestamp;
    private double norm;

    public MotionInstant(float[] values, long timestamp) {
        this.timestamp = timestamp;

        for (int i = 0; i < 3; i++) {
            norm += values[i] * values[i];
        }
        norm = Math.sqrt(norm);
    }

    public MotionInstant(long timestamp, double norm) {
        this.timestamp = timestamp;
        this.norm = norm;
    }

    @Override
    public String toString() {
        return "{" +
                "\"timestamp\":" + timestamp +
                ", \"norm\":" + norm +
                '}';
    }
}
