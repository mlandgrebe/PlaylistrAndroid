package com.attu.models;

/**
 * Created by patrick on 5/29/15.
 */
public class Vote extends ServerLinked {
    private APIUser user;
    private Song song;
    private boolean isUp;


    // Exposing this constructor would be ugly

    public Vote(APIUser user, Song song, boolean isUp) {
        this.user = user;
        this.song = song;
        this.isUp = isUp;
    }


    public APIUser getUser() {
        return user;
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
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + song.hashCode();
        result = 31 * result + (isUp ? 1 : 0);
        return result;
    }
}
