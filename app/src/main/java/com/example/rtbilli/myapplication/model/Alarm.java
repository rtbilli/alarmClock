package com.example.rtbilli.myapplication.model;

/**
 * Created by bburton on 11/9/16.
 */

public class Alarm {

    private long mTime;

    private String mName;

    public Alarm(String name, long time) {

        mName = name;

        mTime = time;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
