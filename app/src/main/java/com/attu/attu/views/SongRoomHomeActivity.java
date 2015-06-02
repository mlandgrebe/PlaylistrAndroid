package com.attu.attu.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.attu.attu.R;
import com.attu.models.Song;
import com.attu.models.SongRoom;
import com.attu.models.Vote;
import com.attu.util.Maybe;
import com.attu.util.State;
import com.spotify.sdk.android.player.Player;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SongRoomHomeActivity extends Activity implements Observer, Runnable{
    private String name;
    private String plist;
    private SongRoomHomeThread upThread;

    TableLayout queue_tracks_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SRHA", "onCreate");
        setContentView(R.layout.activity_song_room_home);
        Button update = (Button) findViewById(R.id.update_button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongRoomHomeActivity x = (SongRoomHomeActivity) v.getContext();
                x.queue_tracks_table.removeAllViews();
                runOnUiThread(x);
            }
        });
        Button addToQueue = (Button) findViewById(R.id.add_button);
        addToQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context con = v.getContext();
                Intent i = new Intent(con, PlayListQueueActivity.class);
                startActivity(i);
            }
        });
        TextView title = (TextView) findViewById(R.id.srName);
        title.setText((String) getIntent().getSerializableExtra("srname"));
        State s = State.getState();
        if(s.getUser().getHostStatus()) {
            name = (String) getIntent().getSerializableExtra("srname");
            plist = (String) getIntent().getSerializableExtra("plist");


            queue_tracks_table = (TableLayout) findViewById(R.id.queue_tracks_table);

            upThread = new SongRoomHomeThread();
            upThread.toUpdate = this;
            upThread.name = name;
            upThread.uri = plist;
            upThread.addObserver(this);
            Thread t = new Thread(upThread);
            t.start();
        }
        if(!s.getUser().getHostStatus()){
            upThread = new SongRoomHomeThread();
            upThread.addObserver(this);
            Thread t = new Thread(upThread);
            t.start();
        }
    }

    public void run() {
        Log.d("SRHA", "run starting");
        State st = State.getState();
        final SongRoom room = State.getState().getUser().getSongRoom();
        if (st.getUser().getHostStatus()) {
            Maybe<Song> toplay = room.popSong();
            if (!toplay.isEmpty()) {
                Player mPlayer = st.getPlayer();
                Song toPlay = (Song) toplay.getVal();
                // start: just added
                //SongRoom room = State.getState().getUser().getSongRoom();

                Maybe<Song> nextSong = room.popSong();
                if (!nextSong.isEmpty()) {
                    Song song = nextSong.getVal();
                    room.startPlaying();
                    mPlayer.play(song.getSpotifyURI());
                }
                // end: just added
                mPlayer.play(toPlay.getSpotifyURI());
            }
        }

        TextView t1, t2;
        Button up, down;
        TableRow row;
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        final State state = State.getState();
        final List<Song> songs = state.getUser().getSongRoom().getQueue().getSongs();
        for (final Song plTrack : songs) {
            row = new TableRow(this);
            t1 = new TextView(this);
            up = new Button(this);
            down = new Button(this);

            up.setText("+");
            down.setText("-");

            String name = plTrack.getName();
            Log.d("SRHomeActivity", name);
            t1.setTextSize(15);

            String s;
            if(name.length() > 12){
                s = name.substring(0,11);
                t1.setText(s + "...");
            }
            else{
                t1.setText(name);
            }

            t1.setTypeface(null, 1);

            t1.setTextSize(15);
            up.setTextSize(25);
            down.setTextSize(25);

            t1.setWidth(150 * dip);
            //t1.setPadding(20 * dip, 0, 0, 0);

            t2 = new TextView(this);
            t2.setText("0");
            if(plTrack.getVotes() != null) {
                List<Vote> votes = plTrack.getVotes();
                int tot = 0;
                for(Vote v: votes){
                    if(v.isUp()){
                        tot += 1;
                    }
                    if(!v.isUp()){
                        tot -= 1;
                    }
                }
                String t2txt = new Integer(tot).toString();
                t2.setText(t2txt);
            }
            t2.setTypeface(null, 1);
            t2.setTextSize(15);
            t2.setWidth(25 * dip);


            up.setWidth(50 * dip);
            down.setWidth(50 * dip);
            up.setBackgroundColor(Color.alpha(0));
            down.setBackgroundColor(Color.alpha(0));
            up.setTextColor(Color.GREEN);
            down.setTextColor(Color.RED);
            up.setOnClickListener(new UpThumb(plTrack));
            down.setOnClickListener(new DownThumb(plTrack));

            row.addView(t1);
            row.addView(up);
            row.addView(down);
            row.addView(t2);

            row.setTag(plTrack);

            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Log.d("SRHA", "queue_tracks_table null ? " + (queue_tracks_table == null));

            queue_tracks_table = (TableLayout) findViewById(R.id.queue_tracks_table);

            Log.d("SRHA", "queue_tracks_table null ? " + (queue_tracks_table == null));

            queue_tracks_table.addView(row, params);

            row.setClickable(true);
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_room_home, menu);
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
}
