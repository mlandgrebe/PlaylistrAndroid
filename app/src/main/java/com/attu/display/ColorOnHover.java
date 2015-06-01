package com.attu.display;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by patrick on 6/1/15.
 */
public class ColorOnHover implements View.OnHoverListener  {
    int color;
    int oldColor;

    public ColorOnHover(int color) {
        this.color = color;
    }

    @Override
    public boolean onHover(View view, MotionEvent motionEvent) {
        Log.d("ColorOnHover", "onHover");
        if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
            view.setBackgroundColor(oldColor);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
            oldColor = view.getDrawingCacheBackgroundColor();
            view.setBackgroundColor(color);
        }
        return true;
    }
}
