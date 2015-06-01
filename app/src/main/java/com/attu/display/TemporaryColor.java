package com.attu.display;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by patrick on 6/1/15.
 */
public class TemporaryColor implements View.OnTouchListener {
    int color;
    int oldColor;
    boolean beenSet;

    public TemporaryColor(int color) {
        this.color = color;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d("TemporaryColor", "onHover");
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            view.setBackgroundColor(oldColor);
        } else if (!beenSet && motionEvent.getAction() == MotionEvent.ACTION_UP) {
            oldColor = view.getDrawingCacheBackgroundColor();
            beenSet = true;
            view.setBackgroundColor(color);
        }
        return true;
    }
}
