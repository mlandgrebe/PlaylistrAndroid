package com.attu.util;

/**
 * Created by patrick on 5/29/15.
 */
public class Maybe<T> {
    T val;

    public Maybe() {
    }

    public Maybe(T val) {
        this.val = val;
    }

    public boolean isEmpty() {
        return val == null;
    }

    public T getVal() {
        return val;
    }
}
