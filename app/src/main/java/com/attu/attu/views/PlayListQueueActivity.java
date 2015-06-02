package com.attu.attu.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.content.Context;
import com.attu.display.TemporaryColor;

import com.attu.attu.R;

import java.util.Observable;
import java.util.Observer;

import kaaes.spotify.webapi.android.models.*;

import static android.graphics.Color.GRAY;

public class PlayListQueueActivity extends Activity implements Observer, Runnable {

    private PlayListQueueThread upThread;
    TableLayout playlist_queue_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Starting", "Update Playlist Tracks");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_queue);
        playlist_queue_table = (TableLayout)findViewById(R.id.playlist_queue_table);

        upThread = new PlayListQueueThread();
        upThread.toUpdate = this;
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
        TextView t1, t2;
        TableRow row;
        //Converting to dip unit
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
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

            t1.setWidth(50 * dip);
            t2.setWidth(150 * dip);
            t1.setPadding(20 * dip, 0, 0, 0);
            row.addView(t1);
            row.addView(t2);
            row.setTag(play.id);

            playlist_queue_table.addView(row, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            row.setClickable(true); //allows you to select a specific row
//            row.setOnTouchListener(new TemporaryColor(GRAY));
            row.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    // here is where we will need to swap out view with view of the playlist's tracks
//                    v.setBackgroundColor(Color.GRAY);
//                    System.out.println("Row clicked: " + v.getId());
                    Context x = v.getContext();
                    Intent i = new Intent(x, QueueSongActivity.class);
                    i.putExtra("plist", (String)v.getTag());
                    startActivity(i);
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
