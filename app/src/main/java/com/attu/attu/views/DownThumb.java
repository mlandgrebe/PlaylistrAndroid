package com.attu.attu.views;
/**
 * Created by zacharyjenkins on 6/1/15.
 */
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import com.attu.models.Song;
import com.attu.models.*;
import com.attu.util.*;

import kaaes.spotify.webapi.android.models.PlaylistTrack;

public class DownThumb implements OnClickListener {
    private Song toThumb;
    public DownThumb(Song song) {
        toThumb = song;
    }

    public void onClick(View v) {
//        v.setBackgroundColor(Color.GRAY);
        State state = State.getState();
        APIUser user = state.getUser();
        user.upvote(toThumb);
    }
}