package com.example.rtbilli.myapplication.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rtbilli.myapplication.R;
import com.example.rtbilli.myapplication.receiver.LocationReceiver;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by rtbilli on 11/6/2016.
 */
public class LocationActivity extends LocationBasedActivity {

    int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private ToggleButton mAlarmToggle;

    private TextView alarmTextView;
    private TextView locationDataTextView;


    private NumberPicker mMinutePicker;
    private NumberPicker mSecondPicker;

    public LocationManager locationManager;

    public LocationAlarmListener listener;
    public Location initialBestLocation = null;
    public Location lastBestLocation = null;

    private boolean hasStarted = false;

    private static double currentLat = 0;
    private static double currentLon = 0;

    private static WeakReference<LocationActivity> inst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        mAlarmToggle = (ToggleButton) findViewById(R.id.button_set_alarm);
        alarmTextView = (TextView) findViewById(R.id.alarm_text);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mMinutePicker = (NumberPicker) findViewById(R.id.numberPickerMin_Loc);
        mSecondPicker = (NumberPicker) findViewById(R.id.numberPickerSec_Loc);

        locationDataTextView = (TextView) findViewById(R.id.alarm_location_text);

        mMinutePicker.setMaxValue(59);
        mSecondPicker.setMaxValue(59);

        mMinutePicker.setMinValue(0);
        mSecondPicker.setMinValue(0);

        mAlarmToggle.setEnabled(false);

        mAlarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {

                    hasStarted = true;

                    Integer minute = mMinutePicker.getValue();
                    Integer second = mSecondPicker.getValue();


                    new CountDownTimer((minute * 60 * 10000) + (second * 1000), 1000) {

                        public void onTick(long millisUntilFinished) {
                            setAlarmText("seconds remaining: " + (millisUntilFinished) / 1000);
                            if (lastBestLocation != null)
                                Log.e("TAG", lastBestLocation.getAccuracy()+"");
                            if (initialBestLocation != null)
                                Log.e("TAG", initialBestLocation.getAccuracy()+"");
                            if (lastBestLocation != null && initialBestLocation != null)
                                locationDataTextView.setText(lastBestLocation.distanceTo(initialBestLocation) + "m");
                        }

                        public void onFinish() {
                            setAlarmText("Get Up And Walk!");
                        }
                    }.start();


                    Calendar calendar = Calendar.getInstance(); //calendar Nov 8 2016 -> 12378192180
                    long time = calendar.getTimeInMillis();
                    time += (minute * 60 * 1000) + (second * 1000);

                    Log.e("LocationActivity", "AFTER:  " + time);
                    setAlarmText(time + "");


                } else {
                    alarmManager.cancel(pendingIntent);
                    setAlarmText("Set Location");
                }

            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        inst = new WeakReference<>(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationAlarmListener();
        Log.e("TAG", "START");
        Log.e("TAG", ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ? "FINE TRUE" : "FINE FALSE");
        Log.e("TAG", ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ? "COARSE TRUE" : "COARSE FALSE" );
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            return;
        }

        Location l1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location l2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (l1 != null) {
            initialBestLocation = l1;
            locationDataTextView.setText("READY");
            mAlarmToggle.setEnabled(true);
        }
        if (l2 != null) {
            initialBestLocation = l2;
            locationDataTextView.setText("READY");
            mAlarmToggle.setEnabled(true);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 3000, 0, listener);
        Log.e("TAG", "START REQUEST");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(listener);
        Log.e("TAG", "DESTROY");
    }

    public static LocationActivity instance() {
        return inst.get();
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    public Location getInitialLocation() {
        return initialBestLocation;
    }

    public Location getLastBestLocation() {
        return lastBestLocation;
    }


    public class LocationAlarmListener implements LocationListener {

        public void onLocationChanged(final Location loc) {

            if (hasStarted)
                lastBestLocation = loc;
            else
                initialBestLocation = loc;

            Log.e("TAG",loc.toString());

            loc.getLatitude();
            loc.getLongitude();

            locationDataTextView.setText("Ready!");

            mAlarmToggle.setEnabled(true);

//            locationDataTextView.setText(String.valueOf(loc.getLatitude()).substring(0, 4) + " " + String.valueOf(loc.getLongitude()).substring(0,5));

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.e("TAG",s);
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();


        }

    }
}
