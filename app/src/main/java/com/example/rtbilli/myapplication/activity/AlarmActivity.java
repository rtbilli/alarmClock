package com.example.rtbilli.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import com.example.rtbilli.myapplication.R;

/**
 * Created by rtbilli on 11/6/2016.
 */
public class AlarmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        TimePicker tp = (TimePicker)findViewById(R.id.timePicker);
    }
}
