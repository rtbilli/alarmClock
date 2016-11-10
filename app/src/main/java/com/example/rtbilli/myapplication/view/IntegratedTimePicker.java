package com.example.rtbilli.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

/**
 * Created by bburton on 11/9/16.
 */

public class IntegratedTimePicker extends TimePicker {

    public IntegratedTimePicker(Context context) {
        super(context);
    }

    public IntegratedTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntegratedTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
}
