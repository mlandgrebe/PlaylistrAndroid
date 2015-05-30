package com.attu.models;

/**
 * Created by patrick on 5/29/15.
 */
public class Vote extends ServerLinked {
    private APIUser castBy;
    private boolean isUp;

    // Exposing this constructor would be ugly


    public Vote(APIUser castBy, boolean isUp) {
        this.castBy = castBy;
        this.isUp = isUp;
    }

    public static Vote upvote(APIUser castBy) {
        return new Vote(castBy, true);
    }

    public static Vote downvote(APIUser castBy) {
        return new Vote(castBy, false);
    }

    public void submit() {
        server.submitVote(castBy.getId(), isUp);
    }
}
