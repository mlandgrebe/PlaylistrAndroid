package com.attu.attu.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.attu.attu.R;

import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

import com.attu.models.APIUser;
import com.attu.models.SongRoom;
import com.attu.remote.Server;

public class SongRoomHomeActivity extends Activity implements Observer, Runnable{
    private APIUser self;
    private SpotifyService spotify;
    private Server server;
    // need to save this to give to the next activity
    // to make a server
    private String serverURL;
    // spotify ID to give to next  activity
    private String SID;
    private SongRoom room;
    private String name;
    private SongRoomHomeThread upThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_room_home);
        serverURL = (String) getIntent().getSerializableExtra("serverUrl");
        SpotifyApi api = new SpotifyApi();
        SID = (String) getIntent().getSerializableExtra("spotifyToken");
        api.setAccessToken(SID);
        spotify = api.getService();
        name = (String) getIntent().getSerializableExtra("srname");

        upThread = new SongRoomHomeThread();
        upThread.spotify = spotify;
        upThread.name = name;
        upThread.serverURL = serverURL;
        upThread.addObserver(this);
        Thread t = new Thread(upThread);
        t.start();

    }
    public void run(){

    }

    public void update(Observable obs, Object obj) {


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

    View.OnClickListener viewQueue = new View.OnClickListener() {
        public void onClick(View v) {
            Context con = v.getContext();
            Intent i = new Intent(con, UpdatePlaylistsActivity.class);
            i.putExtra("spotifyToken", SID);
            startActivity(i);
        }
    };
    View.OnClickListener myMusic = new View.OnClickListener() {
        public void onClick(View v) {
            Context con = v.getContext();
            Intent i = new Intent(con, UpdatePlaylistsActivity.class);
            i.putExtra("spotifyToken", SID);
            startActivity(i);
        }
    };
    View.OnClickListener otherUsers = new View.OnClickListener() {
        public void onClick(View v) {
            Context con = v.getContext();
            Intent i = new Intent(con, UpdatePlaylistsActivity.class);
            i.putExtra("spotifyToken", SID);
            startActivity(i);
        }
    };
}
