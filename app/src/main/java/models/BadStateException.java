package models;

/**
 * Created by patrick on 5/29/15.
 */
public class BadStateException extends Exception {
    public BadStateException(String detailMessage) {
        super(detailMessage);
    }
}
