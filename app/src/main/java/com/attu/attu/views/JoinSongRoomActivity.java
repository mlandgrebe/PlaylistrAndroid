package com.attu.attu.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
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
import com.attu.models.SongRoom;
import com.attu.util.State;

import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.models.PlaylistSimple;

public class JoinSongRoomActivity extends Activity
        implements Observer, Runnable {
    private JoinSongRoomThread upThread;
    private TableLayout songroom_options;
    private SongRoom toJoin;
    private Button button_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_song_room);

        songroom_options = (TableLayout)findViewById(R.id.songroom_options);

        button_join = (Button)this.findViewById(R.id.button_join);
        button_join.setOnClickListener(joinSongRoomHandler);
        // create thread to fetch user's playlists, which may be used to init SongRoom
        upThread = new JoinSongRoomThread();
        upThread.addObserver(this);
        Thread t = new Thread(upThread);
        t.start();
    }

    @Override
    public void run() {
        TextView t1, t2;
        TableRow row;
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        for (SongRoom room : upThread.availableRooms) {
            row = new TableRow(this);
            t1 = new TextView(this);
            t1.setText(room.getName());
            t1.setTypeface(null, 1);
            t1.setTextSize(15);
            t1.setWidth(350 * dip);
            t1.setPadding(20 * dip, 0, 0, 0);
            row.addView(t1);
            row.setTag(room);
            songroom_options.addView(row, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            row.setClickable(true); //allows you to select a specific row
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // change a clicked row's color to gray & get playlist ID for selected playlist
//                    v.setBackgroundColor(Color.GRAY);
                    toJoin = (SongRoom)v.getTag();
                }
            });
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        runOnUiThread(this);
    }

    View.OnClickListener joinSongRoomHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("Click", "Clicked join");
            Intent intent = new Intent(v.getContext(), SongRoomHomeActivity.class);
            State state = State.getState();
            state.getUser().setHostStatus(false);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    State state = State.getState();
                    state.getUser().joinSR(toJoin);
                }
            };
            r.run();
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_song_room, menu);
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
