package com.attu.attu.views;

import android.app.Activity;
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
import com.attu.display.ColorOnHover;
import com.attu.models.Song;
import com.attu.models.SongQueue;
import com.attu.models.SongRoom;
import com.attu.models.APIUser;
import com.attu.util.State;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.SpotifyService;

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
        State s = State.getState();
        SpotifyService spotify = s.getSpotifyService();
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
        //State state = State.getState();
        //APIUser apiUser = state.getUser();
        //SongRoom songRoom = apiUser.getSongRoom();
        //SongQueue songQueue = songRoom.getQueue();
        //List<Song> songs = songQueue.getSongs();

        //for (Song s : songs) {
        //}

        TextView t1, t2;
        Button up, down;
        TableRow row;
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        State state = State.getState();
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

            if(name.length() > 23){
                String s = name.substring(0, 23);
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
            row.setTag(plTrack);


            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Log.d("SRHA", "queue_tracks_table null ? " + (queue_tracks_table == null));

            queue_tracks_table = (TableLayout) findViewById(R.id.queue_tracks_table);

            Log.d("SRHA", "queue_tracks_table null ? " + (queue_tracks_table == null));

            queue_tracks_table.addView(row, params);

            row.setClickable(true); //allows you to select a specific row

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // here is where we will need to swap out view with
                    // song details view, need to handle this differently
                    // for Admin & User and in SongRoom or Your Music
                    v.setBackgroundColor(Color.GRAY);
                    System.out.println("Row clicked: " + v.getId());
                    //Track toPlay = (Track) v.getTag();
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
