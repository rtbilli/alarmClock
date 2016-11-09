package com.example.rtbilli.myapplication.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rtbilli.myapplication.R;
import com.example.rtbilli.myapplication.receiver.LocationReceiver;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by rtbilli on 11/6/2016.
 */
public class LocationActivity extends AppCompatActivity{

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TextView alarmTextView;
    private ToggleButton alarmToggle;

    private NumberPicker mMinutePicker;
    private NumberPicker mSecondPicker;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static double currentLat = 0;
    private static double currentLon = 0;

    private static WeakReference<LocationActivity> inst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        alarmToggle = (ToggleButton) findViewById(R.id.button_set_alarm);
        alarmTextView = (TextView) findViewById(R.id.alarm_text);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mMinutePicker = (NumberPicker)findViewById(R.id.numberPickerMin_Loc);
        mSecondPicker = (NumberPicker)findViewById(R.id.numberPickerSec_Loc);

        mMinutePicker.setMaxValue(59);
        mSecondPicker.setMaxValue(59);

        mMinutePicker.setMinValue(0);
        mSecondPicker.setMinValue(0);

        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {

                    Integer minute = mMinutePicker.getValue();
                    Integer second = mSecondPicker.getValue();

                    setAlarmText(minute + ":" + second);

                    Log.e("LocationActivity","BEFORE: " + Calendar.getInstance().getTimeInMillis()+"");
                    Calendar calendar = Calendar.getInstance(); //calendar Nov 8 2016 -> 12378192180
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, second); // caklendar Nov 8 2016 -> 12378199000

                    Log.e("LocationActivity","AFTER:  " + calendar.getTimeInMillis()+"");
                    setAlarmText(Calendar.getInstance().getTimeInMillis() + ":" + calendar.getTimeInMillis());
                    Intent myIntent = new Intent(LocationActivity.this, LocationReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(LocationActivity.this, 0, myIntent, 0);
                    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

                } else {
                    alarmManager.cancel(pendingIntent);
                    setAlarmText("Set");
                }

            }
        });

    }

    public static LocationActivity instance() {
        return inst.get();
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = new WeakReference<>(this);
    }


    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}
