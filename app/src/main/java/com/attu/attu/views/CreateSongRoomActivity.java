package com.attu.attu.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.attu.attu.R;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.*;

import com.attu.models.*;
import com.attu.remote.*;

public class CreateSongRoomActivity extends ActionBarActivity implements
        View.OnClickListener, Observer, Runnable {

    private CreateSongRoomThread upThread;
    public SpotifyService spotify;
    private String SID;
    TableLayout playlist_table;

    EditText field_name;
    Button button_name_done;
    TableLayout playlist_options;
    Server server;
    String serverURL;
    String plistTag;
    //Context plistContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song_room);
        field_name = (EditText)this.findViewById(R.id.field_name);
        button_name_done = (Button)this.findViewById(R.id.button_name_done);
        button_name_done.setOnClickListener(this);

        SpotifyApi api = new SpotifyApi();
        serverURL = (String) getIntent().getSerializableExtra("server");
        //server = new Server(serverURL);
        SID = (String) getIntent().getSerializableExtra("spotifyToken");
        api.setAccessToken(SID);
        spotify = api.getService();
        playlist_options = (TableLayout)findViewById(R.id.playlist_options);
        upThread = new CreateSongRoomThread();
        upThread.toUpdate = this;
        upThread.spotify = spotify;
        upThread.addObserver(this);
        Thread t = new Thread(upThread);
        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_song_room, menu);
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
        TextView t1, t2;
        TableRow row;
        //Converting to dip unit
        Log.d("observer", "running");
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        Log.d("observer", "calculated dip");
        for (PlaylistSimple play : upThread.l) {
            row = new TableRow(this);
            t1 = new TextView(this);
            t2 = new TextView(this);

            t1.setText(play.name);
            t2.setText(play.uri);
            t1.setTypeface(null, 1);
            t2.setTypeface(null, 1);

            t1.setTextSize(15);
            t2.setTextSize(15);

            t1.setWidth(350 * dip);
            t2.setWidth(150 * dip);
            t1.setPadding(20 * dip, 0, 0, 0);
            row.addView(t1);
            //row.addView(t2);
            row.setTag(play.id);
            playlist_options.addView(row, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            row.setClickable(true); //allows you to select a specific row
            Log.d("observer", "set clickable");
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // here is where we will need to swap out view with view of the playlist's tracks
                    v.setBackgroundColor(Color.GRAY);
                    //System.out.println("Row clicked: " + v.getId());
                    //plistContext = v.getContext();
                    plistTag = (String)v.getTag();
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

    public void onClick(View v) {
        String name = field_name.getText().toString();
        if (name != null) {
            if (plistTag != null) {
                // call createSR that Patrick needs to write
                // it should allow user to set a playlist for the sr
            }
            else{
                // call createSR that Patrick needs to write
                // it should allow user to set a playlist for the sr
            }
            Intent intent = new Intent(this, SongRoomActivity.class);
            intent.putExtra("srname", name);
            intent.putExtra("spotifyToken", SID);
            intent.putExtra("plist", plistTag);
            // use this to instantiate a server and make the songroom on
            // the songroom home activity
            intent.putExtra("server", serverURL);
            this.startActivity(intent);
        }
    }

}