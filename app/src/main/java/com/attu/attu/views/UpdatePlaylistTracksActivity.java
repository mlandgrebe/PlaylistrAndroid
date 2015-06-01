package com.attu.attu.views;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;


import com.attu.attu.R;

import java.util.Observable;
import java.util.Observer;

import com.attu.util.State;
import com.spotify.sdk.android.player.Player;
import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.*;

public class UpdatePlaylistTracksActivity extends Activity implements Observer, Runnable {
    private UpdatePlaylistTracksThread upThread;
    public SpotifyService spotify;
    public String plist;

    TableLayout playlist_tracks_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Starting", "Update Playlist Tracks");
        plist = (String) getIntent().getSerializableExtra("plist");
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken((String) getIntent().getSerializableExtra("spotifyToken"));
        spotify = api.getService();
        setContentView(R.layout.activity_update_playlist_tracks);
        playlist_tracks_table = (TableLayout)findViewById(R.id.playlist_tracks_table);

        upThread = new UpdatePlaylistTracksThread();
        upThread.toUpdate = this;
        upThread.uri = plist;
        upThread.addObserver(this);
        Thread t = new Thread(upThread);
        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void run(){
        final State state = State.getState();
        TextView t1, t2;
        TableRow row;
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        for (PlaylistTrack plTrack : upThread.ts) {
            row = new TableRow(this);

            t1 = new TextView(this);
            t2 = new TextView(this);

            t1.setText(plTrack.track.name);
            t2.setText(plTrack.track.uri);
            t1.setTypeface(null, 1);
            t2.setTypeface(null, 1);

            t1.setTextSize(15);
            t2.setTextSize(15);

            t1.setWidth(50 * dip);
            t2.setWidth(150 * dip);
            t1.setPadding(20 * dip, 0, 0, 0);
            row.addView(t1);
            row.addView(t2);
            row.setTag(plTrack.track.uri);

            playlist_tracks_table.addView(row, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            row.setClickable(true); //allows you to select a specific row
            row.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    // here is where we will need to swap out view with
                    // song details view, need to handle this differently
                    // for Admin & User and in SongRoom or Your Music
                    //v.setBackgroundColor(Color.GRAY);
                    //System.out.println("Row clicked: " + v.getId());
                    //Track toPlay = (Track) v.getTag();
                    Player mPlayer = state.getPlayer();
                    mPlayer.play((String)v.getTag());
                }

            });

        }
    }

    public void update(Observable obs, Object obj){
        Log.d("Update", "Observer Notified");

        if(upThread == null){
            Log.d("Null", "Going to Crash");
        }
        runOnUiThread(this);
    }
}