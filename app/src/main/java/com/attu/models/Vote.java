package com.attu.models;

/**
 * Created by patrick on 5/29/15.
 */
public class Vote extends ServerLinked {
    private ObjectId user;
    private ObjectId song;
    private boolean isUp;

    public Vote(ObjectId user, ObjectId song, boolean isUp) {
        this.user = user;
        this.song = song;
        this.isUp = isUp;
    }



    public boolean isUp() {
        return isUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vote vote = (Vote) o;

        if (isUp != vote.isUp) return false;
        if (!user.equals(vote.user)) return false;
        return song.equals(vote.song);

    }


    @Override
    public String toString() {
        return "Vote{" +
                "user=" + user +
                ", song=" + song +
                ", isUp=" + isUp +
                "} " + super.toString();
    }
}
