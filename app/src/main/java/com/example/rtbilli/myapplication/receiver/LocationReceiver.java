package com.example.rtbilli.myapplication.receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.rtbilli.myapplication.activity.LocationActivity;
import com.example.rtbilli.myapplication.service.LocationAlarmService;

/**
 * Created by bburton on 11/8/16.
 */

public class LocationReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //this will update the UI with message
        if (LocationActivity.instance() != null) {

            LocationActivity inst = LocationActivity.instance();

            if (inst != null) {
                if (inst.getInitialLocation() != null && inst.getLastBestLocation() != null)
                    inst.setAlarmText("Get Up And Walk!");
                else
                    inst.setAlarmText("Not enough info");
            }
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null) {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
//        ringtone.play();

            //this will send a notification message
            ComponentName comp = new ComponentName(context.getPackageName(),
                    LocationAlarmService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }
    }
}
