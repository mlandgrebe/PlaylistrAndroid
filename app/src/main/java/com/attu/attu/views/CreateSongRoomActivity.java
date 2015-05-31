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
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.attu.attu.R;

import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.*;

import com.attu.models.*;
import com.attu.remote.*;

public class CreateSongRoomActivity extends Activity implements
        View.OnClickListener, Observer, Runnable {

    private CreateSongRoomThread upThread;
    public SpotifyService spotify;
    // Access Token String
    private String SID;

    Server server;
    String serverURL;
    String plistTag;

    EditText text_sr_input;
    TableLayout playlist_options;
    Button button_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song_room);
        text_sr_input = (EditText)this.findViewById(R.id.text_sr_input);
        //view_init_queue_opts = (ScrollView)findViewById(R.id.view_init_queue_opts);
        playlist_options = (TableLayout)findViewById(R.id.playlist_options);

        // get button and set a listener on it in order to move to next view
        button_done = (Button)this.findViewById(R.id.button_done);
        button_done.setOnClickListener(this);

        // get serverURL from MainActivity and instantiate server
        serverURL = (String) getIntent().getSerializableExtra("server");
        //server = new Server(serverURL);

        // we get token this token from MainActivity
        SID = (String) getIntent().getSerializableExtra("spotifyToken");

        // create Spotify API instance and set AccessToken
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(SID);
        spotify = api.getService();

        // create thread to fetch user's playlists, which may be used to init SongRoom
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
        //Log.d("observer", "running");
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        //Log.d("observer", "calculated dip");
        for (PlaylistSimple play : upThread.l) {
            row = new TableRow(this);
            t1 = new TextView(this);
            t1.setText(play.name);
            t1.setTypeface(null, 1);
            t1.setTextSize(15);
            t1.setWidth(350 * dip);
            t1.setPadding(20 * dip, 0, 0, 0);
            row.addView(t1);
            row.setTag(play.id);
            playlist_options.addView(row, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            row.setClickable(true); //allows you to select a specific row
            //Log.d("observer", "set clickable");
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // change a clicked row's color to gray & get playlist ID for selected playlist
                    v.setBackgroundColor(Color.GRAY);
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
        String name = text_sr_input.getText().toString();
        Log.d("Text Entered: ", name);
        if (name != null) {
            if (plistTag != null) {

                // call createSR that Patrick needs to write
                // it should allow user to set a playlist for the sr
            }
            else{
                // call createSR that Patrick needs to write
                // it should allow user to set a playlist for the sr
            }
            Intent intent = new Intent(this, SongRoomHomeActivity.class);
            intent.putExtra("srname", name);
            intent.putExtra("spotifyToken", SID);
            intent.putExtra("plist", plistTag);

            // instantiate a server, create SongRoom, and move to SongRoomHomeActivity
            Server server = new Server(serverURL);
            //server.dropUsers();

            User spotifyUser = spotify.getMe();

            APIUser user = server.createUser(spotifyUser);
            // need to get the actual location, which should work now
            PointLocation loc = new PointLocation(15, 16);
            SongRoom room = server.createSR(user.getId(), loc, name);

            Log.d("Got passed songroom", "Awesome");

            intent.putExtra("songroom", room);
            intent.putExtra("serverUrl", serverURL);
            this.startActivity(intent);
        }
    }
}