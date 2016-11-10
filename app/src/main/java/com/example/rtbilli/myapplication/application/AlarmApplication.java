package com.example.rtbilli.myapplication.application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.example.rtbilli.myapplication.activity.LocationActivity;
import com.example.rtbilli.myapplication.model.Alarm;
import com.example.rtbilli.myapplication.receiver.LocationReceiver;
import com.example.rtbilli.myapplication.service.LocationAlarmService;

import java.util.ArrayList;

/**
 * Created by bburton on 11/9/16.
 */

public class AlarmApplication extends Application {

    LocationAlarmService ls;

    private static ArrayList<Alarm> mAlarms;

    @Override
    public void onCreate() {

        super.onCreate();

        ls = new LocationAlarmService();

        mAlarms = new ArrayList<>();
    }

    public static void addAlarm(Alarm s) {
        mAlarms.add(s);
    }


}
