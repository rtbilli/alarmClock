package com.example.rtbilli.myapplication.activity;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rtbilli.myapplication.R;
import com.example.rtbilli.myapplication.view.IntegratedTimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by bburton on 11/9/16.
 */

public class DateTimeActivity extends LocationBasedActivity {

    private LinearLayout mDateLayout;
    private LinearLayout mTimeLayout;
    private LinearLayout mLayoutRepeat;

    private DatePicker mDatePicker;
    private IntegratedTimePicker mTimePicker;

    private Spinner mChoiceSpinner;
    private Spinner mTimeZoneSpinner;

    private Button mButtonSetAlarm;

    private ToggleButton mToggleButtonSun;
    private ToggleButton mToggleButtonMon;
    private ToggleButton mToggleButtonTue;
    private ToggleButton mToggleButtonWed;
    private ToggleButton mToggleButtonThu;
    private ToggleButton mToggleButtonFri;
    private ToggleButton mToggleButtonSat;

    private Calendar current;
    private long milliseconds;
    private ArrayAdapter<String> mTimeZoneAdapter;
    private SimpleDateFormat sdf;
    private Date resultdate;

    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    public LocationManager locationManager;
    public Location previousBestLocation = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        mDateLayout = (LinearLayout)findViewById(R.id.layout_date);
        mTimeLayout = (LinearLayout)findViewById(R.id.layout_time);
        mLayoutRepeat = (LinearLayout)findViewById(R.id.layout_repeat);

        mDatePicker = (DatePicker)findViewById(R.id.date_datetime);
        mTimePicker = (IntegratedTimePicker)findViewById(R.id.time_datetime);

        mButtonSetAlarm = (Button)findViewById(R.id.button_set_dtalarm);

        mToggleButtonSun = (ToggleButton)findViewById(R.id.toggleSun);
        mToggleButtonMon = (ToggleButton)findViewById(R.id.toggleMon);
        mToggleButtonTue = (ToggleButton)findViewById(R.id.toggleTue);
        mToggleButtonWed = (ToggleButton)findViewById(R.id.toggleWed);
        mToggleButtonThu = (ToggleButton)findViewById(R.id.toggleThu);
        mToggleButtonFri = (ToggleButton)findViewById(R.id.toggleFri);
        mToggleButtonSat = (ToggleButton)findViewById(R.id.toggleSat);

        mChoiceSpinner = (Spinner)findViewById(R.id.spinner_select);
        mTimeZoneSpinner = (Spinner)findViewById(R.id.spinner_timezone);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alarm_choice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mChoiceSpinner.setAdapter(adapter);

        mDateLayout.setVisibility(View.GONE);
        mTimeLayout.setVisibility(View.GONE);
        mLayoutRepeat.setVisibility(View.GONE);

        String[] idArray = TimeZone.getAvailableIDs();

        sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");

        mTimeLayout.setFocusable(true);
        mTimeLayout.setFocusableInTouchMode(true);

        mTimeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("TAG","OK");
                view.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });



        mTimeZoneAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, idArray);

        mTimeZoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeZoneSpinner.setAdapter(mTimeZoneAdapter);

        mChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        mTimeLayout.setVisibility(View.VISIBLE);
                        mDateLayout.setVisibility(View.VISIBLE);
                        mLayoutRepeat.setVisibility(View.GONE);
                        break;
                    case 1:
                        mTimeLayout.setVisibility(View.VISIBLE);
                        mDateLayout.setVisibility(View.GONE);
                        mLayoutRepeat.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mChoiceSpinner.setFocusableInTouchMode(true);

        mTimeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getGMTTime();
                String selectedId = (String) (adapterView.getItemAtPosition(i));

                TimeZone timezone = TimeZone.getTimeZone(selectedId);
                String TimeZoneName = timezone.getDisplayName();

                int TimeZoneOffset = timezone.getRawOffset()
                        / (60 * 1000);

                int hrs = TimeZoneOffset / 60;
                int mins = TimeZoneOffset % 60;

                milliseconds = milliseconds + timezone.getRawOffset();

                resultdate = new Date(milliseconds);
                System.out.println(sdf.format(resultdate));

                Toast.makeText(DateTimeActivity.this, "" + sdf.format(resultdate),Toast.LENGTH_SHORT).show();

                milliseconds = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mButtonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DateTimeActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
            }
        });

        setTimezone();
        Toast.makeText(DateTimeActivity.this, TimeZone.getDefault().getID(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTimezone();
    }

    // Convert Local Time into GMT time

    private void getGMTTime() {
        current = Calendar.getInstance();
        Log.e(" ",current.getTime().toString());

        milliseconds = current.getTimeInMillis();

        TimeZone tzCurrent = current.getTimeZone();
        int offset = tzCurrent.getRawOffset();
        if (tzCurrent.inDaylightTime(new Date())) {
            offset = offset + tzCurrent.getDSTSavings();
        }

        milliseconds = milliseconds - offset;

        resultdate = new Date(milliseconds);
        System.out.println(sdf.format(resultdate));
    }

    private void setTimezone() {
        for(int i = 0; i < mTimeZoneSpinner.getCount(); i++){
            Log.e("TAG", mTimeZoneAdapter.getItem(i));
            Log.e("TAG", TimeZone.getDefault().getID());
            if(mTimeZoneAdapter.getItem(i).equals(TimeZone.getDefault().getID())){
                Log.e("TAG", "SET SELECTION: " + i);
                mTimeZoneSpinner.setSelection(i);
                break;
            }
        }
    }
}



