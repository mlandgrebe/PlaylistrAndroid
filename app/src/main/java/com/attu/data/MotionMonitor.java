package com.attu.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by patrick on 5/31/15.
 */
public class MotionMonitor {
    private volatile boolean listening;
    private List<MotionInstant> instants;

    public MotionMonitor() {
    }

    public void startListening() {
        listening = true;
    }

    public void stopListening() {
        listening = false;
    }

    public void recordInstant(MotionInstant instant) {
        if (listening) {
            instants.add(instant);
        }
    }

    public synchronized List<MotionInstant> clearHistory() {
        stopListening();
        List<MotionInstant> toReturn = instants;
        instants = new LinkedList<>();
        return toReturn;
    }
}
